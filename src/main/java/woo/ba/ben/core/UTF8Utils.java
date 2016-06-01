package woo.ba.ben.core;


import java.nio.charset.CoderResult;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public class UTF8Utils {

    public static CoderResult encode(final char[] source, final int offset, final int count, final byte[] dest, final int destOffset) {
        if (source == null || dest == null || offset < 0 || count < 0 || destOffset < 0) {
            throw new IllegalArgumentException("Illegal arguments");
        }

        int sourceOffset = offset, destinationOffset = destOffset;
        final int sourceLength = source.length - sourceOffset;
        int sourceRemaining = count > sourceLength ? sourceLength : count;
        int remainingCapacity = dest.length - destinationOffset;
        if (remainingCapacity < sourceRemaining) {
            return CoderResult.OVERFLOW;
        }

        // handle ascii encoded strings in an optimised loop
        final int smallerLength = Math.min(sourceRemaining, remainingCapacity);
        while (destinationOffset < smallerLength && source[sourceOffset] < 0x80) {
            dest[destinationOffset++] = (byte) source[sourceOffset++];
        }

        int charValueInInt;
        for (int i = sourceOffset; i < sourceRemaining; i++) {
            charValueInInt = source[i];

            if (charValueInInt < 0x80) {
                //check remaining capacity
                remainingCapacity--;
                sourceRemaining--;
                if (remainingCapacity < sourceRemaining) {
                    return CoderResult.OVERFLOW;
                }

                // 1 bit
                dest[destinationOffset++] = (byte) charValueInInt;
            } else if (charValueInInt < 0x800) {
                //check remaining capacity
                remainingCapacity -= 2;
                sourceRemaining--;
                if (remainingCapacity < sourceRemaining) {
                    return CoderResult.OVERFLOW;
                }

                // 2 bits
                dest[destinationOffset++] = (byte) (0xC0 | charValueInInt >> 6);
                dest[destinationOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else if (charValueInInt < 0x10000) {
                //check remaining capacity
                remainingCapacity -= 3;
                sourceRemaining--;
                if (remainingCapacity < sourceRemaining) {
                    return CoderResult.OVERFLOW;
                }

                // 3 bits
                dest[destinationOffset++] = (byte) (0xE0 | charValueInInt >> 12);
                dest[destinationOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                dest[destinationOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else if (charValueInInt < 0x200000) {
                //check remaining capacity
                remainingCapacity -= 4;
                sourceRemaining--;
                if (remainingCapacity < sourceRemaining) {
                    return CoderResult.OVERFLOW;
                }

                // 4 bits
                dest[destinationOffset++] = (byte) (0xF0 | charValueInInt >> 18);
                dest[destinationOffset++] = (byte) (0x80 | charValueInInt >> 12 & 0x3F);
                dest[destinationOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                dest[destinationOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else {
                // error, clean-up byte array;
                return CoderResult.OVERFLOW;
            }
        }
        return CoderResult.UNDERFLOW;
    }

    // no boundaries checking
    private static CoderResult encodeInternal(final char[] source, final int offset, final int count, final byte[] dest, final int destOffset) {
        int sourceOffset = offset, destinationOffset = destOffset;
        final int sourceLength = source.length - sourceOffset;
        int sourceRemaining = count > sourceLength ? sourceLength : count;
        int remainingCapacity = dest.length - destinationOffset;
        if (remainingCapacity < sourceRemaining) {
            return CoderResult.OVERFLOW;
        }

        // handle ascii encoded strings in an optimised loop
        final int smallerLength = Math.min(sourceRemaining, remainingCapacity);
        while (destinationOffset < smallerLength && source[sourceOffset] < 0x80) {
            dest[destinationOffset++] = (byte) source[sourceOffset++];
        }

        int charValueInInt;
        for (int i = sourceOffset; i < sourceRemaining; i++) {
            charValueInInt = source[i];

            if (charValueInInt < 0x80) {
                // 1 bit
                dest[destinationOffset++] = (byte) charValueInInt;
            } else if (charValueInInt < 0x800) {
                // 2 bits
                dest[destinationOffset++] = (byte) (0xC0 | charValueInInt >> 6);
                dest[destinationOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else if (charValueInInt < 0x10000) {
                // 3 bits
                dest[destinationOffset++] = (byte) (0xE0 | charValueInInt >> 12);
                dest[destinationOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                dest[destinationOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else if (charValueInInt < 0x200000) {
                // 4 bits
                dest[destinationOffset++] = (byte) (0xF0 | charValueInInt >> 18);
                dest[destinationOffset++] = (byte) (0x80 | charValueInInt >> 12 & 0x3F);
                dest[destinationOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                dest[destinationOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else {
                // error, clean-up byte array;
                return CoderResult.OVERFLOW;
            }
        }
        return CoderResult.UNDERFLOW;
    }

    public static byte[] encode(final char[] source, final int offset, final int count) {
        if (source == null || offset < 0 || count < 0) {
            throw new IllegalArgumentException("Illegal argument");
        }

        int targetCapacity = 0;
        int charValueInInt;
        for (int i = offset; i < count; i++) {
            charValueInInt = source[i];
            if (charValueInInt < 0x80) {
                targetCapacity++;
            } else if (charValueInInt < 0x800) {
                targetCapacity += 2;
            } else if (charValueInInt < 0x10000) {
                targetCapacity += 3;
            } else if (charValueInInt < 0x200000) {
                targetCapacity += 4;
            } else {
                throw new IllegalArgumentException("Containing unrecognised character:" + source[i]);
            }
        }

        byte[] dest = new byte[targetCapacity];
        int destOffset = 0;
        for (int i = offset; i < count; i++) {
            charValueInInt = source[i];

            if (charValueInInt < 0x80) {
                // 1 bit
                dest[destOffset++] = (byte) charValueInInt;
            } else if (charValueInInt < 0x800) {
                // 2 bits
                dest[destOffset++] = (byte) (0xC0 | charValueInInt >> 6);
                dest[destOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else if (charValueInInt < 0x10000) {
                // 3 bits
                dest[destOffset++] = (byte) (0xE0 | charValueInInt >> 12);
                dest[destOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                dest[destOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else {
                // 4 bits
                dest[destOffset++] = (byte) (0xF0 | charValueInInt >> 18);
                dest[destOffset++] = (byte) (0x80 | charValueInInt >> 12 & 0x3F);
                dest[destOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                dest[destOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            }
        }
        return dest;
    }

    public static CoderResult encode(final char[] source, final int offset, final int count, final long destOffset, final long destLimit) {
        if (source == null || offset < 0 || count < 0 || destOffset < 0 || destLimit < 0) {
            throw new IllegalArgumentException("Illegal arguments");
        }

        int sourceOffset = offset;
        long destinationOffset = destOffset;
        final int sourceLength = source.length - sourceOffset;
        int sourceRemaining = count > sourceLength ? sourceLength : count;
        int remainingCapacity = (int) (destLimit - destinationOffset);
        if (remainingCapacity < sourceRemaining) {
            return CoderResult.OVERFLOW;
        }

        // handle ascii encoded strings in an optimised loop
        final int smallerLength = Math.min(sourceRemaining, remainingCapacity);
        while (destinationOffset < smallerLength && source[sourceOffset] < 0x80) {
            UNSAFE.putByte(destinationOffset++, (byte) source[sourceOffset++]);
        }

        int charValueInInt;
        for (int i = sourceOffset; i < sourceRemaining; i++) {
            charValueInInt = source[i];

            if (charValueInInt < 0x80) {
                //check remaining capacity
                remainingCapacity--;
                sourceRemaining--;
                if (remainingCapacity < sourceRemaining) {
                    return CoderResult.OVERFLOW;
                }

                // 1 bit
                UNSAFE.putByte(destinationOffset++, (byte) charValueInInt);
            } else if (charValueInInt < 0x800) {
                //check remaining capacity
                remainingCapacity -= 2;
                sourceRemaining--;
                if (remainingCapacity < sourceRemaining) {
                    return CoderResult.OVERFLOW;
                }

                // 2 bits
                UNSAFE.putByte(destinationOffset++, (byte) (0xC0 | charValueInInt >> 6));
                UNSAFE.putByte(destinationOffset++, (byte) (0x80 | charValueInInt & 0x3F));
            } else if (charValueInInt < 0x10000) {
                //check remaining capacity
                remainingCapacity -= 3;
                sourceRemaining--;
                if (remainingCapacity < sourceRemaining) {
                    return CoderResult.OVERFLOW;
                }

                // 3 bits
                UNSAFE.putByte(destinationOffset++, (byte) (0xE0 | charValueInInt >> 12));
                UNSAFE.putByte(destinationOffset++, (byte) (0x80 | charValueInInt >> 6 & 0x3F));
                UNSAFE.putByte(destinationOffset++, (byte) (0x80 | charValueInInt & 0x3F));
            } else if (charValueInInt < 0x200000) {
                //check remaining capacity
                remainingCapacity -= 4;
                sourceRemaining--;
                if (remainingCapacity < sourceRemaining) {
                    return CoderResult.OVERFLOW;
                }

                // 4 bits
                UNSAFE.putByte(destinationOffset++, (byte) (0xF0 | charValueInInt >> 18));
                UNSAFE.putByte(destinationOffset++, (byte) (0x80 | charValueInInt >> 12 & 0x3F));
                UNSAFE.putByte(destinationOffset++, (byte) (0x80 | charValueInInt >> 6 & 0x3F));
                UNSAFE.putByte(destinationOffset++, (byte) (0x80 | charValueInInt & 0x3F));
            } else {
                // error, clean-up byte array;
                return CoderResult.OVERFLOW;
            }
        }
        return CoderResult.UNDERFLOW;
    }

    // no boundaries checking
    private static CoderResult encodeInternal(final char[] source, final int offset, final int count, final long destOffset, final long destLimit) {
        int sourceOffset = offset;
        long destinationOffset = destOffset;
        final int sourceLength = source.length - sourceOffset;
        int sourceRemaining = count > sourceLength ? sourceLength : count;
        int remainingCapacity = (int) (destLimit - destinationOffset);
        if (remainingCapacity < sourceRemaining) {
            return CoderResult.OVERFLOW;
        }

        // handle ascii encoded strings in an optimised loop
        final int smallerLength = Math.min(sourceRemaining, remainingCapacity);
        while (destinationOffset < smallerLength && source[sourceOffset] < 0x80) {
            UNSAFE.putByte(destinationOffset++, (byte) source[sourceOffset++]);
        }

        int charValueInInt;
        for (int i = sourceOffset; i < sourceRemaining; i++) {
            charValueInInt = source[i];

            if (charValueInInt < 0x80) {
                // 1 bit
                UNSAFE.putByte(destinationOffset++, (byte) charValueInInt);
            } else if (charValueInInt < 0x800) {
                // 2 bits
                UNSAFE.putByte(destinationOffset++, (byte) (0xC0 | charValueInInt >> 6));
                UNSAFE.putByte(destinationOffset++, (byte) (0x80 | charValueInInt & 0x3F));
            } else if (charValueInInt < 0x10000) {
                // 3 bits
                UNSAFE.putByte(destinationOffset++, (byte) (0xE0 | charValueInInt >> 12));
                UNSAFE.putByte(destinationOffset++, (byte) (0x80 | charValueInInt >> 6 & 0x3F));
                UNSAFE.putByte(destinationOffset++, (byte) (0x80 | charValueInInt & 0x3F));
            } else if (charValueInInt < 0x200000) {
                // 4 bits
                UNSAFE.putByte(destinationOffset++, (byte) (0xF0 | charValueInInt >> 18));
                UNSAFE.putByte(destinationOffset++, (byte) (0x80 | charValueInInt >> 12 & 0x3F));
                UNSAFE.putByte(destinationOffset++, (byte) (0x80 | charValueInInt >> 6 & 0x3F));
                UNSAFE.putByte(destinationOffset++, (byte) (0x80 | charValueInInt & 0x3F));
            } else {
                // error, clean-up byte array;
                return CoderResult.OVERFLOW;
            }
        }
        return CoderResult.UNDERFLOW;
    }


//    public CoderResult encodeStringToHeap(final String src, final ByteBuffer dst) {
//        int lastDp = 0;
//        int arrayOffset = dst.arrayOffset();
//        int dp = arrayOffset + dst.position();
//        int dl = arrayOffset + dst.limit();
//
//        int spCurr = UnsafeString.getOffset(src);
//        int sl = src.length();
//
//        try {
//            CoderResult result = encode(UnsafeString.getChars(src), spCurr, sl,
//                    dst.array(), dp, dl);
//            dst.position(lastDp - arrayOffset);
//            return result;
//        } catch (ArrayIndexOutOfBoundsException e) {
//            return CoderResult.OVERFLOW;
//        }
//    }
//
//    public final CoderResult encodeString(String src, ByteBuffer dst) {
//        if (dst.hasArray())
//            return encodeStringToHeap(src, dst);
//        else
//            return encodeStringToDirect(src, dst);
//    }
//
//    public final CoderResult encodeStringToDirect(String src, ByteBuffer dst) {
//        lastDp = 0;
//        int dp = dst.position();
//        int dl = dst.limit();
//
//        // in JDK7 offset is always 0, but earlier versions accomodated
//        // substrings
//        // pointing back to original array and having a separate offset and
//        // length.
//        int spCurr = UnsafeString.getOffset(src);
//        int sl = src.length();
//
//        // pluck the chars array out of the String, saving us an array copy
//        long address = UnsafeDirectByteBuffer.getAddress(dst);
//        CoderResult result = encode(UnsafeString.getChars(src), spCurr, sl,
//                address + dp, address + dl);
//        // only move the position if we fit the whole thing in.
//        if (lastDp != 0)
//            dst.position((int) (lastDp - address));
//        return result;
//
//    }
//
//    public CoderResult encodeStringToHeap(String src, ByteBuffer dst) {
//        int lastDp = 0;
//        int arrayOffset = dst.arrayOffset();
//        int dp = arrayOffset + dst.position();
//        int dl = arrayOffset + dst.limit();
//
//        int spCurr = UnsafeString.getOffset(src);
//        int sl = src.length();
//
//        try {
//            CoderResult result = encode(UnsafeString.getChars(src), spCurr, sl,
//                    dst.array(), dp, dl);
//            dst.position(lastDp - arrayOffset);
//            return result;
//        } catch (ArrayIndexOutOfBoundsException e) {
//            return CoderResult.OVERFLOW;
//        }
//
//    }
}
