package woo.ba.ben.util;


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
}
