package woo.ba.ben.bean;

import sun.misc.Unsafe;
import woo.ba.ben.core.ClassStruct;
import woo.ba.ben.core.ClassStructFactory;
import woo.ba.ben.core.FieldStruct;
import woo.ba.ben.core.UnsafeFactory;

import java.util.List;

import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;

public class BeanCopier {
    public static final int BYTE_MASK_INT = 0xFF;
    public static final long BYTE_MASK_LONG = 0xFFL;
    private static final Unsafe UNSAFE = UnsafeFactory.get();

    private static int maskInt(final byte byteValue) {
        return byteValue & BYTE_MASK_INT;
    }

    private static long maskLong(final byte byteValue) {
        return byteValue & BYTE_MASK_LONG;
    }

    private static double readDouble(byte[] buffer, int offset) {
        return Double.longBitsToDouble(readLong(buffer, offset));
    }

    private static long readLong(byte[] buffer, int offset) {
        return (maskLong(buffer[offset]) << 56) |
                (maskLong(buffer[offset + 1]) << 48) |
                (maskLong(buffer[offset + 2]) << 40) |
                (maskLong(buffer[offset + 3]) << 32) |
                (maskLong(buffer[offset + 4]) << 24) |
                (maskLong(buffer[offset + 5]) << 16) |
                (maskLong(buffer[offset + 6]) << 8) |
                maskLong(buffer[offset + 7]);
    }

    private static float readFloat(final byte[] buffer, final int offset) {
        return Float.intBitsToFloat(readInt(buffer, offset));
    }

    private static int readInt(final byte[] buffer, final int offset) {
        return (maskInt(buffer[offset]) << 24) |
                (maskInt(buffer[offset + 1]) << 16) |
                (maskInt(buffer[offset + 2]) << 8) |
                maskInt(buffer[offset + 3]);
    }

    private static short readShort(final byte[] buffer, final int offset) {
        return (short) ((maskInt(buffer[offset]) << 8) | maskInt(buffer[offset + 1]));
    }

    private static char readChar(final byte[] buffer, final int offset) {
        return (char) ((maskInt(buffer[offset]) << 8) | maskInt(buffer[offset + 1]));
    }

    private static byte readByte(final byte[] buffer, final int offset) {
        return (byte) maskInt(buffer[offset]);
    }

    private static boolean readBoolean(final byte[] buffer, final int offset) {
        return maskInt(buffer[offset]) != 0;
    }

    public Object copyBean(final Object originalObj) throws InstantiationException {
        if (originalObj == null || originalObj instanceof Class) {
            return originalObj;
        }

        final Class<?> objClass = originalObj.getClass();
        final ClassStruct classStruct = ClassStructFactory.get(objClass);

        final Object objectInstance = createInstance(objClass);
        if (!classStruct.hasInstanceFields()) {
            return objectInstance;
        }

        final long startInstanceOffset = classStruct.getInstanceFieldStartOffset();
        final long instanceFieldsSize = classStruct.getInstanceFieldEndOffset() - startInstanceOffset;
        final byte[] buffer = new byte[(int) instanceFieldsSize];
        UNSAFE.copyMemory(originalObj, startInstanceOffset, buffer, ARRAY_BYTE_BASE_OFFSET, instanceFieldsSize);

        copyValuesFromBuffer(objectInstance, classStruct, buffer);

        return objectInstance;
    }

    private void copyValuesFromBuffer(final Object objectInstance, final ClassStruct classStruct, final byte[] buffer) {
        final List<FieldStruct> sortedInstanceFields = classStruct.getSortedInstanceFields();
        final long firstOffset = sortedInstanceFields.get(0).offset;
        for (final FieldStruct fieldStruct : sortedInstanceFields) {
            if (fieldStruct.type == byte.class) {
                UNSAFE.putByte(objectInstance, fieldStruct.offset, readByte(buffer, (int) (fieldStruct.offset - firstOffset)));
            } else if (fieldStruct.type == boolean.class) {
                UNSAFE.putBoolean(objectInstance, fieldStruct.offset, readBoolean(buffer, (int) (fieldStruct.offset - firstOffset)));
            } else if (fieldStruct.type == char.class) {
                UNSAFE.putChar(objectInstance, fieldStruct.offset, readChar(buffer, (int) (fieldStruct.offset - firstOffset)));
            } else if (fieldStruct.type == short.class) {
                UNSAFE.putShort(objectInstance, fieldStruct.offset, readShort(buffer, (int) (fieldStruct.offset - firstOffset)));
            } else if (fieldStruct.type == float.class) {
                UNSAFE.putFloat(objectInstance, fieldStruct.offset, readFloat(buffer, (int) (fieldStruct.offset - firstOffset)));
            } else if (fieldStruct.type == int.class) {
                UNSAFE.putInt(objectInstance, fieldStruct.offset, readInt(buffer, (int) (fieldStruct.offset - firstOffset)));
            } else if (fieldStruct.type == long.class) {
                UNSAFE.putLong(objectInstance, fieldStruct.offset, readLong(buffer, (int) (fieldStruct.offset - firstOffset)));
            } else if (fieldStruct.type == double.class) {
                UNSAFE.putDouble(objectInstance, fieldStruct.offset, readDouble(buffer, (int) (fieldStruct.offset - firstOffset)));
            } else {
                //UNSAFE.ARRAY_OBJECT_INDEX_SCALE == UNSAFE.ARRAY_INT_INDEX_SCALE
                UNSAFE.putInt(objectInstance, fieldStruct.offset, readInt(buffer, (int) (fieldStruct.offset - firstOffset)));

                //object class
                if (fieldStruct.type.isArray()) {
                    final Class componentType = fieldStruct.type.getComponentType();
                    if (componentType == byte[].class) {

                    } else if (componentType == boolean[].class) {

                    } else if (componentType == char[].class) {

                    } else if (componentType == short[].class) {

                    } else if (componentType == float[].class) {

                    } else if (componentType == int[].class) {

                    } else if (componentType == long[].class) {

                    } else if (componentType == double[].class) {

                    } else {

                    }
                } else {
                    //normal Object
                }
            }
        }
    }

    private Object createInstance(final Class<?> objClass) throws InstantiationException {
        final Object objectInstance = UNSAFE.allocateInstance(objClass);
        if (!UNSAFE.shouldBeInitialized(objClass)) {
            UNSAFE.ensureClassInitialized(objClass);
        }
        return objectInstance;
    }
}
