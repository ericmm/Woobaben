package woo.ba.ben.util;


import com.google.common.primitives.Longs;

import static woo.ba.ben.util.DataReader.readLong;

public class Util {
    private Util() {
    }

    public static void checkNotNull(final Object... args) {
        if (args == null) {
            throw new IllegalArgumentException("Should pass at least one parameter!");
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                throw new NullPointerException("The " + (i + 1) + " parameter is null!");
            }
        }
    }

    public static long nextPowerOfTwo(long x) {
        if (x == 0) return 1;
        x--;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return (x | x >> 32) + 1;
    }

    public boolean arrayEquals(final byte[] left, final byte[] right) {
        if (left == right) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }

        if (right.length != left.length) {
            return false;
        }

        //TODO: check CPU 32-bit or 64-bit, that determines (LittleEndian or BigEndian) & (Integer or Long)
        final int loopTimes = left.length / Longs.BYTES;
        final int loopLength = loopTimes * Longs.BYTES;
        for (int offset = 0; offset < loopLength; offset += Longs.BYTES) {
            if ((readLong(left, offset) ^ readLong(right, offset)) != 0) {
                return false;
            }
        }

        // for remaining
        for (int i = loopLength; i < left.length; i++) {
            if (left[i] != right[i]) {
                return false;
            }
        }

        return true;
    }
}
