package woo.ba.ben.core;


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

    static int unsignedShort(final short num) {
        return num & 0xFFFF;
    }

    static long unsignedInt(final int num) {
        return num & 0xFFFFFFFFL;
    }
}
