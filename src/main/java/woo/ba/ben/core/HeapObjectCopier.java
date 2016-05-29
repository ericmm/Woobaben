package woo.ba.ben.core;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.System.arraycopy;
import static java.lang.reflect.Array.getLength;
import static java.lang.reflect.Array.newInstance;
import static woo.ba.ben.core.ClassStruct.getObjectClass;
import static woo.ba.ben.core.ImmutableClasses.isImmutable;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public class HeapObjectCopier {
    private static final ConcurrentHashMap<Class, List<Field>> FIELD_CACHE = new ConcurrentHashMap<>();

    private HeapObjectCopier() {
    }

    public static <T> T deepCopy(final T originalObj) {
        if (originalObj == null) {
            return originalObj;
        }

        final Map<Object, Object> objectMap = new IdentityHashMap<>();
        final Class<T> objectClass = getObjectClass(originalObj);
        if (objectClass.isArray()) {
            return copyArray(originalObj, objectClass, objectMap);
        }
        return copyObject(originalObj, objectClass, objectMap);
    }

    private static <T> T copyObject(final T originalObj, final Class<T> objectClass, final Map<Object, Object> objectMap) {
        if (originalObj == null || isImmutable(objectClass)) {
            return originalObj;
        }

        final T targetObject = createNonArrayInstance(originalObj, objectClass, objectMap);
        final ClassStruct classStruct = ClassStructFactory.get(objectClass);
        if (!classStruct.hasInstanceFields()) {
            return targetObject;
        }

        Object attributeInOriginalObj, attributeInTargetObj;
        final FieldStruct[] instanceFields = classStruct.getInstanceFields();
        for (final FieldStruct fieldStruct : instanceFields) {
            if (fieldStruct.isPrimitive()) {
                copyPrimitive(originalObj, targetObject, fieldStruct);
            } else {
                attributeInOriginalObj = UNSAFE.getObject(originalObj, fieldStruct.offset);
                if(attributeInOriginalObj == null) {
                    continue;
                }

                if (fieldStruct.isArray()) {
                    attributeInTargetObj = copyArray(attributeInOriginalObj, getObjectClass(attributeInOriginalObj), objectMap);
                } else {
                    attributeInTargetObj = copyObject(attributeInOriginalObj, getObjectClass(attributeInOriginalObj), objectMap);
                }
                UNSAFE.putObject(targetObject, fieldStruct.offset, attributeInTargetObj);
            }
        }
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

    private static <T> T copyArray(final T arrayObj, final Class<T> arrayClass, final Map<Object, Object> objectMap) {
        if (arrayObj == null) {
            return arrayObj;
        }

        final int length = getLength(arrayObj);
        final T copiedArrayObj = createArrayInstance(arrayObj, length, arrayClass, objectMap);
        if (length == 0) {
            return copiedArrayObj;
        }

        if (arrayClass.getComponentType().isPrimitive()) {
            arraycopy(arrayObj, 0, copiedArrayObj, 0, length);
        } else {
            final Object[] originalArray = (Object[]) arrayObj;
            final Object[] targetArray = (Object[]) copiedArrayObj;
            for (int i = 0; i < length; i++) {
                if(originalArray[i] == null) {
                    continue;
                }
                targetArray[i] = copyObject(originalArray[i], getObjectClass(originalArray[i]), objectMap);
            }
        }
        return copiedArrayObj;
    }

    private static <T> T createArrayInstance(final T originalObj, final int length, final Class<T> objClass, final Map<Object, Object> objectMap) {
        T targetArray = (T) objectMap.get(originalObj);
        if (targetArray == null) {
            final Class componentType = objClass.getComponentType();
            targetArray = (T) newInstance(componentType, length);
            objectMap.put(originalObj, targetArray);
        }
        return targetArray;
    }

    private static <T> T createNonArrayInstance(final T originalObj, final Class<T> objectClass, final Map<Object, Object> objectMap) {
        try {
            T targetObject = (T) objectMap.get(originalObj);
            if (targetObject == null) {
                targetObject = (T) UNSAFE.allocateInstance(objectClass);
                if (!UNSAFE.shouldBeInitialized(objectClass)) {
                    UNSAFE.ensureClassInitialized(objectClass);
                }
                objectMap.put(originalObj, targetObject);
            }
            return targetObject;
        } catch (final InstantiationException e) {
            throw new RuntimeException("Cannot instantiate class:" + objectClass, e);
        }
    }
}
