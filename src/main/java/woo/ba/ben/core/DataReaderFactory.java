package woo.ba.ben.core;


import static woo.ba.ben.core.UnsafeFactory.IS_NATIVE_ORDER_BIG_ENDIAN;

public class DataReaderFactory {
    public static final IDataReader BIG_ENDIAN = new BigEndianDataReader();
    public static final IDataReader LITTLE_ENDIAN = new LittleEndianDataReader();

    private DataReaderFactory() {
    }

    public static IDataReader getNativeOrderDataReader() {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? BIG_ENDIAN : LITTLE_ENDIAN;
    }

    public static IDataReader getJvmOrNetworkDataReader() {
        return BIG_ENDIAN;
    }
}
