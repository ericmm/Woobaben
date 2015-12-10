package woo.ba.ben.bean;

import sun.misc.Unsafe;
import woo.ba.ben.core.*;

import java.util.List;

import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;
import static woo.ba.ben.bean.DataReader.*;

public class BeanCopier {
    private static final Unsafe UNSAFE = UnsafeFactory.get();

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

        final byte[] buffer = copyValuesToBuffer(originalObj, classStruct);
        final SimpleMap<Integer, Object> objectMap = new SimpleArrayMap<>();
        setValuesToObjectFromBuffer(objectInstance, classStruct, buffer, objectMap);

        return objectInstance;
    }

    private byte[] copyValuesToBuffer(Object originalObj, ClassStruct classStruct) {
        final long firstInstanceFieldStartPosition = classStruct.getInstanceFieldBlockStartPosition();
        final long instanceFieldsSize = classStruct.getInstanceFieldBlockEndPosition() - firstInstanceFieldStartPosition;
        final byte[] buffer = new byte[(int) instanceFieldsSize];
        UNSAFE.copyMemory(originalObj, firstInstanceFieldStartPosition, buffer, ARRAY_BYTE_BASE_OFFSET, instanceFieldsSize);
        return buffer;
    }

    private void setValuesToObjectFromBuffer(final Object objectInstance, final ClassStruct classStruct, final byte[] buffer, final SimpleMap<Integer, Object> objectMap) throws InstantiationException {
        final List<FieldStruct> sortedInstanceFields = classStruct.getSortedInstanceFields();
        final long firstOffset = sortedInstanceFields.get(0).offset;
        for (final FieldStruct fieldStruct : sortedInstanceFields) {
//            if(fieldStruct.isTransient()) {
//                continue;
//            }

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
                Object objectAttribute = null;
//                Object objectAttribute = objectMap.get(objectReferenceInOriginalBean);
//                if (objectAttribute == null) {
//                    objectAttribute = createInstance(fieldStruct.type);
//                    objectMap.put(objectReferenceInOriginalBean, objectAttribute);
//                    copyObject(objectAttribute, fieldStruct.type, objectMap);
//                }
                UNSAFE.putObject(objectInstance, fieldStruct.offset, objectAttribute);

            }
        }
    }

    private void copyObject(Object objectAttribute, Class attributeClass, SimpleMap<Integer, Object> objectMap) {
        if (attributeClass.isArray()) {
            final Class componentType = attributeClass.getComponentType();
            if (componentType == byte[].class) {

            } else if (componentType == boolean[].class) {

            } else if (componentType == char[].class) {

            } else if (componentType == short[].class) {

            } else if (componentType == float[].class) {

            } else if (componentType == int[].class) {

            } else if (componentType == long[].class) {

            } else if (componentType == double[].class) {

            } else {
                //Object[]

            }
        } else {
            //normal Object
            //copyBean(objectAttribute, attributeClass, objectMap);
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
