package woo.ba.ben.core;


public class LittleEndianHeapDataReader extends AbstractDataReader implements IHeapDataReader {
    @Override
    public long readLong(final byte[] buffer, final int startIndex) {
        return makeLong(
                buffer[startIndex + 7],
                buffer[startIndex + 6],
                buffer[startIndex + 5],
                buffer[startIndex + 4],
                buffer[startIndex + 3],
                buffer[startIndex + 2],
                buffer[startIndex + 1],
                buffer[startIndex]
        );
    }

    @Override
    public int readInt(final byte[] buffer, final int startIndex) {
        return makeInt(
                buffer[startIndex + 3],
                buffer[startIndex + 2],
                buffer[startIndex + 1],
                buffer[startIndex]
        );
    }

    @Override
    public short readShort(final byte[] buffer, final int startIndex) {
        return makeShort(buffer[startIndex + 1], buffer[startIndex]);
    }

    @Override
    public char readChar(final byte[] buffer, final int startIndex) {
        return makeChar(buffer[startIndex + 1], buffer[startIndex]);
    }

    @Override
    public void writeChar(final byte[] buffer, final int startIndex, final char num) {
        buffer[startIndex] = char0(num);
        buffer[startIndex + 1] = char1(num);
    }

    @Override
    public void writeShort(final byte[] buffer, final int startIndex, final short num) {
        buffer[startIndex] = short0(num);
        buffer[startIndex + 1] = short1(num);
    }

    @Override
    public void writeInt(final byte[] buffer, final int startIndex, final int num) {
        buffer[startIndex] = int0(num);
        buffer[startIndex + 1] = int1(num);
        buffer[startIndex + 2] = int2(num);
        buffer[startIndex + 3] = int3(num);
    }

    @Override
    public void writeLong(final byte[] buffer, final int startIndex, final long num) {
        buffer[startIndex] = long0(num);
        buffer[startIndex + 1] = long1(num);
        buffer[startIndex + 2] = long2(num);
        buffer[startIndex + 3] = long3(num);
        buffer[startIndex + 4] = long4(num);
        buffer[startIndex + 5] = long5(num);
        buffer[startIndex + 6] = long6(num);
        buffer[startIndex + 7] = long7(num);
    }
}
