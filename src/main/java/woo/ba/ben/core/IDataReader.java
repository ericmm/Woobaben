package woo.ba.ben.core;


import static java.lang.Double.doubleToRawLongBits;
import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.floatToRawIntBits;
import static java.lang.Float.intBitsToFloat;

public interface IDataReader {
    int BYTE_MASK_INT = 0xFF;
    long BYTE_MASK_LONG = 0xFFL;

    static short swap(final short x) {
        return Short.reverseBytes(x);
    }

    static char swap(final char x) {
        return Character.reverseBytes(x);
    }

    static int swap(final int x) {
        return Integer.reverseBytes(x);
    }

    static long swap(final long x) {
        return Long.reverseBytes(x);
    }

    static short unsignedByte(final byte num) {
        return (short) (num & BYTE_MASK_INT);
    }

    static int unsignedShort(final short num) {
        return num & 0xFFFF;
    }

    static long unsignedInt(final int num) {
        return num & 0xFFFFFFFFL;
    }

    static boolean readBoolean(final byte[] buffer, final int startOffset) {
        return buffer[startOffset] != 0;
    }

    static char makeChar(final byte b1, final byte b0) {
        return (char) ((b1 << 8) | (b0 & BYTE_MASK_INT));
    }

    static short makeShort(final byte b1, final byte b0) {
        return (short) ((b1 << 8) | (b0 & BYTE_MASK_INT));
    }

    static int makeInt(final byte b3, final byte b2, final byte b1, final byte b0) {
        return ((b3) << 24) |
                ((b2 & BYTE_MASK_INT) << 16) |
                ((b1 & BYTE_MASK_INT) << 8) |
                ((b0 & BYTE_MASK_INT));
    }

    static long makeLong(final byte b7, final byte b6, final byte b5, final byte b4,
                         final byte b3, final byte b2, final byte b1, final byte b0) {
        return (((long) b7) << 56) |
                ((b6 & BYTE_MASK_LONG) << 48) |
                ((b5 & BYTE_MASK_LONG) << 40) |
                ((b4 & BYTE_MASK_LONG) << 32) |
                ((b3 & BYTE_MASK_LONG) << 24) |
                ((b2 & BYTE_MASK_LONG) << 16) |
                ((b1 & BYTE_MASK_LONG) << 8) |
                ((b0 & BYTE_MASK_LONG));
    }

    static byte char1(final char x) {
        return (byte) (x >> 8);
    }

    static byte char0(final char x) {
        return (byte) (x);
    }

    static byte short1(final short x) {
        return (byte) (x >> 8);
    }

    static byte short0(final short x) {
        return (byte) (x);
    }

    static byte int3(final int x) {
        return (byte) (x >> 24);
    }

    static byte int2(final int x) {
        return (byte) (x >> 16);
    }

    static byte int1(final int x) {
        return (byte) (x >> 8);
    }

    static byte int0(final int x) {
        return (byte) (x);
    }

    static byte long7(final long x) {
        return (byte) (x >> 56);
    }

    static byte long6(final long x) {
        return (byte) (x >> 48);
    }

    static byte long5(final long x) {
        return (byte) (x >> 40);
    }

    static byte long4(final long x) {
        return (byte) (x >> 32);
    }

    static byte long3(final long x) {
        return (byte) (x >> 24);
    }

    static byte long2(final long x) {
        return (byte) (x >> 16);
    }

    static byte long1(final long x) {
        return (byte) (x >> 8);
    }

    static byte long0(final long x) {
        return (byte) (x);
    }

    default int unsignedShort(final byte[] buffer, final int startOffset) {
        return unsignedShort(readShort(buffer, startOffset));
    }

    default long unsignedInt(final byte[] buffer, final int startOffset) {
        return unsignedInt(readInt(buffer, startOffset));
    }

    default double readDouble(final byte[] buffer, final int startOffset) {
        return longBitsToDouble(readLong(buffer, startOffset));
    }

    long readLong(final byte[] buffer, final int startOffset);

    default float readFloat(final byte[] buffer, final int startOffset) {
        return intBitsToFloat(readInt(buffer, startOffset));
    }

    int readInt(final byte[] buffer, final int startOffset);

    short readShort(final byte[] buffer, final int startOffset);

    char readChar(final byte[] buffer, final int startOffset);

    default boolean arrayEquals(final byte[] left, final byte[] right) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }

        if (right.length != left.length) {
            return false;
        }

        final int loopTimes = left.length / Long.BYTES;
        final int loopLength = loopTimes * Long.BYTES;
        for (int startOffset = 0; startOffset < loopLength; startOffset += Long.BYTES) {
            if ((readLong(left, startOffset) ^ readLong(right, startOffset)) != 0) {
                return false;
            }
        }

        // for remaining
        for (int i = loopLength; i < left.length; i++) {
            if (left[i] != right[i]) {
                return false;
            }
        }

        return true;
    }

    void writeChar(final byte[] buffer, final int startOffset, final char num);

    void writeShort(final byte[] buffer, final int startOffset, final short num);

    void writeInt(final byte[] buffer, final int startOffset, final int num);

    default void writeFloat(final byte[] buffer, final int startOffset, final float num) {
        writeInt(buffer, startOffset, floatToRawIntBits(num));
    }

    void writeLong(final byte[] buffer, final int startOffset, final long num);

    default void writeDouble(final byte[] buffer, final int startOffset, final double num) {
        writeLong(buffer, startOffset, doubleToRawLongBits(num));
    }
}
