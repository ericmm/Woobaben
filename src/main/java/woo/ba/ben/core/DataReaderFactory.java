package woo.ba.ben.core;


import static woo.ba.ben.core.UnsafeFactory.IS_NATIVE_ORDER_BIG_ENDIAN;

public class DataReaderFactory {
    public static final IHeapDataReader HEAP_READER_B = new BigEndianHeapDataReader();
    public static final IHeapDataReader HEAP_READER_L = new LittleEndianHeapDataReader();

    public static final IOffHeapDataReader OFF_HEAP_READER_B = new BigEndianOffHeapDataReader();
    public static final IOffHeapDataReader OFF_HEAP_READER_L = new LittleEndianOffHeapDataReader();

    private DataReaderFactory() {
    }

    public static IHeapDataReader nativeOrderHeapDataReader() {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? HEAP_READER_B : HEAP_READER_L;
    }

    public static IOffHeapDataReader nativeOrderOffHeapDataReader() {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? OFF_HEAP_READER_B : OFF_HEAP_READER_L;
    }

    public static IHeapDataReader jvmOrNetworkHeapDataReader() {
        return HEAP_READER_B;
    }

    public static IOffHeapDataReader jvmOrNetworkOffHeapDataReader() {
        return OFF_HEAP_READER_B;
    }
}
