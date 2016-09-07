package woo.ba.ben.core;


import java.lang.reflect.Field;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CoderResult;

import static java.lang.Character.highSurrogate;
import static java.lang.Character.lowSurrogate;
import static java.lang.Math.min;
import static java.nio.charset.CoderResult.OVERFLOW;
import static java.nio.charset.CoderResult.UNDERFLOW;
import static woo.ba.ben.core.IDataReader.unsignedByte;
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

    static byte[] encode(final char[] source) throws CharacterCodingException {
        final int size = encodingDestBlockSize(source, 0, source.length);
        final byte[] destination = new byte[size];
        encode(source, 0, source.length, destination, 0, destination.length);
        return destination;
    }

    public static CoderResult decode(final byte[] source, final int srcStartOffset, final int srcLimit,
                                     final char[] destination, final int destStartOffset, final int destLimit) throws CharacterCodingException {
        if (source == null || destination == null ||
                srcStartOffset < 0 || srcLimit < 0 || destStartOffset < 0 || destLimit < 0) {
            throw new IllegalArgumentException();
        }

        int sourceStartOffset = srcStartOffset, destinationStartOffset = destStartOffset;
        final int sourceCount = min(source.length - sourceStartOffset, srcLimit);
        final int destinationCount = min(destination.length - destinationStartOffset, destLimit);

        final int smallerCount = min(sourceCount, destinationCount);
        while (destinationStartOffset < smallerCount && unsignedByte(source[sourceStartOffset]) < 0x80) {
            destination[destinationStartOffset++] = (char) source[sourceStartOffset++];
        }

        short byteValueInShort;
        int charValueInInt;
        while (sourceStartOffset < sourceCount) {
            byteValueInShort = unsignedByte(source[sourceStartOffset]);

            if (byteValueInShort < 0x80) { //"01111111" == 0x7f
                //check remaining capacity
                if (destinationStartOffset + 1 > destinationCount) {
                    return OVERFLOW;
                }

                // 1 bit
                destination[destinationStartOffset++] = (char) source[sourceStartOffset++];
            } else if (byteValueInShort < 0xE0) { //"11000000" == 0xc0, "11011111" == 0xdf
                //check remaining capacity
                if (destinationStartOffset + 1 > destinationCount) {
                    return OVERFLOW;
                }

                // two bytes
                if ((sourceStartOffset + 1 >= sourceCount) || !checkTwoBytes(byteValueInShort, unsignedByte(source[sourceStartOffset + 1]))) {
                    throw new CharacterCodingException();
                }

                charValueInInt = ((byteValueInShort & 0x1F) << 6) | (source[sourceStartOffset + 1] & 0x3F);
                sourceStartOffset += 2;

                // 1 bits --> '11111 111111' is 2047
                destination[destinationStartOffset++] = (char) charValueInInt;
            } else if (byteValueInShort < 0xF0) { //"11100000" == 0xe0, "11101111" == 0xef
                //check remaining capacity
                if (destinationStartOffset + 1 > destinationCount) {
                    return OVERFLOW;
                }

                //three bytes
                if ((sourceStartOffset + 2 >= sourceCount) || !checkThreeBytes(byteValueInShort, unsignedByte(source[sourceStartOffset + 1]), unsignedByte(source[sourceStartOffset + 2]))) {
                    throw new CharacterCodingException();
                }

                charValueInInt = ((byteValueInShort & 0x0F) << 12) | ((source[sourceStartOffset + 1] & 0x3F) << 6) | (source[sourceStartOffset + 2] & 0x3F);
                sourceStartOffset += 3;

                // 1 bits --> '1111 111111 111111' is 65535
                destination[destinationStartOffset++] = (char) charValueInInt;
            } else if (byteValueInShort < 0xF8) { //"11110000" == 0xf0, "11110111" == 0xf7
                //four bytes
                if ((sourceStartOffset + 3 >= sourceCount) || !checkFourBytes(byteValueInShort, unsignedByte(source[sourceStartOffset + 1]), unsignedByte(source[sourceStartOffset + 2]), unsignedByte(source[sourceStartOffset + 3]))) {
                    throw new CharacterCodingException();
                }

                charValueInInt = ((byteValueInShort & 0x0F) << 18) | ((source[sourceStartOffset + 1] & 0x3F) << 12) | ((source[sourceStartOffset + 2] & 0x3F) << 6) | (source[sourceStartOffset + 3] & 0x3F);
                sourceStartOffset += 4;

                //check remaining capacity
                if (destinationStartOffset + 2 > destinationCount) {
                    return OVERFLOW;
                }

                // 2 bits
                destination[destinationStartOffset++] = highSurrogate(charValueInInt);
                destination[destinationStartOffset++] = lowSurrogate(charValueInInt);
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

    private static boolean checkFourBytes(short unsignedByte1, short unsignedByte2, short unsignedByte3, short unsignedByte4) {
        return unsignedByte1 > 0xF0 && validSecondaryByte(unsignedByte2) && validSecondaryByte(unsignedByte3) && validSecondaryByte(unsignedByte4);
    }

    private static boolean checkThreeBytes(final short unsignedByte1, final short unsignedByte2, final short unsignedByte3) {
        return unsignedByte1 > 0xE0 && validSecondaryByte(unsignedByte2) && validSecondaryByte(unsignedByte3);
    }

    private static boolean checkTwoBytes(final short unsignedByte1, final short unsignedByte2) {
        return unsignedByte1 > 0xC0 && validSecondaryByte(unsignedByte2);
    }

    private static boolean validSecondaryByte(final short unsignedByte2) {
        return unsignedByte2 >= 0x80 && unsignedByte2 <= 0xBF;
    }
}
