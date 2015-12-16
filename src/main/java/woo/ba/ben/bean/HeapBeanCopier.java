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
        UNSUPPORTED_CLASS_SET.add(System.class);
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

        final T targetObject = createInstance(originalObj, objectMap);
        final ClassStruct classStruct = ClassStructFactory.get(originalObj.getClass());
        if (!classStruct.hasInstanceFields()) {
            return targetObject;
        }

        Object attributeInOriginalObj, attributeInTargetObj;
        for (final FieldStruct fieldStruct : classStruct.getSortedInstanceFields()) {
            if (fieldStruct.type.isPrimitive()) {
                copyPrimitive(originalObj, targetObject, fieldStruct);
            } else {
                attributeInOriginalObj = UNSAFE.getObject(originalObj, fieldStruct.offset);
                if (fieldStruct.type.isArray()) {
                    attributeInTargetObj = copyArray(attributeInOriginalObj, objectMap);
                } else {
                    attributeInTargetObj = copyObject(attributeInOriginalObj, objectMap);
                }
                UNSAFE.putObject(targetObject, fieldStruct.offset, attributeInTargetObj);
            }
        }
        return targetObject;
    }

    private static void copyPrimitive(final Object originalObj, final Object targetObject, final FieldStruct fieldStruct) {
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

    private static Object copyArray(final Object arrayInOriginalObj, final SimpleMap<Integer, Object> objectMap) throws InstantiationException {
        if(arrayInOriginalObj == null) {
            return arrayInOriginalObj;
        }

        final Object arrayInTargetObj = createInstance(arrayInOriginalObj, objectMap);
        final int length = getLength(arrayInOriginalObj);
        if(length == 0) {
            return arrayInTargetObj;
        }

        final Class arrayClass = arrayInOriginalObj.getClass();
        if (!arrayClass.getComponentType().isPrimitive()) {
            final Object[] originalArray = (Object[]) arrayInOriginalObj;
            final Object[] targetArray = (Object[]) arrayInTargetObj;

            Object sourceObjElement, targetObjElement;
            for (int i = 0; i < length; i++) {
                sourceObjElement = originalArray[i];
                targetObjElement = copyObject(sourceObjElement, objectMap);
                targetArray[i] = targetObjElement;
            }
        } else {
            arraycopy(arrayInOriginalObj, 0, arrayInTargetObj, 0, length);
        }
        return arrayInTargetObj;
    }

    private static <T> T createInstance(final T originalObj, final SimpleMap<Integer, Object> objectMap) throws InstantiationException {
        final Integer originalObjId = identityHashCode(originalObj);
        T targetObject = (T) objectMap.get(originalObjId);
        if (targetObject == null) {
            Class<T> objClass = (Class<T>) originalObj.getClass();
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

    private static <T> T createNonArrayInstance(final Class<T> objClass) throws InstantiationException {
        final T objectInstance = (T) UNSAFE.allocateInstance(objClass);
        if (!UNSAFE.shouldBeInitialized(objClass)) {
            UNSAFE.ensureClassInitialized(objClass);
        }
        return objectInstance;
    }
}
