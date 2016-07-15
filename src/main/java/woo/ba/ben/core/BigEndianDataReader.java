package woo.ba.ben.core;

import static java.lang.Double.longBitsToDouble;
import static java.lang.Float.intBitsToFloat;


public class BigEndianDataReader implements IDataReader {

    @Override
    public double readDouble(final byte[] buffer, final int offset) {
        return longBitsToDouble(readLong(buffer, offset));
    }

    @Override
    public long readLong(final byte[] buffer, final int offset) {
        return ((buffer[offset] & BYTE_MASK_LONG) << 56)
                | ((buffer[offset + 1] & BYTE_MASK_LONG) << 48)
                | ((buffer[offset + 2] & BYTE_MASK_LONG) << 40)
                | ((buffer[offset + 3] & BYTE_MASK_LONG) << 32)
                | ((buffer[offset + 4] & BYTE_MASK_LONG) << 24)
                | ((buffer[offset + 5] & BYTE_MASK_LONG) << 16)
                | ((buffer[offset + 6] & BYTE_MASK_LONG) << 8)
                | (buffer[offset + 7] & BYTE_MASK_LONG);
    }

    @Override
    public float readFloat(final byte[] buffer, final int offset) {
        return intBitsToFloat(readInt(buffer, offset));
    }

    @Override
    public int readInt(final byte[] buffer, final int offset) {
        return ((buffer[offset] & BYTE_MASK_INT) << 24)
                | ((buffer[offset + 1] & BYTE_MASK_INT) << 16)
                | ((buffer[offset + 2] & BYTE_MASK_INT) << 8)
                | (buffer[offset + 3] & BYTE_MASK_INT);
    }

    @Override
    public short readShort(final byte[] buffer, final int offset) {
        return (short) (((buffer[offset] & BYTE_MASK_INT) << 8) | (buffer[offset + 1] & BYTE_MASK_INT));
    }

    @Override
    public char readChar(final byte[] buffer, final int offset) {
        return (char) (((buffer[offset] & BYTE_MASK_INT) << 8) | (buffer[offset + 1] & BYTE_MASK_INT));
    }
}
