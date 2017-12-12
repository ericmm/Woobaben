package woo.ba.ben.core;

import static java.lang.Double.doubleToRawLongBits;
import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.floatToRawIntBits;
import static java.lang.Float.intBitsToFloat;
import static woo.ba.ben.core.UnsafeFactory.IS_NATIVE_ORDER_BIG_ENDIAN;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class LittleEndianOffHeapDataHandler extends AbstractDataReader implements IOffHeapDataHandler {
    LittleEndianOffHeapDataHandler() {
    }

    @Override
    public long readLong(final long startAddress) {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? makeLong(
                unsafeGet(startAddress + 7),
                unsafeGet(startAddress + 6),
                unsafeGet(startAddress + 5),
                unsafeGet(startAddress + 4),
                unsafeGet(startAddress + 3),
                unsafeGet(startAddress + 2),
                unsafeGet(startAddress + 1),
                unsafeGet(startAddress)
        ) : UNSAFE.getLong(startAddress);
    }

    @Override
    public double readDouble(final long startAddress) {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? longBitsToDouble(readLong(startAddress)) : UNSAFE.getDouble(startAddress);
    }

    @Override
    public float readFloat(final long startAddress) {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? intBitsToFloat(readInt(startAddress)) : UNSAFE.getFloat(startAddress);
    }

    @Override
    public int readInt(final long startAddress) {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? makeInt(
                unsafeGet(startAddress + 3),
                unsafeGet(startAddress + 2),
                unsafeGet(startAddress + 1),
                unsafeGet(startAddress)
        ) : UNSAFE.getInt(startAddress);
    }

    @Override
    public short readShort(final long startAddress) {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? makeShort(unsafeGet(startAddress + 1), unsafeGet(startAddress)) : UNSAFE.getShort(startAddress);
    }

    @Override
    public char readChar(final long startAddress) {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? makeChar(unsafeGet(startAddress + 1), unsafeGet(startAddress)) : UNSAFE.getChar(startAddress);
    }

    @Override
    public void writeChar(final long startAddress, final char num) {
        if (IS_NATIVE_ORDER_BIG_ENDIAN) {
            unsafePut(startAddress, char0(num));
            unsafePut(startAddress + 1, char1(num));
        } else {
            UNSAFE.putChar(startAddress, num);
        }
    }

    @Override
    public void writeShort(final long startAddress, final short num) {
        if (IS_NATIVE_ORDER_BIG_ENDIAN) {
            unsafePut(startAddress, short0(num));
            unsafePut(startAddress + 1, short1(num));
        } else {
            UNSAFE.putShort(startAddress, num);
        }
    }

    @Override
    public void writeInt(final long startAddress, final int num) {
        if (IS_NATIVE_ORDER_BIG_ENDIAN) {
            unsafePut(startAddress, int0(num));
            unsafePut(startAddress + 1, int1(num));
            unsafePut(startAddress + 2, int2(num));
            unsafePut(startAddress + 3, int3(num));
        } else {
            UNSAFE.putInt(startAddress, num);
        }
    }

    @Override
    public void writeFloat(final long startAddress, final float num) {
        if (IS_NATIVE_ORDER_BIG_ENDIAN) {
            writeInt(startAddress, floatToRawIntBits(num));
        } else {
            UNSAFE.putFloat(startAddress, num);
        }
    }

    @Override
    public void writeDouble(final long startAddress, final double num) {
        if (IS_NATIVE_ORDER_BIG_ENDIAN) {
            writeLong(startAddress, doubleToRawLongBits(num));
        } else {
            UNSAFE.putDouble(startAddress, num);
        }
    }

    @Override
    public void writeLong(final long startAddress, final long num) {
        if (!IS_NATIVE_ORDER_BIG_ENDIAN) {
            UNSAFE.putLong(startAddress, num);
        } else {
            unsafePut(startAddress, long0(num));
            unsafePut(startAddress + 1, long1(num));
            unsafePut(startAddress + 2, long2(num));
            unsafePut(startAddress + 3, long3(num));
            unsafePut(startAddress + 4, long4(num));
            unsafePut(startAddress + 5, long5(num));
            unsafePut(startAddress + 6, long6(num));
            unsafePut(startAddress + 7, long7(num));
        }
    }
}
