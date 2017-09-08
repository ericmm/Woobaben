package woo.ba.ben.core;


import sun.misc.Unsafe;

import java.lang.reflect.Constructor;

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.nativeOrder;

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
            IS_NATIVE_ORDER_BIG_ENDIAN = BIG_ENDIAN.equals(nativeOrder());
        } catch (final Exception e) {
            throw new RuntimeException("Cannot get the Unsafe instance!", e);
        }
    }

    private UnsafeFactory() {
    }

//    public static int getTypeSize(final Class classType) {
//        return UNSAFE.arrayIndexScale(classType);
//    }

    public static int getTypeSize(final Class classType) {
        if (!classType.isPrimitive()) {
            return UNSAFE.ARRAY_OBJECT_INDEX_SCALE;
        } else if (classType == int.class) {
            return UNSAFE.ARRAY_INT_INDEX_SCALE;
        } else if (classType == long.class) {
            return UNSAFE.ARRAY_LONG_INDEX_SCALE;
        } else if (classType == boolean.class) {
            return UNSAFE.ARRAY_BOOLEAN_INDEX_SCALE;
        } else if (classType == double.class) {
            return UNSAFE.ARRAY_DOUBLE_INDEX_SCALE;
        } else if (classType == float.class) {
            return UNSAFE.ARRAY_FLOAT_INDEX_SCALE;
        } else if (classType == byte.class) {
            return UNSAFE.ARRAY_BYTE_INDEX_SCALE;
        } else if (classType == char.class) {
            return UNSAFE.ARRAY_CHAR_INDEX_SCALE;
        } else {
            return UNSAFE.ARRAY_SHORT_INDEX_SCALE;
        }
    }
}
