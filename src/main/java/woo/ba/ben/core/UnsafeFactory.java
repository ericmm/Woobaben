package woo.ba.ben.core;


import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.nio.ByteOrder;

public class UnsafeFactory {
    public static final int ADDRESS_SIZE;
    public static final int OBJECT_REF_SIZE;
    public static final ByteOrder SYSTEM_BYTE_ORDER = ByteOrder.nativeOrder();

    private static final Unsafe UNSAFE;

    static {
        try {
            Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
            unsafeConstructor.setAccessible(true);
            UNSAFE = unsafeConstructor.newInstance();

            ADDRESS_SIZE = UNSAFE.addressSize();
            OBJECT_REF_SIZE = UNSAFE.arrayIndexScale(Object[].class);
        } catch (Exception e) {
            throw new RuntimeException("Cannot get the Unsafe instance!", e);
        }
    }

    private UnsafeFactory() {
    }

    public static Unsafe get() {
        return UNSAFE;
    }
}
