package woo.ba.ben.core;


import static java.lang.Double.doubleToRawLongBits;
import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.floatToRawIntBits;
import static java.lang.Float.intBitsToFloat;
import static java.lang.Long.BYTES;
import static woo.ba.ben.core.IDataReader.unsignedByte;
import static woo.ba.ben.core.IDataReader.unsignedInt;
import static woo.ba.ben.core.IDataReader.unsignedShort;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public interface IOffHeapDataHandler extends IDataReader {

    default short readUnsignedByte(final long startAddress) {
        return unsignedByte(UNSAFE.getByte(startAddress));
    }

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

        long leftAddress = leftStartAddress, rightAddress = rightStartAddress;
        for (int startOffset = 0; startOffset < loopLength; startOffset += BYTES) {
            leftAddress += startOffset;
            rightAddress += startOffset;
            if (UNSAFE.getLong(leftAddress) != UNSAFE.getLong(rightAddress)) {
                return false;
            }
        }

        // for remaining
        for (int i = loopLength; i < length; i++) {
            if (UNSAFE.getByte(leftAddress + i) != UNSAFE.getByte(rightAddress + i)) {
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
