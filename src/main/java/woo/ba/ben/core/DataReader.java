package woo.ba.ben.core;


import static woo.ba.ben.core.UnsafeFactory.IS_NATIVE_ORDER_BIG_ENDIAN;

public class DataReader {
    public static final IDataReader BIG_ENDIAN_DATA_READER = new BigEndianDataReader();
    public static final IDataReader LITTLE_ENDIAN_DATA_READER = new LittleEndianDataReader();

    private DataReader() {
    }

    public static IDataReader getNativeOrderDataReader() {
        return IS_NATIVE_ORDER_BIG_ENDIAN ? BIG_ENDIAN_DATA_READER : LITTLE_ENDIAN_DATA_READER;
    }

    public static IDataReader getJvmOrNetworkDataReader() {
        return BIG_ENDIAN_DATA_READER;
    }
}
