package woo.ba.ben.core;


import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteOrder;

public class UnsafeFactory {
    private static Unsafe unsafe = null;
    public static int ADDRESS_SIZE = 0;

    static {
        try {
            Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
            unsafeConstructor.setAccessible(true);
            unsafe = unsafeConstructor.newInstance();

            ADDRESS_SIZE = unsafe.addressSize();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot get the Unsafe instance!", e);
        }
    }

    private UnsafeFactory() {
    }

    public static Unsafe get() throws Exception {
        return unsafe;
    }

    public static boolean isBigEndian() {
        return ByteOrder.BIG_ENDIAN.equals(ByteOrder.nativeOrder());
    }

}
