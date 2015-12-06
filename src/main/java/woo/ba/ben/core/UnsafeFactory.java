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
            final Constructor<Unsafe> unsafeConstructor = Unsafe.class.getDeclaredConstructor();
            unsafeConstructor.setAccessible(true);
            UNSAFE = unsafeConstructor.newInstance();

            ADDRESS_SIZE = UNSAFE.addressSize();
            OBJECT_REF_SIZE = UNSAFE.arrayIndexScale(Object[].class);
        } catch (final Exception e) {
            throw new RuntimeException("Cannot get the Unsafe instance!", e);
        }
    }

    private UnsafeFactory() {
    }

    public static Unsafe get() {
        return UNSAFE;
    }

    public static int getTypeSize(final Class classType) {
        if (classType == null) {
            return 0;
        }

        if (boolean.class == classType) {
            return UNSAFE.ARRAY_BOOLEAN_INDEX_SCALE;
        } else if (byte.class == classType) {
            return UNSAFE.ARRAY_BYTE_INDEX_SCALE;
        } else if (short.class == classType) {
            return UNSAFE.ARRAY_SHORT_INDEX_SCALE;
        } else if (char.class == classType) {
            return UNSAFE.ARRAY_CHAR_INDEX_SCALE;
        } else if (int.class == classType) {
            return UNSAFE.ARRAY_INT_INDEX_SCALE;
        } else if (float.class == classType) {
            return UNSAFE.ARRAY_FLOAT_INDEX_SCALE;
        } else if (long.class == classType) {
            return UNSAFE.ARRAY_LONG_INDEX_SCALE;
        } else if (double.class == classType) {
            return UNSAFE.ARRAY_DOUBLE_INDEX_SCALE;
        } else {
            return UNSAFE.ARRAY_OBJECT_INDEX_SCALE;
        }
    }
}
