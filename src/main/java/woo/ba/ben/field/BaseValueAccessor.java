package woo.ba.ben.field;

import woo.ba.ben.core.ClassStructFactory;
import woo.ba.ben.core.FieldStruct;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public abstract class BaseValueAccessor {

    protected Class getObjectClass(final Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Input object cannot be null!");
        }

        return obj instanceof Class ? (Class) obj : obj.getClass();
    }

    protected FieldStruct getFieldStruct(final Class objClass, final String field) {
        final FieldStruct fieldStruct = ClassStructFactory.get(objClass).getField(field);
        if (fieldStruct == null) {
            throw new IllegalArgumentException("Field [" + field + "] does not exist on Class [" + objClass + "] or its Super Classes!");
        }
        return fieldStruct;
    }

    protected Object getArrayObject(final Object obj, final String field, final Class componentType) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        if (!fieldStruct.isArray() && componentType.isAssignableFrom(fieldStruct.type.getComponentType())) {
            throw new IllegalArgumentException("Field [" + field + "] on Class [" + objClass + "] is not an array of type [" + componentType.getSimpleName() + "]!");
        }
        final Object arrayObj = UNSAFE.getObject(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
        if (arrayObj == null) {
            throw new IllegalArgumentException("Field [" + field + "] on Class [" + objClass + "] is NULL");
        }
        return arrayObj;
    }

    protected void checkArrayIndex(final int length, final int index) {
        if (index >= length) {
            throw new IndexOutOfBoundsException("Array index [" + index + "] is greater than array length [" + length + "]!");
        }
    }
}
