package woo.ba.ben.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.System.arraycopy;
import static java.lang.System.identityHashCode;
import static java.lang.reflect.Array.getLength;
import static java.lang.reflect.Array.newInstance;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public class HeapObjectCopier {
    private static final Set<Class> UNSUPPORTED_CLASSES_SET = new HashSet<>();

    static {
        UNSUPPORTED_CLASSES_SET.add(Class.class);
        UNSUPPORTED_CLASSES_SET.add(Enum.class);
        UNSUPPORTED_CLASSES_SET.add(Annotation.class);
        UNSUPPORTED_CLASSES_SET.add(Field.class);
        UNSUPPORTED_CLASSES_SET.add(System.class);
        UNSUPPORTED_CLASSES_SET.add(HeapObjectCopier.class);
    }

    private HeapObjectCopier() {
    }

    public static boolean addSingletonClass(final Class singletonClass) {
        return UNSUPPORTED_CLASSES_SET.add(singletonClass);
    }

    public static <T> T deepCopy(final T originalObj) {
        if (originalObj == null) {
            return originalObj;
        }

        final Map<Integer, Object> objectMap = new HashMap<>();
        if (originalObj.getClass().isArray()) {
            return copyArray(originalObj, objectMap);
        }
        return copyObject(originalObj, objectMap);
    }

    private static <T> boolean isCloneable(final T originalObj) {
        return !UNSUPPORTED_CLASSES_SET.contains(originalObj.getClass());
    }

    private static <T> T copyObject(final T originalObj, final Map<Integer, Object> objectMap) {
        if (originalObj == null || !isCloneable(originalObj)) {
            return originalObj;
        }

        final T targetObject = createInstance(originalObj, objectMap);
        final ClassStruct classStruct = ClassStructFactory.get(originalObj.getClass());
        if (!classStruct.hasInstanceFields()) {
            return targetObject;
        }

        Object attributeInOriginalObj, attributeInTargetObj;
//        for (final FieldStruct fieldStruct : classStruct.getSortedInstanceFields()) {
//            if (fieldStruct.type.isPrimitive()) {
//                copyPrimitive(originalObj, targetObject, fieldStruct);
//            } else {
//                attributeInOriginalObj = UNSAFE.getObject(originalObj, fieldStruct.offset);
//                if (fieldStruct.type.isArray()) {
//                    attributeInTargetObj = copyArray(attributeInOriginalObj, objectMap);
//                } else {
//                    attributeInTargetObj = copyObject(attributeInOriginalObj, objectMap);
//                }
//                UNSAFE.putObject(targetObject, fieldStruct.offset, attributeInTargetObj);
//            }
//        }
        return targetObject;
    }

    private static <T> void copyPrimitive(final T originalObj, final T targetObject, final FieldStruct fieldStruct) {
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

    private static <T> T copyArray(final T arrayObj, final Map<Integer, Object> objectMap) {
        if (arrayObj == null) {
            return arrayObj;
        }

        final T copiedArrayObj = createInstance(arrayObj, objectMap);
        final int length = getLength(arrayObj);
        if (length == 0) {
            return copiedArrayObj;
        }

        if (arrayObj.getClass().getComponentType().isPrimitive()) {
            arraycopy(arrayObj, 0, copiedArrayObj, 0, length);
        } else {
            final Object[] originalArray = (Object[]) arrayObj;
            final Object[] targetArray = (Object[]) copiedArrayObj;
            for (int i = 0; i < length; i++) {
                targetArray[i] = copyObject(originalArray[i], objectMap);
                ;
            }
        }
        return copiedArrayObj;
    }

    private static <T> T createInstance(final T originalObj, final Map<Integer, Object> objectMap) {
        final Integer originalObjId = identityHashCode(originalObj);
        T targetObject = (T) objectMap.get(originalObjId);
        if (targetObject == null) {
            final Class<T> objClass = (Class<T>) originalObj.getClass();
            if (objClass.isArray()) {
                targetObject = createArrayInstance(originalObj, objClass);
            } else {
                targetObject = createNonArrayInstance(objClass);
            }
            objectMap.put(originalObjId, targetObject);
        }
        return targetObject;
    }

    private static <T> T createArrayInstance(final T originalObj, final Class<T> objClass) {
        final Class componentType = objClass.getComponentType();
        final int length = getLength(originalObj);
        return (T) newInstance(componentType, length);
    }

    private static <T> T createNonArrayInstance(final Class<T> objClass) {
        try {
            final T objectInstance = (T) UNSAFE.allocateInstance(objClass);
            if (!UNSAFE.shouldBeInitialized(objClass)) {
                UNSAFE.ensureClassInitialized(objClass);
            }
            return objectInstance;
        } catch (final InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
