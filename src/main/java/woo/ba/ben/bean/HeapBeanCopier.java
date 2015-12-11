package woo.ba.ben.bean;

import sun.misc.Unsafe;
import woo.ba.ben.core.*;

import java.util.List;

import static java.lang.System.arraycopy;
import static java.lang.System.identityHashCode;

public class HeapBeanCopier {
    private static final Unsafe UNSAFE = UnsafeFactory.get();

    private HeapBeanCopier() {
    }

    public static Object copyBean(final Object originalObj) throws InstantiationException {
        if (originalObj == null || originalObj instanceof Class) {
            return originalObj;
        }

        final Class<?> objClass = originalObj.getClass();
        final Object targetObject = createInstance(objClass);
        final ClassStruct classStruct = ClassStructFactory.get(objClass);
        if (!classStruct.hasInstanceFields()) {
            return targetObject;
        }

        final SimpleMap<Integer, Object> objectMap = new SimpleArrayMap<>();
        copyObject(originalObj, targetObject, objClass, objectMap);
        return targetObject;
    }

    private static void copyObject(final Object originalObj, final Object targetObject, final Class<?> objClass, final SimpleMap<Integer, Object> objectMap) throws InstantiationException {
        final ClassStruct classStruct = ClassStructFactory.get(objClass);

        final List<FieldStruct> sortedInstanceFields = classStruct.getSortedInstanceFields();
        for (final FieldStruct fieldStruct : sortedInstanceFields) {
            if (fieldStruct.type.isPrimitive()) {
                copyPrimitiveAttribute(originalObj, targetObject, fieldStruct);
            } else if (fieldStruct.type.isArray()) {
                copyArrayAttribute(originalObj, targetObject, fieldStruct);
            } else {
                copyObjectAttribute(originalObj, targetObject, fieldStruct, objectMap);
            }
        }
    }

    private static void copyPrimitiveAttribute(final Object originalObj, final Object targetObject, final FieldStruct fieldStruct) {
        if (fieldStruct.type == byte.class) {
            UNSAFE.putByte(targetObject, fieldStruct.offset, UNSAFE.getByte(originalObj, fieldStruct.offset));
        } else if (fieldStruct.type == boolean.class) {
            UNSAFE.putBoolean(targetObject, fieldStruct.offset, UNSAFE.getBoolean(originalObj, fieldStruct.offset));
        } else if (fieldStruct.type == char.class) {
            UNSAFE.putChar(targetObject, fieldStruct.offset, UNSAFE.getChar(originalObj, fieldStruct.offset));
        } else if (fieldStruct.type == short.class) {
            UNSAFE.putShort(targetObject, fieldStruct.offset, UNSAFE.getShort(originalObj, fieldStruct.offset));
        } else if (fieldStruct.type == float.class) {
            UNSAFE.putFloat(targetObject, fieldStruct.offset, UNSAFE.getFloat(originalObj, fieldStruct.offset));
        } else if (fieldStruct.type == int.class) {
            UNSAFE.putInt(targetObject, fieldStruct.offset, UNSAFE.getInt(originalObj, fieldStruct.offset));
        } else if (fieldStruct.type == long.class) {
            UNSAFE.putLong(targetObject, fieldStruct.offset, UNSAFE.getLong(originalObj, fieldStruct.offset));
        } else if (fieldStruct.type == double.class) {
            UNSAFE.putDouble(targetObject, fieldStruct.offset, UNSAFE.getDouble(originalObj, fieldStruct.offset));
        }
    }

    private static void copyArrayAttribute(final Object originalObj, final Object targetObject, final FieldStruct fieldStruct) {
        final Object arrayInOriginalObj = UNSAFE.getObject(originalObj, fieldStruct.offset);
        final Object targetArrayAttribute = createArrayInstance(arrayInOriginalObj, fieldStruct);
        UNSAFE.putObject(targetObject, fieldStruct.offset, targetArrayAttribute);
    }

    private static Object createArrayInstance(final Object arrayInOriginalObj, final FieldStruct fieldStruct) {
        if (arrayInOriginalObj == null) {
            return null;
        }

        final Object targetArray;
        if (fieldStruct.type == byte[].class) {
            final byte[] originalArray = (byte[]) arrayInOriginalObj;
            targetArray = new byte[originalArray.length];
            arraycopy(originalArray, 0, targetArray, 0, originalArray.length);
        } else if (fieldStruct.type == boolean[].class) {
            final boolean[] originalArray = (boolean[]) arrayInOriginalObj;
            targetArray = new boolean[originalArray.length];
            arraycopy(originalArray, 0, targetArray, 0, originalArray.length);
        } else if (fieldStruct.type == char[].class) {
            final char[] originalArray = (char[]) arrayInOriginalObj;
            targetArray = new char[originalArray.length];
            arraycopy(originalArray, 0, targetArray, 0, originalArray.length);
        } else if (fieldStruct.type == short[].class) {
            final short[] originalArray = (short[]) arrayInOriginalObj;
            targetArray = new short[originalArray.length];
            arraycopy(originalArray, 0, targetArray, 0, originalArray.length);
        } else if (fieldStruct.type == float[].class) {
            final float[] originalArray = (float[]) arrayInOriginalObj;
            targetArray = new float[originalArray.length];
            arraycopy(originalArray, 0, targetArray, 0, originalArray.length);
        } else if (fieldStruct.type == int[].class) {
            final int[] originalArray = (int[]) arrayInOriginalObj;
            targetArray = new int[originalArray.length];
            arraycopy(originalArray, 0, targetArray, 0, originalArray.length);
        } else if (fieldStruct.type == long[].class) {
            final long[] originalArray = (long[]) arrayInOriginalObj;
            targetArray = new long[originalArray.length];
            arraycopy(originalArray, 0, targetArray, 0, originalArray.length);
        } else if (fieldStruct.type == double[].class) {
            final double[] originalArray = (double[]) arrayInOriginalObj;
            targetArray = new double[originalArray.length];
            arraycopy(originalArray, 0, targetArray, 0, originalArray.length);
        } else {
            //Object[]
            final Object[] originalArray = (Object[]) arrayInOriginalObj;
            targetArray = new Object[originalArray.length];
            arraycopy(originalArray, 0, targetArray, 0, originalArray.length);
        }
        return targetArray;
    }

    private static void copyObjectAttribute(final Object originalObj, final Object targetObject, final FieldStruct fieldStruct, final SimpleMap<Integer, Object> objectMap) throws InstantiationException {
        final Object originalObjAttribute = UNSAFE.getObject(originalObj, fieldStruct.offset);
        final Integer originalObjAttributeId = identityHashCode(originalObjAttribute);

        Object targetObjectAttribute = objectMap.get(originalObjAttributeId);
        if (targetObjectAttribute == null) {
            targetObjectAttribute = createInstance(fieldStruct.type);
            copyObject(originalObjAttribute, targetObjectAttribute, fieldStruct.type, objectMap);
            objectMap.put(originalObjAttributeId, targetObjectAttribute);
        }
        UNSAFE.putObject(targetObject, fieldStruct.offset, targetObjectAttribute);
    }

//    private byte[] copyValuesToBuffer(final Object originalObj, final ClassStruct classStruct) {
//        if (classStruct.realClass.isArray()) {
//
//        }
//        //normal object
//        final long firstInstanceFieldStartPosition = classStruct.getInstanceFieldBlockStartPosition();
//        final long instanceFieldsSize = classStruct.getInstanceFieldBlockEndPosition() - firstInstanceFieldStartPosition;
//        final byte[] buffer = new byte[(int) instanceFieldsSize];
//        UNSAFE.copyMemory(originalObj, firstInstanceFieldStartPosition, buffer, ARRAY_BYTE_BASE_OFFSET, instanceFieldsSize);
//        return buffer;
//    }

    private static Object createInstance(final Class<?> objClass) throws InstantiationException {
        final Object objectInstance = UNSAFE.allocateInstance(objClass);
        if (!UNSAFE.shouldBeInitialized(objClass)) {
            UNSAFE.ensureClassInitialized(objClass);
        }
        return objectInstance;
    }
}
