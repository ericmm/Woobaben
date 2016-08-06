package woo.ba.ben.core;


import static woo.ba.ben.core.UnsafeFactory.IS_NATIVE_ORDER_BIG_ENDIAN;

public class DataHandlerFactory {
    public static final IHeapDataHandler HEAP_READER_B = new BigEndianHeapDataHandler();
    public static final IHeapDataHandler HEAP_READER_L = new LittleEndianHeapDataHandler();

    public static final IOffHeapDataHandler OFF_HEAP_READER_B = new BigEndianOffHeapDataHandler();
    public static final IOffHeapDataHandler OFF_HEAP_READER_L = new LittleEndianOffHeapDataHandler();

    private DataHandlerFactory() {
    }

    public static IHeapDataHandler nativeOrderHeapDataHandler() {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? HEAP_READER_B : HEAP_READER_L;
    }

    public static IOffHeapDataHandler nativeOrderOffHeapDataHandler() {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? OFF_HEAP_READER_B : OFF_HEAP_READER_L;
    }

    public static IHeapDataHandler jvmOrNetworkHeapDataHandler() {
        return HEAP_READER_B;
    }

    public static IOffHeapDataHandler jvmOrNetworkOffHeapDataHandler() {
        return OFF_HEAP_READER_B;
    }
}
