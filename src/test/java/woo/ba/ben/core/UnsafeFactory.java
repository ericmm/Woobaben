package woo.ba.ben.core;


import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeFactory {
    private static Unsafe unsafe = null;
    private static Exception exception = null;

    static {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            exception = e;
        }
    }

    private UnsafeFactory() {
    }

    public static Unsafe get() throws Exception {
        if (unsafe == null) {
            throw new Exception("Cannot get Unsafe instance!", exception);
        }
        return unsafe;
    }

    public static Unsafe getSafely() {
        return unsafe;
    }
}
