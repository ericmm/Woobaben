package woo.ba.ben.core;

import java.util.IdentityHashMap;
import java.util.Map;

import static java.lang.System.arraycopy;
import static java.lang.reflect.Array.getLength;
import static java.lang.reflect.Array.newInstance;
import static woo.ba.ben.core.ClassStruct.classStruct;
import static woo.ba.ben.core.ClassStruct.getObjectClass;
import static woo.ba.ben.core.ImmutableClasses.isImmutable;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public class HeapObjectCopier {
    private HeapObjectCopier() {
    }

    public static <T> T deepCopy(final T originalObj) {
        if (originalObj == null) {
            return null;
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

        final T targetObject = createNonArrayInstanceIfNeeded(originalObj, objectClass, objectMap);
        final ClassStruct classStruct = classStruct(objectClass);
        if (!classStruct.hasInstanceFields()) {
            return targetObject;
        }

        Object attributeInOriginalObj, attributeInTargetObj;
        for (final FieldStruct fieldStruct : classStruct.getInstanceFields()) {
            if (fieldStruct.isPrimitive()) {
                copyPrimitive(originalObj, targetObject, fieldStruct);
            } else {
                attributeInOriginalObj = UNSAFE.getObject(originalObj, fieldStruct.offset);
                if (attributeInOriginalObj == null) {
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
            return null;
        }

        final int length = getLength(arrayObj);
        final T copiedArrayObj = createArrayInstanceIfNeeded(arrayObj, length, arrayClass, objectMap);
        if (length == 0) {
            return copiedArrayObj;
        }

        if (arrayClass.getComponentType().isPrimitive()) {
            arraycopy(arrayObj, 0, copiedArrayObj, 0, length);
        } else {
            final Object[] originalArray = (Object[]) arrayObj;
            final Object[] targetArray = (Object[]) copiedArrayObj;
            Object objInArray;
            Class objectClass;
            for (int i = 0; i < length; i++) {
                objInArray = originalArray[i];
                if (objInArray == null) {
                    continue;
                }

                objectClass = getObjectClass(objInArray);
                if (objectClass.isArray()) {
                    targetArray[i] = copyArray(objInArray, objectClass, objectMap);
                } else {
                    targetArray[i] = copyObject(objInArray, objectClass, objectMap);
                }
            }
        }
        return copiedArrayObj;
    }

    private static <T> T createArrayInstanceIfNeeded(final T originalObj, final int length, final Class<T> objClass, final Map<Object, Object> objectMap) {
        T targetArray = (T) objectMap.get(originalObj);
        if (targetArray == null) {
            targetArray = (T) newInstance(objClass.getComponentType(), length);
            objectMap.put(originalObj, targetArray);
        }
        return targetArray;
    }

    private static <T> T createNonArrayInstanceIfNeeded(final T originalObj, final Class<T> objectClass, final Map<Object, Object> objectMap) {
        try {
            T targetObject = (T) objectMap.get(originalObj);
            if (targetObject == null) {
                targetObject = createObject(objectClass);
                objectMap.put(originalObj, targetObject);
            }
            return targetObject;
        } catch (final InstantiationException e) {
            throw new RuntimeException("Cannot instantiate class:" + objectClass.getName(), e);
        }
    }

    private static <T> T createObject(final Class<T> objectClass) throws InstantiationException {
        final T targetObject = (T) UNSAFE.allocateInstance(objectClass);
        if (!UNSAFE.shouldBeInitialized(objectClass)) {
            UNSAFE.ensureClassInitialized(objectClass);
        }
        return targetObject;
    }

    /*
    //!!Unsafe - not verified!!
    static Object unsafeShallowCopy(final Object obj) {
        if (obj == null || isImmutable(obj.getClass())) {
            return obj;
        }
        final long size = unsafeSizeOf(obj);
        final long start = toAddress(obj);
        final long address = UNSAFE.allocateMemory(size);
        UNSAFE.copyMemory(start, address, size);
        return fromAddress(address);
    }

    private static long toAddress(final Object obj) {
        final Object[] array = new Object[]{obj};
        return unsignedInt(UNSAFE.getInt(array, (long) ARRAY_OBJECT_BASE_OFFSET));
    }

    private static Object fromAddress(final long address) {
        final Object[] array = new Object[]{null};
        UNSAFE.putLong(array, (long) ARRAY_OBJECT_BASE_OFFSET, address);
        return array[0];
    }

    public static <T> T shallowCopy(final T obj) throws InstantiationException {
        if (obj == null || isImmutable(obj.getClass())) {
            return obj;
        }

        final Class<T> objClass = (Class<T>) obj.getClass();
        if (objClass.isArray()) {
            final int length = getLength(obj);
            final T arrayObj = createArray(objClass.getComponentType(), length);
            arraycopy(obj, 0, arrayObj, 0, length);
            return arrayObj;
        } else {
            final ClassStruct struct = ClassStructFactory.get(objClass);
            final long instanceBlockSize = struct.getInstanceBlockSize();
            final byte[] buffer = new byte[(int) instanceBlockSize];

            // copy field value block to an array buffer
            UNSAFE.copyMemory(obj, struct.getStartOffset(), buffer, ARRAY_BYTE_BASE_OFFSET, instanceBlockSize);

            final T newObj = createObject(objClass);
            restoreFromBuffer(buffer, struct, newObj);
            return newObj;
        }
    }

    private static <T> void restoreFromBuffer(final byte[] buffer, final ClassStruct classStruct, final T newObj) {
        //TODO
    }
    */
}
