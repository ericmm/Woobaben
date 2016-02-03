package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

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
        final float[] array = (float[]) getArrayObject(bean, field, float.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    @Override
    public void set(final Object bean, final String field, final float value) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        UNSAFE.putFloat(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final float value) {
        final float[] array = (float[]) getArrayObject(bean, field, float.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }
}
