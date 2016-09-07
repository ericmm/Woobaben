package woo.ba.ben.core;


import static java.lang.Double.doubleToRawLongBits;
import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.floatToRawIntBits;
import static java.lang.Float.intBitsToFloat;
import static java.lang.Long.BYTES;
import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;
import static woo.ba.ben.core.IDataReader.unsignedByte;
import static woo.ba.ben.core.IDataReader.unsignedInt;
import static woo.ba.ben.core.IDataReader.unsignedShort;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public interface IHeapDataHandler extends IDataReader {

    default short readUnsignedByte(final byte[] buffer, final int startIndex) {
        return unsignedByte(buffer[startIndex]);
    }

    default int readUnsignedShort(final byte[] buffer, final int startIndex) {
        return unsignedShort(readShort(buffer, startIndex));
    }

    default long readUnsignedInt(final byte[] buffer, final int startIndex) {
        return unsignedInt(readInt(buffer, startIndex));
    }

    default double readDouble(final byte[] buffer, final int startIndex) {
        return longBitsToDouble(readLong(buffer, startIndex));
    }

    long readLong(final byte[] buffer, final int startIndex);

    default float readFloat(final byte[] buffer, final int startIndex) {
        return intBitsToFloat(readInt(buffer, startIndex));
    }

    int readInt(final byte[] buffer, final int startIndex);

    short readShort(final byte[] buffer, final int startIndex);

    char readChar(final byte[] buffer, final int startIndex);

    static boolean arrayEquals(final byte[] left, final byte[] right) {
        if (left == right) {
            return true;
        }

        if (left == null || right == null) {
            return false;
        }

        if (right.length != left.length) {
            return false;
        }

        final int loopTimes = left.length / BYTES;
        final int loopLength = loopTimes * BYTES;
        long currentOffset = ARRAY_BYTE_BASE_OFFSET;
        for (int startIndex = 0; startIndex < loopLength; startIndex += BYTES) {
            if (UNSAFE.getLong(left, currentOffset) != UNSAFE.getLong(right, currentOffset)) {
                return false;
            }
            currentOffset += BYTES;
        }

        // for remaining
        for (int i = loopLength; i < left.length; i++) {
            if (left[i] != right[i]) {
                return false;
            }
        }
        return true;
    }

    void writeChar(final byte[] buffer, final int startIndex, final char num);

    void writeShort(final byte[] buffer, final int startIndex, final short num);

    void writeInt(final byte[] buffer, final int startIndex, final int num);

    default void writeFloat(final byte[] buffer, final int startIndex, final float num) {
        writeInt(buffer, startIndex, floatToRawIntBits(num));
    }

    void writeLong(final byte[] buffer, final int startIndex, final long num);

    default void writeDouble(final byte[] buffer, final int startIndex, final double num) {
        writeLong(buffer, startIndex, doubleToRawLongBits(num));
    }
}
