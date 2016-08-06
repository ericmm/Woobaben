package woo.ba.ben.core;

import static java.lang.Double.doubleToRawLongBits;
import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.floatToRawIntBits;
import static java.lang.Float.intBitsToFloat;
import static woo.ba.ben.core.UnsafeFactory.IS_NATIVE_ORDER_BIG_ENDIAN;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class BigEndianOffHeapDataReader extends AbstractDataReader implements IOffHeapDataReader {
    BigEndianOffHeapDataReader() {
    }

    @Override
    public long readLong(final long startAddress) {
        return IS_NATIVE_ORDER_BIG_ENDIAN ?
                UNSAFE.getLong(startAddress) :
                makeLong(
                        unsafeGet(startAddress),
                        unsafeGet(startAddress + 1),
                        unsafeGet(startAddress + 2),
                        unsafeGet(startAddress + 3),
                        unsafeGet(startAddress + 4),
                        unsafeGet(startAddress + 5),
                        unsafeGet(startAddress + 6),
                        unsafeGet(startAddress + 7)
                );
    }

    @Override
    public double readDouble(final long startAddress) {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? UNSAFE.getDouble(startAddress) : longBitsToDouble(readLong(startAddress));
    }


    @Override
    public float readFloat(final long startAddress) {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? UNSAFE.getFloat(startAddress) : intBitsToFloat(readInt(startAddress));
    }

    @Override
    public int readInt(final long startAddress) {
        return IS_NATIVE_ORDER_BIG_ENDIAN ?
                UNSAFE.getInt(startAddress) :
                makeInt(
                        unsafeGet(startAddress),
                        unsafeGet(startAddress + 1),
                        unsafeGet(startAddress + 2),
                        unsafeGet(startAddress + 3)
                );
    }

    @Override
    public short readShort(final long startAddress) {
        return IS_NATIVE_ORDER_BIG_ENDIAN ?
                UNSAFE.getShort(startAddress) :
                makeShort(unsafeGet(startAddress), unsafeGet(startAddress + 1));
    }

    @Override
    public char readChar(final long startAddress) {
        return IS_NATIVE_ORDER_BIG_ENDIAN ?
                UNSAFE.getChar(startAddress) :
                makeChar(unsafeGet(startAddress), unsafeGet(startAddress + 1));
    }

    @Override
    public void writeChar(final long startAddress, final char num) {
        if (IS_NATIVE_ORDER_BIG_ENDIAN) {
            UNSAFE.putChar(startAddress, num);
        } else {
            unsafePut(startAddress, char1(num));
            unsafePut(startAddress + 1, char0(num));
        }
    }

    @Override
    public void writeShort(final long startAddress, final short num) {
        if (IS_NATIVE_ORDER_BIG_ENDIAN) {
            UNSAFE.putShort(startAddress, num);
        } else {
            unsafePut(startAddress, short1(num));
            unsafePut(startAddress + 1, short0(num));
        }
    }

    @Override
    public void writeInt(final long startAddress, final int num) {
        if (IS_NATIVE_ORDER_BIG_ENDIAN) {
            UNSAFE.putInt(startAddress, num);
        } else {
            unsafePut(startAddress, int3(num));
            unsafePut(startAddress + 1, int2(num));
            unsafePut(startAddress + 2, int1(num));
            unsafePut(startAddress + 3, int0(num));
        }
    }

    @Override
    public void writeFloat(final long startAddress, final float num) {
        if (IS_NATIVE_ORDER_BIG_ENDIAN) {
            UNSAFE.putFloat(startAddress, num);
        } else {
            writeInt(startAddress, floatToRawIntBits(num));
        }
    }

    @Override
    public void writeDouble(final long startAddress, final double num) {
        if (IS_NATIVE_ORDER_BIG_ENDIAN) {
            UNSAFE.putDouble(startAddress, num);
        } else {
            writeLong(startAddress, doubleToRawLongBits(num));
        }
    }

    @Override
    public void writeLong(final long startAddress, final long num) {
        if (IS_NATIVE_ORDER_BIG_ENDIAN) {
            UNSAFE.putLong(startAddress, num);
        } else {
            unsafePut(startAddress, long7(num));
            unsafePut(startAddress + 1, long6(num));
            unsafePut(startAddress + 2, long5(num));
            unsafePut(startAddress + 3, long4(num));
            unsafePut(startAddress + 4, long3(num));
            unsafePut(startAddress + 5, long2(num));
            unsafePut(startAddress + 6, long1(num));
            unsafePut(startAddress + 7, long0(num));
        }
    }
}
