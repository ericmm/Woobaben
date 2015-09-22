package woo.ba.ben.core;


import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class UnsafeFactory {
    public static final int ADDRESS_SIZE;
    public static final int OBJECT_REF_SIZE;
    private static final Unsafe unsafe;

    static {
        try {
            Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
            unsafeConstructor.setAccessible(true);
            unsafe = unsafeConstructor.newInstance();

            ADDRESS_SIZE = unsafe.addressSize();
            OBJECT_REF_SIZE = unsafe.arrayIndexScale(Object[].class);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot get the Unsafe instance!", e);
        }
    }

    private UnsafeFactory() {
    }

    public static Unsafe get() throws Exception {
        return unsafe;
    }
}
