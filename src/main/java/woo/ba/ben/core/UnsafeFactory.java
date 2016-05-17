package woo.ba.ben.core;


import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.nio.ByteOrder;

import static java.nio.ByteOrder.BIG_ENDIAN;

class UnsafeFactory {
    static final boolean IS_64_BIT;
    static final boolean IS_NATIVE_ORDER_BIG_ENDIAN;
    static final int ADDRESS_SIZE;
    static final int OBJECT_REF_SIZE;
    static final Unsafe UNSAFE;

    static {
        try {
            final Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
            unsafeConstructor.setAccessible(true);
            UNSAFE = unsafeConstructor.newInstance();

            ADDRESS_SIZE = UNSAFE.addressSize();
            OBJECT_REF_SIZE = UNSAFE.arrayIndexScale(Object[].class);
            IS_64_BIT = (ADDRESS_SIZE == Long.SIZE);
            IS_NATIVE_ORDER_BIG_ENDIAN = BIG_ENDIAN.equals(ByteOrder.nativeOrder());
        } catch (final Exception e) {
            throw new RuntimeException("Cannot get the Unsafe instance!", e);
        }
    }

    private UnsafeFactory() {
    }

    static int getTypeSize(final Class classType) {
        return UNSAFE.arrayIndexScale(classType);
    }
}
