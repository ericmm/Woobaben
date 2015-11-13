package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import java.lang.reflect.Array;

import static sun.misc.Unsafe.ARRAY_BOOLEAN_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_BOOLEAN_INDEX_SCALE;

public class BooleanValuePropertyAccessor implements BooleanPropertyAccessor {

    @Override
    public boolean get(final Object bean, final String field) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        return UNSAFE.getBoolean(fieldStruct.isStatic() ? beanClass : bean, fieldStruct.offset);
    }

    @Override
    public boolean getArrayElementAt(final Object bean, final String field, final int index) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        if (!fieldStruct.isArray()) {
            throw new IllegalArgumentException("Field [" + field + "] on Class [" + beanClass + "] is not an array!");
        }

        final Object arrayObj = UNSAFE.getObject(fieldStruct.isStatic() ? beanClass : bean, fieldStruct.offset);
        final int length = Array.getLength(arrayObj);
        if (index >= length) {
            throw new IndexOutOfBoundsException("Array index ["+index+"] is greater than array length ["+length+"]!");
        }

        return UNSAFE.getBoolean(arrayObj, (long) (ARRAY_BOOLEAN_BASE_OFFSET + index * ARRAY_BOOLEAN_INDEX_SCALE));
    }

    @Override
    public void set(final Object bean, final String field, final boolean value) {

    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final boolean value) {

    }
}
