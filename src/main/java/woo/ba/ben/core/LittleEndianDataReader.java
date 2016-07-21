package woo.ba.ben.core;


import static woo.ba.ben.core.IDataReader.char0;
import static woo.ba.ben.core.IDataReader.char1;
import static woo.ba.ben.core.IDataReader.int0;
import static woo.ba.ben.core.IDataReader.int1;
import static woo.ba.ben.core.IDataReader.int2;
import static woo.ba.ben.core.IDataReader.int3;
import static woo.ba.ben.core.IDataReader.long0;
import static woo.ba.ben.core.IDataReader.long1;
import static woo.ba.ben.core.IDataReader.long2;
import static woo.ba.ben.core.IDataReader.long3;
import static woo.ba.ben.core.IDataReader.long4;
import static woo.ba.ben.core.IDataReader.long5;
import static woo.ba.ben.core.IDataReader.long6;
import static woo.ba.ben.core.IDataReader.long7;
import static woo.ba.ben.core.IDataReader.makeChar;
import static woo.ba.ben.core.IDataReader.makeInt;
import static woo.ba.ben.core.IDataReader.makeLong;
import static woo.ba.ben.core.IDataReader.makeShort;
import static woo.ba.ben.core.IDataReader.short0;
import static woo.ba.ben.core.IDataReader.short1;

public class LittleEndianDataReader implements IDataReader {
    @Override
    public long readLong(final byte[] buffer, final int offset) {
        return makeLong(
                buffer[offset + 7],
                buffer[offset + 6],
                buffer[offset + 5],
                buffer[offset + 4],
                buffer[offset + 3],
                buffer[offset + 2],
                buffer[offset + 1],
                buffer[offset]
        );
    }

    @Override
    public int readInt(final byte[] buffer, final int offset) {
        return makeInt(
                buffer[offset + 3],
                buffer[offset + 2],
                buffer[offset + 1],
                buffer[offset]
        );
    }

    @Override
    public short readShort(final byte[] buffer, final int offset) {
        return makeShort(buffer[offset + 1], buffer[offset]);
    }

    @Override
    public char readChar(final byte[] buffer, final int offset) {
        return makeChar(buffer[offset + 1], buffer[offset]);
    }

    @Override
    public void writeChar(final byte[] buffer, final int startOffset, final char num) {
        buffer[startOffset] = char0(num);
        buffer[startOffset + 1] = char1(num);
    }

    @Override
    public void writeShort(final byte[] buffer, final int startOffset, final short num) {
        buffer[startOffset] = short0(num);
        buffer[startOffset + 1] = short1(num);
    }

    @Override
    public void writeInt(final byte[] buffer, final int startOffset, final int num) {
        buffer[startOffset] = int0(num);
        buffer[startOffset + 1] = int1(num);
        buffer[startOffset + 2] = int2(num);
        buffer[startOffset + 3] = int3(num);
    }

    @Override
    public void writeLong(final byte[] buffer, final int startOffset, final long num) {
        buffer[startOffset] = long0(num);
        buffer[startOffset + 1] = long1(num);
        buffer[startOffset + 2] = long2(num);
        buffer[startOffset + 3] = long3(num);
        buffer[startOffset + 4] = long4(num);
        buffer[startOffset + 5] = long5(num);
        buffer[startOffset + 6] = long6(num);
        buffer[startOffset + 7] = long7(num);
    }
}
