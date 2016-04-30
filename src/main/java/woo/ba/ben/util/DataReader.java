package woo.ba.ben.util;


public class DataReader {
    private static final int BYTE_MASK_INT = 0xFF;
    private static final long BYTE_MASK_LONG = 0xFFL;

    private DataReader() {
    }

    public static double readDoubleBigEndian(final byte[] buffer, final int offset) {
        return Double.longBitsToDouble(readLongBigEndian(buffer, offset));
    }

    public static double readDoubleLittleEndian(final byte[] buffer, final int offset) {
        return Double.longBitsToDouble(readLongLittleEndian(buffer, offset));
    }

    public static long readLongBigEndian(final byte[] buffer, final int offset) {
        return ((buffer[offset] & BYTE_MASK_LONG) << 56)
                | ((buffer[offset + 1] & BYTE_MASK_LONG) << 48)
                | ((buffer[offset + 2] & BYTE_MASK_LONG) << 40)
                | ((buffer[offset + 3] & BYTE_MASK_LONG) << 32)
                | ((buffer[offset + 4] & BYTE_MASK_LONG) << 24)
                | ((buffer[offset + 5] & BYTE_MASK_LONG) << 16)
                | ((buffer[offset + 6] & BYTE_MASK_LONG) << 8)
                | buffer[offset + 7] & BYTE_MASK_LONG;
    }

    public static final long readLongLittleEndian(final byte[] buffer, final int offset) {
        return ((buffer[offset + 7] & BYTE_MASK_LONG) << 56)
                | ((buffer[offset + 6] & BYTE_MASK_LONG) << 48)
                | ((buffer[offset + 5] & BYTE_MASK_LONG) << 40)
                | ((buffer[offset + 4] & BYTE_MASK_LONG) << 32)
                | ((buffer[offset + 3] & BYTE_MASK_LONG) << 24)
                | ((buffer[offset + 2] & BYTE_MASK_LONG) << 16)
                | ((buffer[offset + 1] & BYTE_MASK_LONG) << 8)
                | buffer[offset] & BYTE_MASK_LONG;
    }

    public static float readFloatBigEndian(final byte[] buffer, final int offset) {
        return Float.intBitsToFloat(readIntBigEndian(buffer, offset));
    }

    public static float readFloatLittleEndian(final byte[] buffer, final int offset) {
        return Float.intBitsToFloat(readIntLittleEndian(buffer, offset));
    }

    public static int readIntBigEndian(final byte[] buffer, final int offset) {
        return ((buffer[offset] & BYTE_MASK_INT) << 24)
                | ((buffer[offset + 1] & BYTE_MASK_INT) << 16)
                | ((buffer[offset + 2] & BYTE_MASK_INT) << 8)
                | buffer[offset + 3] & BYTE_MASK_INT;
    }

    public static int readIntLittleEndian(final byte[] buffer, final int offset) {
        return (buffer[offset] & BYTE_MASK_INT)
                | ((buffer[offset + 1] & BYTE_MASK_INT) << 8)
                | ((buffer[offset + 2] & BYTE_MASK_INT) << 16)
                | ((buffer[offset + 3] & BYTE_MASK_INT) << 24);
    }

    public static short readShortBigEndian(final byte[] buffer, final int offset) {
        return (short) (((buffer[offset] & BYTE_MASK_INT) << 8) | buffer[offset + 1] & BYTE_MASK_INT);
    }

    public static short readShortLittleEndian(final byte[] buffer, final int offset) {
        return (short) ((buffer[offset] & BYTE_MASK_INT) | ((buffer[offset + 1] & BYTE_MASK_INT) << 8));
    }

    public static char readCharBigEndian(final byte[] buffer, final int offset) {
        return (char) (((buffer[offset] & BYTE_MASK_INT) << 8) | buffer[offset + 1] & BYTE_MASK_INT);
    }

    public static char readCharLittleEndian(final byte[] buffer, final int offset) {
        return (char) ((buffer[offset] & BYTE_MASK_INT) | ((buffer[offset + 1] & BYTE_MASK_INT) << 8));
    }

    public static byte readByte(final byte[] buffer, final int offset) {
        return (byte) (buffer[offset] & BYTE_MASK_INT);
    }

    public static boolean readBoolean(final byte[] buffer, final int offset) {
        return (buffer[offset] & BYTE_MASK_INT) != 0;
    }

    public static boolean arrayEquals(final byte[] left, final byte[] right) {
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
        for (int offset = 0; offset < loopLength; offset += Long.BYTES) {
            if ((readLongBigEndian(left, offset) ^ readLongBigEndian(right, offset)) != 0) {
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
}
