package woo.ba.ben.util;


public class Util {
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
}
