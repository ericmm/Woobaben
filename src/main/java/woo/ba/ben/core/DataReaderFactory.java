package woo.ba.ben.core;


import static woo.ba.ben.core.UnsafeFactory.IS_NATIVE_ORDER_BIG_ENDIAN;

public class DataReaderFactory {
    public static final IHeapDataReader BIG_ENDIAN_HEAP = new BigEndianHeapDataReader();
    public static final IHeapDataReader LITTLE_ENDIAN_HEAP = new LittleEndianHeapDataReader();

    public static final IOffHeapDataReader BIG_ENDIAN_OFF_HEAP = new BigEndianOffHeapDataReader();
    public static final IOffHeapDataReader LITTLE_ENDIAN_OFF_HEAP = new LittleEndianOffHeapDataReader();

    private DataReaderFactory() {
    }

    public static IHeapDataReader getNativeOrderHeapDataReader() {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? BIG_ENDIAN_HEAP : LITTLE_ENDIAN_HEAP;
    }

    public static IOffHeapDataReader getNativeOrderOffHeapDataReader() {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? BIG_ENDIAN_OFF_HEAP : LITTLE_ENDIAN_OFF_HEAP;
    }

    public static IHeapDataReader getJvmOrNetworkHeapDataReader() {
        return BIG_ENDIAN_HEAP;
    }

    public static IOffHeapDataReader getJvmOrNetworkOffHeapDataReader() {
        return BIG_ENDIAN_OFF_HEAP;
    }
}
