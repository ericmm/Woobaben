package woo.ba.ben.core;


import static java.lang.Double.doubleToRawLongBits;
import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.floatToRawIntBits;
import static java.lang.Float.intBitsToFloat;
import static java.lang.Long.BYTES;
import static woo.ba.ben.core.IDataReader.unsignedInt;
import static woo.ba.ben.core.IDataReader.unsignedShort;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public interface IOffHeapDataReader extends IDataReader {

    default int readUnsignedShort(final long startAddress) {
        return unsignedShort(readShort(startAddress));
    }

    default long readUnsignedInt(final long startAddress) {
        return unsignedInt(readInt(startAddress));
    }

    default double readDouble(final long startAddress) {
        return longBitsToDouble(readLong(startAddress));
    }

    long readLong(final long startAddress);

    default float readFloat(final long startAddress) {
        return intBitsToFloat(readInt(startAddress));
    }

    int readInt(final long startAddress);

    short readShort(final long startAddress);

    char readChar(final long startAddress);

    default boolean blockEquals(final long leftStartAddress, final long rightStartAddress, final int length) {
        if (leftStartAddress == rightStartAddress) {
            return true;
        }

        final int loopTimes = length / BYTES;
        final int loopLength = loopTimes * BYTES;
        for (int startOffset = 0; startOffset < loopLength; startOffset += BYTES) {
            if (0L != (UNSAFE.getLong(startOffset) ^ UNSAFE.getLong(startOffset))) {
                return false;
            }
        }

        // for remaining
        for (int i = loopLength; i < length; i++) {
            if (UNSAFE.getByte(i) != UNSAFE.getByte(i)) {
                return false;
            }
        }
        return true;
    }

    void writeChar(final long startAddress, final char num);

    void writeShort(final long startAddress, final short num);

    void writeInt(final long startAddress, final int num);

    default void writeFloat(final long startAddress, final float num) {
        writeInt(startAddress, floatToRawIntBits(num));
    }

    void writeLong(final long startAddress, final long num);

    default void writeDouble(final long startAddress, final double num) {
        writeLong(startAddress, doubleToRawLongBits(num));
    }
}
