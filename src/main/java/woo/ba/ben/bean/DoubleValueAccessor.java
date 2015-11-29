package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_DOUBLE_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_DOUBLE_INDEX_SCALE;

class DoubleValueAccessor extends BaseValueAccessor implements DoublePropertyAccessor {
    private final static DoublePropertyAccessor DOUBLE_PROPERTY_ACCESSOR = new DoubleValueAccessor();

    private DoubleValueAccessor() {
    }

    static DoublePropertyAccessor getInstance() {
        return DOUBLE_PROPERTY_ACCESSOR;
    }

    @Override
    public double get(final Object bean, final String field) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        return UNSAFE.getDouble(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public double getArrayElementAt(final Object bean, final String field, final int index) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getDouble(arrayObj, (long) (ARRAY_DOUBLE_BASE_OFFSET + index * ARRAY_DOUBLE_INDEX_SCALE));
    }

    @Override
    public void set(final Object bean, final String field, final double value) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        UNSAFE.putDouble(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final double value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putDouble(arrayObj, (long) (ARRAY_DOUBLE_BASE_OFFSET + index * ARRAY_DOUBLE_INDEX_SCALE), value);
    }
}
