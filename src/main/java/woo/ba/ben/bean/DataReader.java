package woo.ba.ben.bean;


public class DataReader {
    private static final int BYTE_MASK_INT = 0xFF;
    private static final long BYTE_MASK_LONG = 0xFFL;

    private DataReader() {
    }

    public static double readDouble(final byte[] buffer, final int offset) {
        return Double.longBitsToDouble(readLong(buffer, offset));
    }

    public static long readLong(final byte[] buffer, final int offset) {
        return (normalizeLong(buffer[offset]) << 56)
                | (normalizeLong(buffer[offset + 1]) << 48)
                | (normalizeLong(buffer[offset + 2]) << 40)
                | (normalizeLong(buffer[offset + 3]) << 32)
                | (normalizeLong(buffer[offset + 4]) << 24)
                | (normalizeLong(buffer[offset + 5]) << 16)
                | (normalizeLong(buffer[offset + 6]) << 8)
                | normalizeLong(buffer[offset + 7]);
    }

    public static final long readLongLittleEndian(final byte[] buffer, final int offset) {
        return (normalizeLong(buffer[offset + 7]) << 56)
                | (normalizeLong(buffer[offset + 6]) << 48)
                | (normalizeLong(buffer[offset + 5]) << 40)
                | (normalizeLong(buffer[offset + 4]) << 32)
                | (normalizeLong(buffer[offset + 3]) << 24)
                | (normalizeLong(buffer[offset + 2]) << 16)
                | (normalizeLong(buffer[offset + 1]) << 8)
                | normalizeLong(buffer[offset]);
    }

    public static float readFloat(final byte[] buffer, final int offset) {
        return Float.intBitsToFloat(readInt(buffer, offset));
    }

    public static int readInt(final byte[] buffer, final int offset) {
        return (normalizeInt(buffer[offset]) << 24)
                | (normalizeInt(buffer[offset + 1]) << 16)
                | (normalizeInt(buffer[offset + 2]) << 8)
                | normalizeInt(buffer[offset + 3]);
    }

    public static int readIntLittleEndian(final byte[] buffer, final int offset) {
        return normalizeInt(buffer[offset])
                | (normalizeInt(buffer[offset + 1]) << 8)
                | (normalizeInt(buffer[offset + 2]) << 16)
                | (normalizeInt(buffer[offset + 3]) << 24);
    }

    public static short readShort(final byte[] buffer, final int offset) {
        return (short) ((normalizeInt(buffer[offset]) << 8) | normalizeInt(buffer[offset + 1]));
    }

    public static char readChar(final byte[] buffer, final int offset) {
        return (char) ((normalizeInt(buffer[offset]) << 8) | normalizeInt(buffer[offset + 1]));
    }

    public static byte readByte(final byte[] buffer, final int offset) {
        return (byte) normalizeInt(buffer[offset]);
    }

    public static boolean readBoolean(final byte[] buffer, final int offset) {
        return normalizeInt(buffer[offset]) != 0;
    }

    private static int normalizeInt(final byte byteValue) {
        return byteValue & BYTE_MASK_INT;
    }

    private static long normalizeLong(final byte byteValue) {
        return byteValue & BYTE_MASK_LONG;
    }
}
