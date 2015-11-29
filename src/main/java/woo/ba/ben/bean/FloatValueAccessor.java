package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_FLOAT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_FLOAT_INDEX_SCALE;

class FloatValueAccessor extends BaseValueAccessor implements FloatPropertyAccessor {
    private final static FloatPropertyAccessor FLOAT_PROPERTY_ACCESSOR = new FloatValueAccessor();

    private FloatValueAccessor() {
    }

    static FloatPropertyAccessor getInstance() {
        return FLOAT_PROPERTY_ACCESSOR;
    }

    @Override
    public float get(final Object bean, final String field) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        return UNSAFE.getFloat(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public float getArrayElementAt(final Object bean, final String field, final int index) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getFloat(arrayObj, (long) (ARRAY_FLOAT_BASE_OFFSET + index * ARRAY_FLOAT_INDEX_SCALE));
    }

    @Override
    public void set(final Object bean, final String field, final float value) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        UNSAFE.putFloat(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final float value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putFloat(arrayObj, (long) (ARRAY_FLOAT_BASE_OFFSET + index * ARRAY_FLOAT_INDEX_SCALE), value);
    }
}
