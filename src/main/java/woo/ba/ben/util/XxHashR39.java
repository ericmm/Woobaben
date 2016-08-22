package woo.ba.ben.util;


import woo.ba.ben.core.DataHandlerFactory;
import woo.ba.ben.core.IHeapDataHandler;
import woo.ba.ben.core.IOffHeapDataHandler;

import static woo.ba.ben.core.DataHandlerFactory.nativeOrderHeapDataHandler;
import static woo.ba.ben.core.DataHandlerFactory.nativeOrderOffHeapDataHandler;

/*
 * refactored from
 * https://github.com/OpenHFT/Zero-Allocation-Hashing/blob/master/src/main/java/net/openhft/hashing/XxHash_r39.java
 *
 */
public class XxHashR39 {
    // Primes if treated as unsigned
    private static final long P1 = -7046029288634856825L;
    private static final long P2 = -4417276706812531889L;
    private static final long P3 = 1609587929392839161L;
    private static final long P4 = -8796714831421723037L;
    private static final long P5 = 2870177450012600261L;

    private XxHashR39() {
    }

    public static long hash64(final long seed, final byte[] input, int offset, final int length) {
        final IHeapDataHandler dataHandler = nativeOrderHeapDataHandler();

        long hash;
        long remaining = length;

        if (remaining >= 32) {
            long v1 = seed + P1 + P2;
            long v2 = seed + P2;
            long v3 = seed;
            long v4 = seed - P1;

            do {
                v1 += dataHandler.readLong(input, offset) * P2;
                v1 = Long.rotateLeft(v1, 31);
                v1 *= P1;

                v2 += dataHandler.readLong(input, offset + 8) * P2;
                v2 = Long.rotateLeft(v2, 31);
                v2 *= P1;

                v3 += dataHandler.readLong(input, offset + 16) * P2;
                v3 = Long.rotateLeft(v3, 31);
                v3 *= P1;

                v4 += dataHandler.readLong(input, offset + 24) * P2;
                v4 = Long.rotateLeft(v4, 31);
                v4 *= P1;

                offset += 32;
                remaining -= 32;
            } while (remaining >= 32);

            hash = Long.rotateLeft(v1, 1)
                    + Long.rotateLeft(v2, 7)
                    + Long.rotateLeft(v3, 12)
                    + Long.rotateLeft(v4, 18);

            v1 *= P2;
            v1 = Long.rotateLeft(v1, 31);
            v1 *= P1;
            hash ^= v1;
            hash = hash * P1 + P4;

            v2 *= P2;
            v2 = Long.rotateLeft(v2, 31);
            v2 *= P1;
            hash ^= v2;
            hash = hash * P1 + P4;

            v3 *= P2;
            v3 = Long.rotateLeft(v3, 31);
            v3 *= P1;
            hash ^= v3;
            hash = hash * P1 + P4;

            v4 *= P2;
            v4 = Long.rotateLeft(v4, 31);
            v4 *= P1;
            hash ^= v4;
            hash = hash * P1 + P4;
        } else {
            hash = seed + P5;
        }

        hash += length;

        while (remaining >= 8) {
            long k1 = dataHandler.readLong(input, offset);
            k1 *= P2;
            k1 = Long.rotateLeft(k1, 31);
            k1 *= P1;
            hash ^= k1;
            hash = Long.rotateLeft(hash, 27) * P1 + P4;
            offset += 8;
            remaining -= 8;
        }

        if (remaining >= 4) {
            hash ^= dataHandler.readInt(input, offset) * P1;
            hash = Long.rotateLeft(hash, 23) * P2 + P3;
            offset += 4;
            remaining -= 4;
        }

        while (remaining != 0) {
            hash ^= dataHandler.readUnsignedByte(input, offset) * P5;
            hash = Long.rotateLeft(hash, 11) * P1;
            --remaining;
            ++offset;
        }

        return finalize(hash);
    }

    public static long hash64(final long seed, final long input, long offset, final long length) {
        final IOffHeapDataHandler dataHandler = nativeOrderOffHeapDataHandler();

        long hash;
        long remaining = length;

        if (remaining >= 32) {
            long v1 = seed + P1 + P2;
            long v2 = seed + P2;
            long v3 = seed;
            long v4 = seed - P1;

            do {
                v1 += dataHandler.readLong(input + offset) * P2;
                v1 = Long.rotateLeft(v1, 31);
                v1 *= P1;

                v2 += dataHandler.readLong(input + offset + 8) * P2;
                v2 = Long.rotateLeft(v2, 31);
                v2 *= P1;

                v3 += dataHandler.readLong(input + offset + 16) * P2;
                v3 = Long.rotateLeft(v3, 31);
                v3 *= P1;

                v4 += dataHandler.readLong(input + offset + 24) * P2;
                v4 = Long.rotateLeft(v4, 31);
                v4 *= P1;

                offset += 32;
                remaining -= 32;
            } while (remaining >= 32);

            hash = Long.rotateLeft(v1, 1)
                    + Long.rotateLeft(v2, 7)
                    + Long.rotateLeft(v3, 12)
                    + Long.rotateLeft(v4, 18);

            v1 *= P2;
            v1 = Long.rotateLeft(v1, 31);
            v1 *= P1;
            hash ^= v1;
            hash = hash * P1 + P4;

            v2 *= P2;
            v2 = Long.rotateLeft(v2, 31);
            v2 *= P1;
            hash ^= v2;
            hash = hash * P1 + P4;

            v3 *= P2;
            v3 = Long.rotateLeft(v3, 31);
            v3 *= P1;
            hash ^= v3;
            hash = hash * P1 + P4;

            v4 *= P2;
            v4 = Long.rotateLeft(v4, 31);
            v4 *= P1;
            hash ^= v4;
            hash = hash * P1 + P4;
        } else {
            hash = seed + P5;
        }

        hash += length;

        while (remaining >= 8) {
            long k1 = dataHandler.readLong(input + offset);
            k1 *= P2;
            k1 = Long.rotateLeft(k1, 31);
            k1 *= P1;
            hash ^= k1;
            hash = Long.rotateLeft(hash, 27) * P1 + P4;
            offset += 8;
            remaining -= 8;
        }

        if (remaining >= 4) {
            hash ^= dataHandler.readInt(input + offset) * P1;
            hash = Long.rotateLeft(hash, 23) * P2 + P3;
            offset += 4;
            remaining -= 4;
        }

        while (remaining != 0) {
            hash ^= dataHandler.readUnsignedByte(input + offset) * P5;
            hash = Long.rotateLeft(hash, 11) * P1;
            --remaining;
            ++offset;
        }

        return finalize(hash);
    }

    private static long finalize(long hash) {
        hash ^= hash >>> 33;
        hash *= P2;
        hash ^= hash >>> 29;
        hash *= P3;
        hash ^= hash >>> 32;
        return hash;
    }
}
