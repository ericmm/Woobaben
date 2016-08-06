package woo.ba.ben.core;

class BigEndianHeapDataHandler extends AbstractDataReader implements IHeapDataHandler {
    BigEndianHeapDataHandler() {
    }

    @Override
    public long readLong(final byte[] buffer, final int startIndex) {
        return makeLong(
                buffer[startIndex],
                buffer[startIndex + 1],
                buffer[startIndex + 2],
                buffer[startIndex + 3],
                buffer[startIndex + 4],
                buffer[startIndex + 5],
                buffer[startIndex + 6],
                buffer[startIndex + 7]
        );
    }

    @Override
    public int readInt(final byte[] buffer, final int startIndex) {
        return makeInt(
                buffer[startIndex],
                buffer[startIndex + 1],
                buffer[startIndex + 2],
                buffer[startIndex + 3]
        );
    }

    @Override
    public short readShort(final byte[] buffer, final int startIndex) {
        return makeShort(buffer[startIndex], buffer[startIndex + 1]);
    }

    @Override
    public char readChar(final byte[] buffer, final int startIndex) {
        return makeChar(buffer[startIndex], buffer[startIndex + 1]);
    }

    @Override
    public void writeChar(final byte[] buffer, final int startIndex, final char num) {
        buffer[startIndex] = char1(num);
        buffer[startIndex + 1] = char0(num);
    }

    @Override
    public void writeShort(final byte[] buffer, final int startIndex, final short num) {
        buffer[startIndex] = short1(num);
        buffer[startIndex + 1] = short0(num);
    }

    @Override
    public void writeInt(final byte[] buffer, final int startIndex, final int num) {
        buffer[startIndex] = int3(num);
        buffer[startIndex + 1] = int2(num);
        buffer[startIndex + 2] = int1(num);
        buffer[startIndex + 3] = int0(num);
    }

    @Override
    public void writeLong(final byte[] buffer, final int startIndex, final long num) {
        buffer[startIndex] = long7(num);
        buffer[startIndex + 1] = long6(num);
        buffer[startIndex + 2] = long5(num);
        buffer[startIndex + 3] = long4(num);
        buffer[startIndex + 4] = long3(num);
        buffer[startIndex + 5] = long2(num);
        buffer[startIndex + 6] = long1(num);
        buffer[startIndex + 7] = long0(num);
    }
}
