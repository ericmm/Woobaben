package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import java.lang.reflect.Array;

import static sun.misc.Unsafe.ARRAY_OBJECT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_OBJECT_INDEX_SCALE;

public class TypedObjectValuePropertyAccessor<T> implements TypedObjectPropertyAccessor {

    @Override
    public Object get(final Object bean, final String field) {
        if (bean == null) {
            return null;
        }
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        return fieldStruct == null ? null : UNSAFE.getObject(fieldStruct.isStatic() ? beanClass : bean, fieldStruct.offset);
    }


    @Override
    public Object getArrayElementAt(final Object bean, final String field, final int index) {
        if (bean == null) {
            return null;
        }
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        if (fieldStruct == null) {
            return null;
        }

        if (!fieldStruct.isArray()) {
            throw new IllegalArgumentException("Field [" + field + "] on Class [" + beanClass + "] is not an array!");
        }
        final Object arrayObj = UNSAFE.getObject(fieldStruct.isStatic() ? beanClass : bean, fieldStruct.offset);
        if (arrayObj == null) {
            return null;
        }
        final int length = Array.getLength(arrayObj);
        if (index >= length) {
            throw new IndexOutOfBoundsException("Array index ["+index+"] is greater than array length ["+length+"]!");
        }

        return UNSAFE.getObject(arrayObj, (long) (ARRAY_OBJECT_BASE_OFFSET + index * ARRAY_OBJECT_INDEX_SCALE));
    }

    @Override
    public void set(final Object bean, final String field, final Object value) {

    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final Object value) {

    }
}
