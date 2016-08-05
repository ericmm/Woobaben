package woo.ba.ben.core;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static woo.ba.ben.core.DataReaderFactory.OFF_HEAP_READER_B;
import static woo.ba.ben.core.DataReaderFactory.OFF_HEAP_READER_L;
import static woo.ba.ben.core.IDataReader.BYTE_MASK_LONG;
import static woo.ba.ben.core.UnsafeFactory.IS_NATIVE_ORDER_BIG_ENDIAN;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public class LittleEndianOffHeapDataReaderTest {
    private IOffHeapDataReader readerL, readerB;
    private static long address;

    @BeforeClass
    public static void init() {
        address = UNSAFE.allocateMemory(8);

        unsafePut(address, (byte) 0);
        unsafePut(address + 1, (byte) 1);
        unsafePut(address + 2, (byte) 2);
        unsafePut(address + 3, (byte) 3);
        unsafePut(address + 4, (byte) 4);
        unsafePut(address + 5, (byte) 5);
        unsafePut(address + 6, (byte) 6);
        unsafePut(address + 7, (byte) 7);
    }

    @Before
    public void setup() throws Exception {
        readerL = OFF_HEAP_READER_L;
        readerB = OFF_HEAP_READER_B;
    }

    @Test
    public void shouldPrintOutValue() {
        long unsafeReadValue = UNSAFE.getLong(address);
        long littleEndianValue = readerL.readLong(address);
        long bigEndianValue = readerB.readLong(address);
        long littleEndianRaw = readLongRawL(address);
        long bigEndianRaw = readLongRawB(address);

        System.out.println("System native byte order  =====>" + (IS_NATIVE_ORDER_BIG_ENDIAN ? "big endian" : "little endian"));
        System.out.println("Unsafe read value =====>" + unsafeReadValue);
        System.out.println("LE Reader  =====>" + littleEndianValue);
        System.out.println("LE Raw  =====>" + littleEndianRaw);
        System.out.println("BE Reader  =====>" + bigEndianValue);
        System.out.println("BE Raw  =====>" + bigEndianRaw);

        assertEquals(littleEndianValue, littleEndianRaw);
        assertEquals(bigEndianValue, bigEndianRaw);

        if(IS_NATIVE_ORDER_BIG_ENDIAN) {
            assertEquals(unsafeReadValue, bigEndianValue);
        } else {
            assertEquals(littleEndianValue, unsafeReadValue);
        }
    }

    private long readLongRawB(final long startAddress) {
        return makeLong(
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

    private long readLongRawL(final long startAddress) {
        return makeLong(
                unsafeGet(startAddress + 7),
                unsafeGet(startAddress + 6),
                unsafeGet(startAddress + 5),
                unsafeGet(startAddress + 4),
                unsafeGet(startAddress + 3),
                unsafeGet(startAddress + 2),
                unsafeGet(startAddress + 1),
                unsafeGet(startAddress)
        );
    }

    private static long makeLong(final byte b7, final byte b6, final byte b5, final byte b4,
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

    private static byte unsafeGet(final long address) {
        return UNSAFE.getByte(address);
    }

    private static void unsafePut(final long address, final byte value) {
        UNSAFE.putByte(address, value);
    }
}