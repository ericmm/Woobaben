package woo.ba.ben.util;


import static woo.ba.ben.util.Util.checkNotNull;

/*
 * from https://github.com/prasanthj/hasher/blob/master/src/main/java/hasher/FNV1a.java
 */
public class FNV1a {
    private static final int FNV1_32_INIT = 0x811c9dc5;
    private static final int FNV1_PRIME_32 = 16777619;
    private static final long FNV1_64_INIT = 0xcbf29ce484222325L;
    private static final long FNV1_PRIME_64 = 1099511628211L;

    private FNV1a(){}
    /**
     * FNV1a 32 bit variant.
     *
     * @param data - input byte array
     * @return - hashcode
     */
    public static int hash32(final byte[] data) {
        checkNotNull(data);
        return hash32(data, data.length);
    }

    private static int hash32(final byte[] data, final int length) {
        int hash = FNV1_32_INIT;
        for (int i = 0; i < length; i++) {
            hash ^= (data[i] & 0xff);
            hash *= FNV1_PRIME_32;
        }

        return hash;
    }

    /**
     * FNV1a 64 bit variant.
     *
     * @param data - input byte array
     * @return - hashcode
     */
    public static long hash64(final byte[] data) {
        checkNotNull(data);
        return hash64(data, data.length);
    }

    private static long hash64(final byte[] data, final int length) {
        long hash = FNV1_64_INIT;
        for (int i = 0; i < length; i++) {
            hash ^= (data[i] & 0xff);
            hash *= FNV1_PRIME_64;
        }

        return hash;
    }
}
