package woo.ba.ben.core;


import static woo.ba.ben.core.IDataReader.BYTE_MASK_INT;
import static woo.ba.ben.core.IDataReader.BYTE_MASK_LONG;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

abstract class AbstractDataReader {

    static byte char1(final char x) {
        return (byte) (x >> 8);
    }

    static byte char0(final char x) {
        return (byte) (x);
    }

    static byte short1(final short x) {
        return (byte) (x >> 8);
    }

    static byte short0(final short x) {
        return (byte) (x);
    }

    static byte int3(final int x) {
        return (byte) (x >> 24);
    }

    static byte int2(final int x) {
        return (byte) (x >> 16);
    }

    static byte int1(final int x) {
        return (byte) (x >> 8);
    }

    static byte int0(final int x) {
        return (byte) (x);
    }

    static byte long7(final long x) {
        return (byte) (x >> 56);
    }

    static byte long6(final long x) {
        return (byte) (x >> 48);
    }

    static byte long5(final long x) {
        return (byte) (x >> 40);
    }

    static byte long4(final long x) {
        return (byte) (x >> 32);
    }

    static byte long3(final long x) {
        return (byte) (x >> 24);
    }

    static byte long2(final long x) {
        return (byte) (x >> 16);
    }

    static byte long1(final long x) {
        return (byte) (x >> 8);
    }

    static byte long0(final long x) {
        return (byte) (x);
    }

    static char makeChar(final byte b1, final byte b0) {
        return (char) ((b1 << 8) | (b0 & BYTE_MASK_INT));
    }

    static short makeShort(final byte b1, final byte b0) {
        return (short) ((b1 << 8) | (b0 & BYTE_MASK_INT));
    }

    static int makeInt(final byte b3, final byte b2, final byte b1, final byte b0) {
        return ((b3) << 24) |
                ((b2 & BYTE_MASK_INT) << 16) |
                ((b1 & BYTE_MASK_INT) << 8) |
                (b0 & BYTE_MASK_INT);
    }

    static long makeLong(final byte b7, final byte b6, final byte b5, final byte b4,
                         final byte b3, final byte b2, final byte b1, final byte b0) {
        return (((long) b7) << 56) |
                ((b6 & BYTE_MASK_LONG) << 48) |
                ((b5 & BYTE_MASK_LONG) << 40) |
                ((b4 & BYTE_MASK_LONG) << 32) |
                ((b3 & BYTE_MASK_LONG) << 24) |
                ((b2 & BYTE_MASK_LONG) << 16) |
                ((b1 & BYTE_MASK_LONG) << 8) |
                (b0 & BYTE_MASK_LONG);
    }

    static byte unsafeGet(final long address) {
        return UNSAFE.getByte(address);
    }

    static void unsafePut(final long address, final byte value) {
        UNSAFE.putByte(address, value);
    }
}
