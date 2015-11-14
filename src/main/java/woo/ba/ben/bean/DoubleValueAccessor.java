package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_DOUBLE_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_DOUBLE_INDEX_SCALE;

public class DoubleValueAccessor extends BaseValueAccessor implements DoublePropertyAccessor {
    private final static DoublePropertyAccessor DOUBLE_PROPERTY_ACCESSOR = new DoubleValueAccessor();

    private DoubleValueAccessor() {
    }

    @Override
    public double get(Object bean, String field) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        return UNSAFE.getDouble(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public double getArrayElementAt(Object bean, String field, int index) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getDouble(arrayObj, (long) (ARRAY_DOUBLE_BASE_OFFSET + index * ARRAY_DOUBLE_INDEX_SCALE));
    }

    @Override
    public void set(Object bean, String field, double value) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        UNSAFE.putDouble(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(Object bean, String field, int index, double value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putDouble(arrayObj, (long) (ARRAY_DOUBLE_BASE_OFFSET + index * ARRAY_DOUBLE_INDEX_SCALE), value);
    }

    public static IPropertyAccessor getInstance() {
        return DOUBLE_PROPERTY_ACCESSOR;
    }
}
