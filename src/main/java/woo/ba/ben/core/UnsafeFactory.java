package woo.ba.ben.core;


import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.nio.ByteOrder;

import static java.nio.ByteOrder.BIG_ENDIAN;

class UnsafeFactory {
    public static final boolean IS_64_BIT;
    public static final boolean IS_NATIVE_ORDER_BIG_ENDIAN;
    public static final int OBJECT_REF_SIZE;
    public static final Unsafe UNSAFE;

    static {
        try {
            final Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
            unsafeConstructor.setAccessible(true);
            UNSAFE = unsafeConstructor.newInstance();

            OBJECT_REF_SIZE = UNSAFE.arrayIndexScale(Object[].class);
            IS_64_BIT = UNSAFE.addressSize() == Long.SIZE;
            IS_NATIVE_ORDER_BIG_ENDIAN = BIG_ENDIAN.equals(ByteOrder.nativeOrder());
        } catch (final Exception e) {
            throw new RuntimeException("Cannot get the Unsafe instance!", e);
        }
    }

    private UnsafeFactory() {
    }

    public static int getTypeSize(final Class classType) {
        return UNSAFE.arrayIndexScale(classType);
    }
}
