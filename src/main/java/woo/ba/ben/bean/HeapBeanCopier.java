package woo.ba.ben.bean;

import sun.misc.Unsafe;
import woo.ba.ben.core.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static java.lang.System.arraycopy;
import static java.lang.System.identityHashCode;
import static java.lang.reflect.Array.getLength;
import static java.lang.reflect.Array.newInstance;

public class HeapBeanCopier {
    private static final Unsafe UNSAFE = UnsafeFactory.get();
    private static final Set<Class> UNSUPPORTED_CLASS_SET = new HashSet<>();

    static {
        UNSUPPORTED_CLASS_SET.add(Class.class);
        UNSUPPORTED_CLASS_SET.add(Enum.class);
        UNSUPPORTED_CLASS_SET.add(Annotation.class);
        UNSUPPORTED_CLASS_SET.add(Field.class);
    }

    private HeapBeanCopier() {
    }

    public static <T> T copyBean(final T originalObj) throws InstantiationException {
        final SimpleMap<Integer, Object> objectMap = new SimpleArrayMap<>();
        return copyObject(originalObj, objectMap);
    }

    private static <T> boolean isCloneable(final T originalObj) {
        for (final Class clazz : UNSUPPORTED_CLASS_SET) {
            if (clazz.isInstance(originalObj)) {
                return false;
            }
        }

        final Class<T> clz = (Class<T>) originalObj.getClass();
        if (clz.equals(HeapBeanCopier.class)) {
            return false;
        }

        return true;
    }

    private static <T> T copyObject(final T originalObj, final SimpleMap<Integer, Object> objectMap) throws InstantiationException {
        if (originalObj == null || !isCloneable(originalObj)) {
            return originalObj;
        }

        final Class<T> objClass = (Class<T>) originalObj.getClass();
        final T targetObject = createInstance(originalObj, objClass);
        final ClassStruct classStruct = ClassStructFactory.get(objClass);
        if (!classStruct.hasInstanceFields()) {
            return targetObject;
        }

        for (final FieldStruct fieldStruct : classStruct.getSortedInstanceFields()) {
            if (fieldStruct.type.isPrimitive()) {
                copyPrimitiveAttribute(originalObj, targetObject, fieldStruct);
            } else if (fieldStruct.type.isArray()) {
                copyArrayAttribute(originalObj, targetObject, fieldStruct, objectMap);
            } else {
                copyObjectAttribute(originalObj, targetObject, fieldStruct, objectMap);
            }
        }
        return targetObject;
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

    private static void copyArrayAttribute(final Object originalObj, final Object targetObject, final FieldStruct fieldStruct, final SimpleMap<Integer, Object> objectMap) throws InstantiationException {
        final Object arrayInOriginalObj = UNSAFE.getObject(originalObj, fieldStruct.offset);
        if (arrayInOriginalObj == null) {
            UNSAFE.putObject(targetObject, fieldStruct.offset, null);
            return;
        }

        final Integer originalArrayAttributeId = identityHashCode(arrayInOriginalObj);
        Object targetArrayAttribute = objectMap.get(originalArrayAttributeId);
        if (targetArrayAttribute == null) {
            targetArrayAttribute = copyArray(arrayInOriginalObj, fieldStruct, objectMap);
            objectMap.put(originalArrayAttributeId, targetArrayAttribute);
        }
        UNSAFE.putObject(targetObject, fieldStruct.offset, targetArrayAttribute);
    }

    private static Object copyArray(final Object arrayInOriginalObj, final FieldStruct fieldStruct, final SimpleMap<Integer, Object> objectMap) throws InstantiationException {
        final Object targetArrayObj = createInstance(arrayInOriginalObj, fieldStruct.type);
        final Class componentType = fieldStruct.type.getComponentType();
        final int length = getLength(arrayInOriginalObj);
        if (!componentType.isPrimitive()) {
            final Object[] originalArray = (Object[]) arrayInOriginalObj;
            final Object[] targetArray = (Object[]) targetArrayObj;

            Object sourceObjElement, targetObjElement;
            for (int i = 0; i < length; i++) {
                sourceObjElement = originalArray[i];
                targetObjElement = copyObject(sourceObjElement, objectMap);
                targetArray[i] = targetObjElement;
            }
        } else {
            arraycopy(arrayInOriginalObj, 0, targetArrayObj, 0, length);
        }
        return targetArrayObj;
    }

    private static void copyObjectAttribute(final Object originalObj, final Object targetObject, final FieldStruct fieldStruct, final SimpleMap<Integer, Object> objectMap) throws InstantiationException {
        final Object originalObjAttribute = UNSAFE.getObject(originalObj, fieldStruct.offset);
        if (originalObjAttribute == null) {
            UNSAFE.putObject(targetObject, fieldStruct.offset, null);
            return;
        }

        final Integer originalObjAttributeId = identityHashCode(originalObjAttribute);
        Object targetObjectAttribute = objectMap.get(originalObjAttributeId);
        if (targetObjectAttribute == null) {
            targetObjectAttribute = copyObject(originalObjAttribute, objectMap);
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

    private static <T> T createInstance(final T originalObj, final Class<T> objClass) throws InstantiationException {
        if (objClass.isArray()) {
            return createArrayInstance(originalObj, objClass);
        }
        return createNonArrayInstance(objClass);
    }

    private static <T> T createArrayInstance(final T originalObj, final Class<T> objClass) {
        final Class componentType = objClass.getComponentType();
        final int length = getLength(originalObj);
        return (T) newInstance(componentType, length);
    }

    private static <T> T createNonArrayInstance(final Class<T> objClass) throws InstantiationException {
        final T objectInstance = (T) UNSAFE.allocateInstance(objClass);
        if (!UNSAFE.shouldBeInitialized(objClass)) {
            UNSAFE.ensureClassInitialized(objClass);
        }
        return objectInstance;
    }
}
