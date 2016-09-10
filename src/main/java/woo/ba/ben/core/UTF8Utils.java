package woo.ba.ben.core;


import java.nio.charset.CharacterCodingException;
import java.nio.charset.CoderResult;

import static java.lang.Character.MIN_LOW_SURROGATE;
import static java.lang.Math.min;
import static java.nio.charset.CoderResult.OVERFLOW;
import static java.nio.charset.CoderResult.UNDERFLOW;
import static woo.ba.ben.core.IDataReader.unsignedByte;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class UTF8Utils {
    private static final int HIGH_SURROGATE_BASE = 55232;
    private static final FieldStruct stringValueField = ClassStructFactory.get(String.class).getField("value");


    static char[] getCharArrayDirectly(final String str) {
        if (str == null) {
            return null;
        }
        return (char[]) FieldAccessor.getObject(str, stringValueField);
    }

    static int encodingDestBlockSize(final char[] source, final int srcStartOffset, final int srcLimit) throws CharacterCodingException {
        if (source == null || srcStartOffset < 0 || srcLimit < 0) {
            throw new IllegalArgumentException();
        }

        int sourceStartOffset = srcStartOffset;
        final int sourceLength = source.length - sourceStartOffset;
        final int sourceRemaining = min(sourceLength, srcLimit);

        int size = 0;
        while (sourceStartOffset < sourceRemaining && source[sourceStartOffset] < 0x80) {
            sourceStartOffset++;
            size++;
        }

        int charValueInInt;
        for (int i = sourceStartOffset; i < sourceRemaining; i++) {
            charValueInInt = source[i];
            if (charValueInInt < 0x80) {
                size++;
            } else if (charValueInInt < 0x800) {
                size += 2;
            } else if (charValueInInt < 0x10000) {
                size += 3;
            } else if (charValueInInt < 0x200000) {
                size += 4;
            } else {
                throw new CharacterCodingException();
            }
        }
        return size;
    }

    static byte[] encode(final char[] source) throws CharacterCodingException {
        final int size = encodingDestBlockSize(source, 0, source.length);
        final byte[] destination = new byte[size];
        encode(source, 0, source.length, destination, 0, destination.length);
        return destination;
    }

    public static CoderResult decode(final byte[] source, final int srcStartOffset, final int srcLimit,
                                     final char[] destination, final int destStartOffset, final int destLimit) {
        if (source == null || destination == null ||
                srcStartOffset < 0 || srcLimit < 0 || destStartOffset < 0 || destLimit < 0) {
            throw new IllegalArgumentException();
        }

        int sourceStartOffset = srcStartOffset, destinationStartOffset = destStartOffset;
        final int sourceCount = min(source.length - sourceStartOffset, srcLimit);
        final int destinationCount = min(destination.length - destinationStartOffset, destLimit);

        final int smallerCount = min(sourceCount, destinationCount);
        while (destinationStartOffset < smallerCount && source[sourceStartOffset] >= 0) { //0 ~ 127
            destination[destinationStartOffset++] = (char) source[sourceStartOffset++];
        }

        int charValueInInt;
        while (sourceStartOffset < sourceCount) {
            charValueInInt = unsignedByte(source[sourceStartOffset++]);

            if (charValueInInt < 0x80) { //"01111111" == 0x7f
                //check remaining capacity
                if (destinationStartOffset + 1 > destinationCount) {
                    return OVERFLOW;
                }

                // 1 bit
                destination[destinationStartOffset++] = (char) charValueInInt;
            } else if (charValueInInt < 0xE0) { //"11000000" == 0xc0, "11011111" == 0xdf
                //check remaining capacity
                if ((destinationStartOffset + 1 > destinationCount) || (sourceStartOffset + 1 > sourceCount)) {
                    return OVERFLOW;
                }

                // two bytes into one char --> '11111 111111' is 2047
                destination[destinationStartOffset++] = (char) (((charValueInInt & 0x1F) << 6) | (source[sourceStartOffset++] & 0x3F));
            } else if (charValueInInt < 0xF0) { //"11100000" == 0xe0, "11101111" == 0xef
                //check remaining capacity
                if ((destinationStartOffset + 1 > destinationCount) || (sourceStartOffset + 2 > sourceCount)) {
                    return OVERFLOW;
                }

                // three bytes into one char --> '1111 111111 111111' is 65535
                destination[destinationStartOffset++] = (char) (((charValueInInt & 0x0F) << 12) | ((source[sourceStartOffset++] & 0x3F) << 6) | (source[sourceStartOffset++] & 0x3F));
            } else if (charValueInInt < 0xF8) { //"11110000" == 0xf0, "11110111" == 0xf7
                //check remaining capacity
                if ((destinationStartOffset + 2 > destinationCount) || (sourceStartOffset + 3 > sourceCount)) {
                    return OVERFLOW;
                }

                charValueInInt = ((charValueInInt & 0x0F) << 18) | ((source[sourceStartOffset++] & 0x3F) << 12) | ((source[sourceStartOffset++] & 0x3F) << 6) | (source[sourceStartOffset++] & 0x3F);

                // four bytes into two chars
                destination[destinationStartOffset++] = (char) ((charValueInInt >>> 10) + HIGH_SURROGATE_BASE);
                destination[destinationStartOffset++] = (char) ((charValueInInt & 0x3ff) + MIN_LOW_SURROGATE);
            } else {
                return OVERFLOW;
            }
        }
        return UNDERFLOW;
    }

    public static CoderResult encode(final char[] source, final int srcStartOffset, final int srcLimit,
                                     final byte[] destination, final int destStartOffset, final int destLimit) {
        if (source == null || destination == null ||
                srcStartOffset < 0 || srcLimit < 0 || destStartOffset < 0 || destLimit < 0) {
            throw new IllegalArgumentException();
        }

        int sourceStartOffset = srcStartOffset, destinationStartOffset = destStartOffset;
        final int sourceCount = min(source.length - sourceStartOffset, srcLimit);
        final int destinationCount = min(destination.length - destinationStartOffset, destLimit);

        if (destinationCount < sourceCount) {
            return OVERFLOW;
        }

        final int smallerCount = min(sourceCount, destinationCount);
        while (destinationStartOffset < smallerCount && source[sourceStartOffset] < 0x80) {
            destination[destinationStartOffset++] = (byte) source[sourceStartOffset++];
        }

        int charValueInInt;
        for (int i = sourceStartOffset; i < sourceCount; i++) {
            charValueInInt = source[i];

            if (charValueInInt < 0x80) {
                //check remaining capacity
                if (destinationStartOffset + 1 > destinationCount) {
                    return OVERFLOW;
                }

                // 1 bit
                destination[destinationStartOffset++] = (byte) charValueInInt;
            } else if (charValueInInt < 0x800) {
                //check remaining capacity
                if (destinationStartOffset + 2 > destinationCount) {
                    return OVERFLOW;
                }

                // 2 bits
                destination[destinationStartOffset++] = (byte) (0xC0 | charValueInInt >> 6);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else if (charValueInInt < 0x10000) {
                //check remaining capacity
                if (destinationStartOffset + 3 > destinationCount) {
                    return OVERFLOW;
                }

                // 3 bits
                destination[destinationStartOffset++] = (byte) (0xE0 | charValueInInt >> 12);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else if (charValueInInt < 0x200000) {
                //check remaining capacity
                if (destinationStartOffset + 4 > destinationCount) {
                    return OVERFLOW;
                }

                // 4 bits
                destination[destinationStartOffset++] = (byte) (0xF0 | charValueInInt >> 18);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt >> 12 & 0x3F);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else {
                return OVERFLOW;
            }
        }
        return UNDERFLOW;
    }

    public static CoderResult encode(final char[] source, final int srcStartOffset, final int srcLimit,
                                     final long destAddress, final int destStartOffset, final int destLimit) {
        if (source == null || destAddress < 0 ||
                srcStartOffset < 0 || srcLimit < 0 || destStartOffset < 0 || destLimit < 0) {
            throw new IllegalArgumentException();
        }

        int sourceStartOffset = srcStartOffset, destinationStartOffset = destStartOffset;
        final int sourceCount = min(source.length - sourceStartOffset, srcLimit);
        final int destinationCount = destLimit - destinationStartOffset;

        if (destinationCount < sourceCount) {
            return OVERFLOW;
        }

        final int smallerCount = min(sourceCount, destinationCount);
        while (destinationStartOffset < smallerCount && source[sourceStartOffset] < 0x80) {
            putByte(destAddress, destinationStartOffset++, (byte) source[sourceStartOffset++]);
        }

        int charValueInInt;
        for (int i = sourceStartOffset; i < sourceCount; i++) {
            charValueInInt = source[i];

            if (charValueInInt < 0x80) {
                //check remaining capacity
                if (destinationStartOffset + 1 > destinationCount) {
                    return OVERFLOW;
                }

                // 1 bit
                putByte(destAddress, destinationStartOffset++, (byte) charValueInInt);
            } else if (charValueInInt < 0x800) {
                //check remaining capacity
                if (destinationStartOffset + 2 > destinationCount) {
                    return OVERFLOW;
                }

                // 2 bits
                putByte(destAddress, destinationStartOffset++, (byte) (0xC0 | charValueInInt >> 6));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | charValueInInt & 0x3F));
            } else if (charValueInInt < 0x10000) {
                //check remaining capacity
                if (destinationStartOffset + 3 > destinationCount) {
                    return OVERFLOW;
                }

                // 3 bits
                putByte(destAddress, destinationStartOffset++, (byte) (0xE0 | charValueInInt >> 12));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | charValueInInt >> 6 & 0x3F));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | charValueInInt & 0x3F));
            } else if (charValueInInt < 0x200000) {
                //check remaining capacity
                if (destinationStartOffset + 4 > destinationCount) {
                    return OVERFLOW;
                }

                // 4 bits
                putByte(destAddress, destinationStartOffset++, (byte) (0xF0 | charValueInInt >> 18));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | charValueInInt >> 12 & 0x3F));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | charValueInInt >> 6 & 0x3F));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | charValueInInt & 0x3F));
            } else {
                return OVERFLOW;
            }
        }
        return UNDERFLOW;
    }

    private static void putByte(final long address, final int position, final byte value) {
        UNSAFE.putByte(address + position, value);
    }
}
