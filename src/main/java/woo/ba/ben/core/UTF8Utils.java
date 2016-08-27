package woo.ba.ben.core;


import java.lang.reflect.Field;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CoderResult;

import static java.lang.Math.min;
import static java.nio.charset.CoderResult.OVERFLOW;
import static java.nio.charset.CoderResult.UNDERFLOW;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class UTF8Utils {
    private static Field STRING_VALUE_FIELD = null;

    static {
        try {
            STRING_VALUE_FIELD = String.class.getDeclaredField("value");
            STRING_VALUE_FIELD.setAccessible(true);
        } catch (final NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    static char[] getCharArrayDirectly(final String str) {
        try {
            return (char[]) STRING_VALUE_FIELD.get(str);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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

//    static int decodingDestBlockSize(final byte[] source, final int srcStartOffset, final int srcLimit) throws CharacterCodingException {
//        if (source == null || srcStartOffset < 0 || srcLimit < 0) {
//            throw new IllegalArgumentException();
//        }
//
//        int sourceStartOffset = srcStartOffset;
//        final int sourceLength = source.length - sourceStartOffset;
//        final int sourceRemaining = min(sourceLength, srcLimit);
//
//        int size = 0;
//        while (sourceStartOffset < sourceRemaining && source[sourceStartOffset] > 0) {
//            sourceStartOffset++;
//            size++;
//        }
//
//        for (int i = sourceStartOffset; i < sourceRemaining; i++) {
//            if (source[i] > 0) {
//                size++;
//            } else {
//                switch (0xF0 & source[i]) {
//                    case 0xE0:
//                        i += 3;
//                        break;
//                    case 0xF0:
//                        i += 4;
//                        break;
//                    default:
//                        i += 2;
//                        break;
//                }
//            }
//        }
//        return size;
//    }


    static byte[] encode(final char[] source, final int srcStartOffset, final int srcLimit) throws CharacterCodingException {
        final int size = encodingDestBlockSize(source, srcStartOffset, srcLimit);
        final byte[] destination = new byte[size];
        encodeInternal(source, srcStartOffset, srcLimit, destination);
        return destination;
    }

    private static void encodeInternal(final char[] source, final int srcStartOffset, final int srcLimit, final byte[] destination) {
        int sourceStartOffset = srcStartOffset, destinationStartOffset = 0;
        final int sourceLength = source.length - sourceStartOffset;
        final int sourceRemaining = min(sourceLength, srcLimit);
        while (sourceStartOffset < sourceRemaining && source[sourceStartOffset] < 0x80) {
            destination[destinationStartOffset++] = (byte) source[sourceStartOffset++];
        }

        int charValueInInt;
        for (int i = sourceStartOffset; i < sourceRemaining; i++) {
            charValueInInt = source[i];

            if (charValueInInt < 0x80) {
                destination[destinationStartOffset++] = (byte) charValueInInt;
            } else if (charValueInInt < 0x800) {
                // 2 bits
                destination[destinationStartOffset++] = (byte) (0xC0 | charValueInInt >> 6);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else if (charValueInInt < 0x10000) {
                // 3 bits
                destination[destinationStartOffset++] = (byte) (0xE0 | charValueInInt >> 12);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else {
                // 4 bits
                destination[destinationStartOffset++] = (byte) (0xF0 | charValueInInt >> 18);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt >> 12 & 0x3F);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                destination[destinationStartOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            }
        }
    }

    public static CoderResult encode(final char[] source, final int srcStartOffset, final int srcLimit,
                                     final byte[] destination, final int destStartOffset, final int destLimit) {
        if (source == null || destination == null ||
                srcStartOffset < 0 || srcLimit < 0 || destStartOffset < 0 || destLimit < 0) {
            throw new IllegalArgumentException();
        }

        int sourceStartOffset = srcStartOffset, destinationStartOffset = destStartOffset;
        final int sourceLength = source.length - sourceStartOffset;
        final int sourceCount = min(sourceLength, srcLimit);
        final int destinationLength = destination.length - destStartOffset;
        final int destinationCount = min(destinationLength, destLimit);

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
        final int sourceLength = source.length - sourceStartOffset;
        final int sourceCount = min(sourceLength, srcLimit);
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
