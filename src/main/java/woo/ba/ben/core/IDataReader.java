package woo.ba.ben.core;


public interface IDataReader {
    int BYTE_MASK_INT = 0xFF;
    long BYTE_MASK_LONG = 0xFFL;

    double readDouble(final byte[] buffer, final int offset);

    long readLong(final byte[] buffer, final int offset);

    float readFloat(final byte[] buffer, final int offset);

    int readInt(final byte[] buffer, final int offset);

    short readShort(final byte[] buffer, final int offset);

    char readChar(final byte[] buffer, final int offset);

    default byte readByte(final byte[] buffer, final int offset) {
        return (byte) (buffer[offset] & BYTE_MASK_INT);
    }

    default boolean readBoolean(final byte[] buffer, final int offset) {
        return readByte(buffer, offset) != 0;
    }

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
        for (int offset = 0; offset < loopLength; offset += Long.BYTES) {
            if ((readLong(left, offset) ^ readLong(right, offset)) != 0) {
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

    default long unsignedInt(final int num) {
        return num & 0xFFFFFFFFL;
    }

    default int unsignedShort(final short num) {
        return num & 0xFFFF;
    }
}
