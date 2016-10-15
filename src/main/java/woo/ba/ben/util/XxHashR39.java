package woo.ba.ben.util;


import woo.ba.ben.core.IHeapDataHandler;
import woo.ba.ben.core.IOffHeapDataHandler;

import static java.lang.Long.rotateLeft;
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
                v1 = rotateLeft(v1, 31);
                v1 *= P1;

                v2 += dataHandler.readLong(input, offset + 8) * P2;
                v2 = rotateLeft(v2, 31);
                v2 *= P1;

                v3 += dataHandler.readLong(input, offset + 16) * P2;
                v3 = rotateLeft(v3, 31);
                v3 *= P1;

                v4 += dataHandler.readLong(input, offset + 24) * P2;
                v4 = rotateLeft(v4, 31);
                v4 *= P1;

                offset += 32;
                remaining -= 32;
            } while (remaining >= 32);

            hash = rotateLeft(v1, 1)
                    + rotateLeft(v2, 7)
                    + rotateLeft(v3, 12)
                    + rotateLeft(v4, 18);

            v1 *= P2;
            v1 = rotateLeft(v1, 31);
            v1 *= P1;
            hash ^= v1;
            hash = hash * P1 + P4;

            v2 *= P2;
            v2 = rotateLeft(v2, 31);
            v2 *= P1;
            hash ^= v2;
            hash = hash * P1 + P4;

            v3 *= P2;
            v3 = rotateLeft(v3, 31);
            v3 *= P1;
            hash ^= v3;
            hash = hash * P1 + P4;

            v4 *= P2;
            v4 = rotateLeft(v4, 31);
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
            k1 = rotateLeft(k1, 31);
            k1 *= P1;
            hash ^= k1;
            hash = rotateLeft(hash, 27) * P1 + P4;
            offset += 8;
            remaining -= 8;
        }

        if (remaining >= 4) {
            hash ^= dataHandler.readInt(input, offset) * P1;
            hash = rotateLeft(hash, 23) * P2 + P3;
            offset += 4;
            remaining -= 4;
        }

        while (remaining != 0) {
            hash ^= dataHandler.readUnsignedByte(input, offset) * P5;
            hash = rotateLeft(hash, 11) * P1;
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
                v1 = rotateLeft(v1, 31);
                v1 *= P1;

                v2 += dataHandler.readLong(input + offset + 8) * P2;
                v2 = rotateLeft(v2, 31);
                v2 *= P1;

                v3 += dataHandler.readLong(input + offset + 16) * P2;
                v3 = rotateLeft(v3, 31);
                v3 *= P1;

                v4 += dataHandler.readLong(input + offset + 24) * P2;
                v4 = rotateLeft(v4, 31);
                v4 *= P1;

                offset += 32;
                remaining -= 32;
            } while (remaining >= 32);

            hash = rotateLeft(v1, 1)
                    + rotateLeft(v2, 7)
                    + rotateLeft(v3, 12)
                    + rotateLeft(v4, 18);

            v1 *= P2;
            v1 = rotateLeft(v1, 31);
            v1 *= P1;
            hash ^= v1;
            hash = hash * P1 + P4;

            v2 *= P2;
            v2 = rotateLeft(v2, 31);
            v2 *= P1;
            hash ^= v2;
            hash = hash * P1 + P4;

            v3 *= P2;
            v3 = rotateLeft(v3, 31);
            v3 *= P1;
            hash ^= v3;
            hash = hash * P1 + P4;

            v4 *= P2;
            v4 = rotateLeft(v4, 31);
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
            k1 = rotateLeft(k1, 31);
            k1 *= P1;
            hash ^= k1;
            hash = rotateLeft(hash, 27) * P1 + P4;
            offset += 8;
            remaining -= 8;
        }

        if (remaining >= 4) {
            hash ^= dataHandler.readInt(input + offset) * P1;
            hash = rotateLeft(hash, 23) * P2 + P3;
            offset += 4;
            remaining -= 4;
        }

        while (remaining != 0) {
            hash ^= dataHandler.readUnsignedByte(input + offset) * P5;
            hash = rotateLeft(hash, 11) * P1;
            --remaining;
            ++offset;
        }

        return finalize(hash);
    }

    /*

//  32-bits hash functions

    static const U32 PRIME32_1 = 2654435761U;
    static const U32 PRIME32_2 = 2246822519U;
    static const U32 PRIME32_3 = 3266489917U;
    static const U32 PRIME32_4 =  668265263U;
    static const U32 PRIME32_5 =  374761393U;

    static U32 XXH32_round(U32 seed, U32 input)
    {
        seed += input * PRIME32_2;
        seed  = XXH_rotl32(seed, 13);
        seed *= PRIME32_1;
        return seed;
    }

    FORCE_INLINE U32 XXH32_endian_align(const void* input, size_t len, U32 seed, XXH_endianess endian, XXH_alignment align)
    {
        const BYTE* p = (const BYTE*)input;
        const BYTE* bEnd = p + len;
        U32 h32;
        #define XXH_get32bits(p) XXH_readLE32_align(p, endian, align)

        #ifdef XXH_ACCEPT_NULL_INPUT_POINTER
        if (p==NULL) {
            len=0;
            bEnd=p=(const BYTE*)(size_t)16;
        }
        #endif

        if (len>=16) {
            const BYTE* const limit = bEnd - 16;
            U32 v1 = seed + PRIME32_1 + PRIME32_2;
            U32 v2 = seed + PRIME32_2;
            U32 v3 = seed + 0;
            U32 v4 = seed - PRIME32_1;

            do {
                v1 = XXH32_round(v1, XXH_get32bits(p)); p+=4;
                v2 = XXH32_round(v2, XXH_get32bits(p)); p+=4;
                v3 = XXH32_round(v3, XXH_get32bits(p)); p+=4;
                v4 = XXH32_round(v4, XXH_get32bits(p)); p+=4;
            } while (p<=limit);

            h32 = XXH_rotl32(v1, 1) + XXH_rotl32(v2, 7) + XXH_rotl32(v3, 12) + XXH_rotl32(v4, 18);
        } else {
            h32  = seed + PRIME32_5;
        }

        h32 += (U32) len;

        while (p+4<=bEnd) {
            h32 += XXH_get32bits(p) * PRIME32_3;
            h32  = XXH_rotl32(h32, 17) * PRIME32_4 ;
            p+=4;
        }

        while (p<bEnd) {
            h32 += (*p) * PRIME32_5;
            h32 = XXH_rotl32(h32, 11) * PRIME32_1 ;
            p++;
        }

        h32 ^= h32 >> 15;
        h32 *= PRIME32_2;
        h32 ^= h32 >> 13;
        h32 *= PRIME32_3;
        h32 ^= h32 >> 16;

        return h32;
    }


    XXH_PUBLIC_API unsigned int XXH32 (const void* input, size_t len, unsigned int seed)
    {

        XXH_endianess endian_detected = (XXH_endianess)XXH_CPU_LITTLE_ENDIAN;

        if (XXH_FORCE_ALIGN_CHECK) {
            if ((((size_t)input) & 3) == 0) {
                if ((endian_detected==XXH_littleEndian) || XXH_FORCE_NATIVE_FORMAT)
                    return XXH32_endian_align(input, len, seed, XXH_littleEndian, XXH_aligned);
                else
                    return XXH32_endian_align(input, len, seed, XXH_bigEndian, XXH_aligned);
            }   }

        if ((endian_detected==XXH_littleEndian) || XXH_FORCE_NATIVE_FORMAT)
            return XXH32_endian_align(input, len, seed, XXH_littleEndian, XXH_unaligned);
        else
            return XXH32_endian_align(input, len, seed, XXH_bigEndian, XXH_unaligned);
    }
     */

    private static long finalize(long hash) {
        hash ^= hash >>> 33;
        hash *= P2;
        hash ^= hash >>> 29;
        hash *= P3;
        hash ^= hash >>> 32;
        return hash;
    }
}
