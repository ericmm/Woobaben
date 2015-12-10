package woo.ba.ben.bean;


public class DataReader {
    private static final int BYTE_MASK_INT = 0xFF;
    private static final long BYTE_MASK_LONG = 0xFFL;

    private DataReader() {
    }

    public static double readDouble(byte[] buffer, int offset) {
        return Double.longBitsToDouble(readLong(buffer, offset));
    }

    public static long readLong(byte[] buffer, int offset) {
        return (maskLong(buffer[offset]) << 56) |
                (maskLong(buffer[offset + 1]) << 48) |
                (maskLong(buffer[offset + 2]) << 40) |
                (maskLong(buffer[offset + 3]) << 32) |
                (maskLong(buffer[offset + 4]) << 24) |
                (maskLong(buffer[offset + 5]) << 16) |
                (maskLong(buffer[offset + 6]) << 8) |
                maskLong(buffer[offset + 7]);
    }

    public static float readFloat(final byte[] buffer, final int offset) {
        return Float.intBitsToFloat(readInt(buffer, offset));
    }

    public static int readInt(final byte[] buffer, final int offset) {
        return (maskInt(buffer[offset]) << 24) |
                (maskInt(buffer[offset + 1]) << 16) |
                (maskInt(buffer[offset + 2]) << 8) |
                maskInt(buffer[offset + 3]);
    }

    public static short readShort(final byte[] buffer, final int offset) {
        return (short) ((maskInt(buffer[offset]) << 8) | maskInt(buffer[offset + 1]));
    }

    public static char readChar(final byte[] buffer, final int offset) {
        return (char) ((maskInt(buffer[offset]) << 8) | maskInt(buffer[offset + 1]));
    }

    public static byte readByte(final byte[] buffer, final int offset) {
        return (byte) maskInt(buffer[offset]);
    }

    public static boolean readBoolean(final byte[] buffer, final int offset) {
        return maskInt(buffer[offset]) != 0;
    }

    private static int maskInt(final byte byteValue) {
        return byteValue & BYTE_MASK_INT;
    }

    private static long maskLong(final byte byteValue) {
        return byteValue & BYTE_MASK_LONG;
    }
}
