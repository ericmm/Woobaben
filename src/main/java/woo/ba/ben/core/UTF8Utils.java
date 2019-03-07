package woo.ba.ben.core;


import java.lang.reflect.Field;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CoderResult;

import static java.lang.Character.MAX_SURROGATE;
import static java.lang.Character.MIN_HIGH_SURROGATE;
import static java.lang.Character.MIN_LOW_SURROGATE;
import static java.lang.Character.MIN_SUPPLEMENTARY_CODE_POINT;
import static java.lang.Character.MIN_SURROGATE;
import static java.lang.Character.isSurrogatePair;
import static java.lang.Math.min;
import static java.nio.charset.CoderResult.OVERFLOW;
import static java.nio.charset.CoderResult.UNDERFLOW;
import static java.nio.charset.CoderResult.malformedForLength;
import static java.nio.charset.CoderResult.unmappableForLength;
import static woo.ba.ben.core.IDataReader.unsignedByte;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class UTF8Utils {
    private static final int HIGH_SURROGATE_BASE = 55232;
    private static final int ENCODING_CODE_POINT_BASE = MIN_SUPPLEMENTARY_CODE_POINT - (MIN_HIGH_SURROGATE << 10) - MIN_LOW_SURROGATE;

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

        final int sourceRemaining = min(source.length - srcStartOffset, srcLimit);

        int size = 0;
        int charValueInInt;
        for (int i = srcStartOffset; i < sourceRemaining; i++) {
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

    static int decodingDestBlockSize(final byte[] source, final int srcStartOffset, final int srcLimit) throws CharacterCodingException {
        if (source == null || srcStartOffset < 0 || srcLimit < 0) {
            throw new IllegalArgumentException();
        }

        final int sourceRemaining = min(source.length - srcStartOffset, srcLimit);

        int size = 0;
        int charValueInInt;
        for (int i = srcStartOffset; i < sourceRemaining; i++) {
            charValueInInt = unsignedByte(source[i]);
            if (charValueInInt < 0xF0) {
                size++;
            } else if (charValueInInt < 0xF8) {
                size += 2;
            } else {
                throw new CharacterCodingException();
            }
        }
        return size;
    }

    static byte[] encodeExactly(final char[] source) throws CharacterCodingException {
        final int size = encodingDestBlockSize(source, 0, source.length);
        final byte[] destination = new byte[size];
        encode(source, 0, source.length, destination, 0, destination.length);
        return destination;
    }

    static byte[] encodeQuickly(final char[] source) throws CharacterCodingException {
        final byte[] destination = new byte[source.length * 3];
        if (encode(source, 0, source.length, destination, 0, destination.length) == OVERFLOW) {
            throw new CharacterCodingException();
        }
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
        byte byte2, byte3, byte4;
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
                if ((destinationStartOffset + 1 > destinationCount) || (sourceStartOffset + 1 > sourceCount)
                        || !checkTwoBytes(charValueInInt, unsignedByte(byte2 = source[sourceStartOffset++]))) {
                    return OVERFLOW;
                }
                // two bytes into one char --> '11111 111111' is 2047
                destination[destinationStartOffset++] = (char) (((charValueInInt & 0x1F) << 6) | (byte2 & 0x3F));
            } else if (charValueInInt < 0xF0) { //"11100000" == 0xe0, "11101111" == 0xef
                //check remaining capacity
                if ((destinationStartOffset + 1 > destinationCount) || (sourceStartOffset + 2 > sourceCount)
                        || !checkThreeBytes(charValueInInt, unsignedByte(byte2 = source[sourceStartOffset++]), unsignedByte(byte3 = source[sourceStartOffset++]))) {
                    return OVERFLOW;
                }
                // three bytes into one char --> '1111 111111 111111' is 65535
                destination[destinationStartOffset++] = (char) (((charValueInInt & 0x0F) << 12) | ((byte2 & 0x3F) << 6) | (byte3 & 0x3F));
            } else if (charValueInInt < 0xF8) { //"11110000" == 0xf0, "11110111" == 0xf7
                //check remaining capacity
                if ((destinationStartOffset + 2 > destinationCount) || (sourceStartOffset + 3 > sourceCount)
                        || !checkFourBytes(charValueInInt, unsignedByte(byte2 = source[sourceStartOffset++]), unsignedByte(byte3 = source[sourceStartOffset++]), unsignedByte(byte4 = source[sourceStartOffset++]))) {
                    return OVERFLOW;
                }

                charValueInInt = ((charValueInInt & 0x0F) << 18) | ((byte2 & 0x3F) << 12) | ((byte3 & 0x3F) << 6) | (byte4 & 0x3F);

                // four bytes into two chars
                destination[destinationStartOffset++] = (char) ((charValueInInt >>> 10) + HIGH_SURROGATE_BASE);
                destination[destinationStartOffset++] = (char) ((charValueInInt & 0x3ff) + MIN_LOW_SURROGATE);
            } else {
                return OVERFLOW;
            }
        }
        return UNDERFLOW;
    }

    public static CoderResult decode2(final byte[] source, final int srcStartOffset, final int srcLimit,
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
        byte byte2, byte3, byte4;
        while (sourceStartOffset < sourceCount) {
            charValueInInt = source[sourceStartOffset++];

            if (charValueInInt >= 0) { //"01111111" == 0x7f
                //check remaining capacity
                if (destinationStartOffset + 1 > destinationCount) {
                    return OVERFLOW;
                }

                // 1 bit
                destination[destinationStartOffset++] = (char) charValueInInt;
            } else if (charValueInInt > -9) { //invalid, "11xxxxxx"
                return OVERFLOW;
            } else if (charValueInInt > -17) { //"11110000" ==> 4 bytes
                //check remaining capacity
                if ((destinationStartOffset + 2 > destinationCount) || (sourceStartOffset + 3 > sourceCount)
                        || !checkFourBytes2(charValueInInt, byte2 = source[sourceStartOffset++], byte3 = source[sourceStartOffset++], byte4 = source[sourceStartOffset++])) {
                    return OVERFLOW;
                }

                charValueInInt = ((charValueInInt & 0x0F) << 18) | ((byte2 & 0x3F) << 12) | ((byte3 & 0x3F) << 6) | (byte4 & 0x3F);
                // four bytes into two chars
                destination[destinationStartOffset++] = (char) ((charValueInInt >>> 10) + HIGH_SURROGATE_BASE);
                destination[destinationStartOffset++] = (char) ((charValueInInt & 0x3ff) + MIN_LOW_SURROGATE);

            } else if (charValueInInt > -33) { //"1110xxxx" ==> 3 bytes
                //check remaining capacity
                if ((destinationStartOffset + 1 > destinationCount) || (sourceStartOffset + 2 > sourceCount)
                        || !checkThreeBytes2(charValueInInt, byte2 = source[sourceStartOffset++], byte3 = source[sourceStartOffset++])) {
                    return OVERFLOW;
                }

                // three bytes into one char --> '1111 111111 111111' is 65535
                destination[destinationStartOffset++] = (char) (((charValueInInt & 0x0F) << 12) | ((byte2 & 0x3F) << 6) | (byte3 & 0x3F));
            } else if (charValueInInt > -65) { //"110xxxxx" ==> 2 bytes
                //check remaining capacity
                if ((destinationStartOffset + 1 > destinationCount) || (sourceStartOffset + 1 > sourceCount)
                        || !checkTwoBytes2(charValueInInt, byte2 = source[sourceStartOffset++])) {
                    return OVERFLOW;
                }
                // two bytes into one char --> '11111 111111' is 2047
                destination[destinationStartOffset++] = (char) (((charValueInInt & 0x1F) << 6) | (byte2 & 0x3F));
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
            throw new IllegalArgumentException("Byte array not large enough");
        }

        final int smallerCount = min(sourceCount, destinationCount);
        while (destinationStartOffset < smallerCount && source[sourceStartOffset] < 0x80) {
            destination[destinationStartOffset++] = (byte) source[sourceStartOffset++];
        }

        int codePoint;
        char highSurrogate, lowSurrogate;
        for (int i = sourceStartOffset; i < sourceCount; i++) {
            codePoint = source[i];
            if (codePoint < 0x80) {
                //check remaining capacity
                if (destinationStartOffset + 1 > destinationCount) {
                    return OVERFLOW;
                }

                // 1 bit
                destination[destinationStartOffset++] = (byte) codePoint;
            } else if (codePoint < 0x800) {
                //check remaining capacity
                if (destinationStartOffset + 2 > destinationCount) {
                    return OVERFLOW;
                }

                // 2 bits
                destination[destinationStartOffset++] = (byte) (0xC0 | codePoint >> 6);
                destination[destinationStartOffset++] = (byte) (0x80 | codePoint & 0x3F);
            } else if (codePoint < MIN_SURROGATE || (codePoint > MAX_SURROGATE && codePoint < 0x10000)) {
                //check remaining capacity
                if (destinationStartOffset + 3 > destinationCount) {
                    return OVERFLOW;
                }

                // 3 bits
                destination[destinationStartOffset++] = (byte) (0xE0 | codePoint >> 12);
                destination[destinationStartOffset++] = (byte) (0x80 | codePoint >> 6 & 0x3F);
                destination[destinationStartOffset++] = (byte) (0x80 | codePoint & 0x3F);
            } else if (codePoint >= MIN_SURROGATE && codePoint <= MAX_SURROGATE) {
                if (i + 1 >= sourceCount) {
                    return unmappableForLength(i + 1);
                }

                // two chars for one code point
                highSurrogate = source[i++];
                lowSurrogate = source[i];
                if (!isSurrogatePair(highSurrogate, lowSurrogate)) {
                    return malformedForLength(i);
                }
                codePoint = (highSurrogate << 10) + lowSurrogate + ENCODING_CODE_POINT_BASE;
                if (codePoint < 0x10000) {
                    //check remaining capacity
                    if (destinationStartOffset + 3 > destinationCount) {
                        return OVERFLOW;
                    }

                    // 3 bits
                    destination[destinationStartOffset++] = (byte) (0xE0 | codePoint >> 12);
                    destination[destinationStartOffset++] = (byte) (0x80 | codePoint >> 6 & 0x3F);
                    destination[destinationStartOffset++] = (byte) (0x80 | codePoint & 0x3F);
                } else {
                    //check remaining capacity
                    if (destinationStartOffset + 4 > destinationCount) {
                        return OVERFLOW;
                    }

                    // 4 bits
                    destination[destinationStartOffset++] = (byte) (0xF0 | codePoint >> 18);
                    destination[destinationStartOffset++] = (byte) (0x80 | codePoint >> 12 & 0x3F);
                    destination[destinationStartOffset++] = (byte) (0x80 | codePoint >> 6 & 0x3F);
                    destination[destinationStartOffset++] = (byte) (0x80 | codePoint & 0x3F);
                }
            } else if (codePoint < 0x110000) {
                //check remaining capacity
                if (destinationStartOffset + 4 > destinationCount) {
                    return OVERFLOW;
                }

                // 4 bits
                destination[destinationStartOffset++] = (byte) (0xF0 | codePoint >> 18);
                destination[destinationStartOffset++] = (byte) (0x80 | codePoint >> 12 & 0x3F);
                destination[destinationStartOffset++] = (byte) (0x80 | codePoint >> 6 & 0x3F);
                destination[destinationStartOffset++] = (byte) (0x80 | codePoint & 0x3F);
            } else {
                return unmappableForLength(i);
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
            throw new IllegalArgumentException("Byte array not large enough");
        }

        final int smallerCount = min(sourceCount, destinationCount);
        while (destinationStartOffset < smallerCount && source[sourceStartOffset] < 0x80) {
            putByte(destAddress, destinationStartOffset++, (byte) source[sourceStartOffset++]);
        }

        int codePoint;
        char highSurrogate, lowSurrogate;
        for (int i = sourceStartOffset; i < sourceCount; i++) {
            codePoint = source[i];
            if (codePoint < 0x80) {
                //check remaining capacity
                if (destinationStartOffset + 1 > destinationCount) {
                    return OVERFLOW;
                }

                // 1 bit
                putByte(destAddress, destinationStartOffset++, (byte) codePoint);
            } else if (codePoint < 0x800) {
                //check remaining capacity
                if (destinationStartOffset + 2 > destinationCount) {
                    return OVERFLOW;
                }

                // 2 bits
                putByte(destAddress, destinationStartOffset++, (byte) (0xC0 | codePoint >> 6));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | codePoint & 0x3F));
            } else if (codePoint < MIN_SURROGATE || (codePoint > MAX_SURROGATE && codePoint < 0x10000)) {
                //check remaining capacity
                if (destinationStartOffset + 3 > destinationCount) {
                    return OVERFLOW;
                }

                // 3 bits
                putByte(destAddress, destinationStartOffset++, (byte) (0xE0 | codePoint >> 12));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | codePoint >> 6 & 0x3F));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | codePoint & 0x3F));
            } else if (codePoint >= MIN_SURROGATE && codePoint <= MAX_SURROGATE) {
                if (i + 1 >= sourceCount) {
                    return unmappableForLength(i + 1);
                }

                // two chars for one code point
                highSurrogate = source[i++];
                lowSurrogate = source[i];
                if (!isSurrogatePair(highSurrogate, lowSurrogate)) {
                    return malformedForLength(i);
                }
                codePoint = (highSurrogate << 10) + lowSurrogate + ENCODING_CODE_POINT_BASE;
                if (codePoint < 0x10000) {
                    //check remaining capacity
                    if (destinationStartOffset + 3 > destinationCount) {
                        return OVERFLOW;
                    }

                    // 3 bits
                    putByte(destAddress, destinationStartOffset++, (byte) (0xE0 | codePoint >> 12));
                    putByte(destAddress, destinationStartOffset++, (byte) (0x80 | codePoint >> 6 & 0x3F));
                    putByte(destAddress, destinationStartOffset++, (byte) (0x80 | codePoint & 0x3F));
                } else {
                    //check remaining capacity
                    if (destinationStartOffset + 4 > destinationCount) {
                        return OVERFLOW;
                    }

                    // 4 bits
                    putByte(destAddress, destinationStartOffset++, (byte) (0xF0 | codePoint >> 18));
                    putByte(destAddress, destinationStartOffset++, (byte) (0x80 | codePoint >> 12 & 0x3F));
                    putByte(destAddress, destinationStartOffset++, (byte) (0x80 | codePoint >> 6 & 0x3F));
                    putByte(destAddress, destinationStartOffset++, (byte) (0x80 | codePoint & 0x3F));
                }
            } else if (codePoint < 0x110000) {
                //check remaining capacity
                if (destinationStartOffset + 4 > destinationCount) {
                    return OVERFLOW;
                }

                // 4 bits
                putByte(destAddress, destinationStartOffset++, (byte) (0xF0 | codePoint >> 18));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | codePoint >> 12 & 0x3F));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | codePoint >> 6 & 0x3F));
                putByte(destAddress, destinationStartOffset++, (byte) (0x80 | codePoint & 0x3F));
            } else {
                return unmappableForLength(i);
            }
        }
        return UNDERFLOW;
    }

    private static void putByte(final long address, final int position, final byte value) {
        UNSAFE.putByte(address + position, value);
    }

    private static boolean checkFourBytes(final int unsignedByte1, short unsignedByte2, short unsignedByte3, short unsignedByte4) {
        return unsignedByte1 > 0xF0 && validSecondaryByte(unsignedByte2) && validSecondaryByte(unsignedByte3) && validSecondaryByte(unsignedByte4);
    }

    private static boolean checkThreeBytes(final int unsignedByte1, final short unsignedByte2, final short unsignedByte3) {
        return unsignedByte1 > 0xE0 && validSecondaryByte(unsignedByte2) && validSecondaryByte(unsignedByte3);
    }

    private static boolean checkTwoBytes(final int unsignedByte1, final short unsignedByte2) {
        return unsignedByte1 > 0xC0 && validSecondaryByte(unsignedByte2);
    }

    private static boolean validSecondaryByte(final short unsignedByte2) {
        return unsignedByte2 >= 0x80 && unsignedByte2 <= 0xBF;
    }

    private static boolean checkFourBytes2(final int byte1, short byte2, short byte3, short byte4) {
        return byte1 < -8 && validSecondaryByte2(byte2) && validSecondaryByte2(byte3) && validSecondaryByte2(byte4);
    }

    private static boolean checkThreeBytes2(final int byte1, final short byte2, final short byte3) {
        return byte1 < -16 && validSecondaryByte2(byte2) && validSecondaryByte2(byte3);
    }

    private static boolean checkTwoBytes2(final int byte1, final short byte2) {
        return byte1 < -32 && validSecondaryByte2(byte2);
    }

    private static boolean validSecondaryByte2(final short byte2) {
        //[10000000 -- 10111111]
        return byte2 < -64;
    }

//  http://www.daemonology.net/blog/2008-06-05-faster-utf8-strlen.html
//  http://www.daemonology.net/blog/strlentest.c

//    public void countUtf8Character(final byte[] s) {
//
//        while (s[i] > 0) {
//            ascii:
//            {
//                i++;
//            }
//        }
//
//        count += i - iBefore;
//
//        while (s[i]) {
//            if (s[i] > 0) {
//                iBefore = i;
//                break ascii;
//            } else {
//                switch (0xF0 & s[i]) {
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
//
//            count++;
//        }
//    }
}
