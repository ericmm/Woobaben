package woo.ba.ben.util;


public class DataReader {
    private static final int BYTE_MASK_INT = 0xFF;
    private static final long BYTE_MASK_LONG = 0xFFL;

    private DataReader() {
    }

    public static double readDouble(final byte[] buffer, final int offset) {
        return Double.longBitsToDouble(readLong(buffer, offset));
    }

    public static long readLong(final byte[] buffer, final int offset) {
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

    public static float readFloat(final byte[] buffer, final int offset) {
        return Float.intBitsToFloat(readInt(buffer, offset));
    }

    public static int readInt(final byte[] buffer, final int offset) {
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

    public static short readShort(final byte[] buffer, final int offset) {
        return (short) (((buffer[offset] & BYTE_MASK_INT) << 8) | buffer[offset + 1] & BYTE_MASK_INT);
    }

    public static short readShortLittleEndian(final byte[] buffer, final int offset) {
        return (short) ((buffer[offset] & BYTE_MASK_INT) | ((buffer[offset + 1] & BYTE_MASK_INT) << 8));
    }

    public static char readChar(final byte[] buffer, final int offset) {
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
}
