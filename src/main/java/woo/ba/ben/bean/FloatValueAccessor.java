package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_FLOAT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_FLOAT_INDEX_SCALE;

public class FloatValueAccessor extends BaseValueAccessor implements FloatPropertyAccessor {
    private final static FloatPropertyAccessor FLOAT_PROPERTY_ACCESSOR = new FloatValueAccessor();

    private FloatValueAccessor() {
    }

    @Override
    public float get(Object bean, String field) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        return UNSAFE.getFloat(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public float getArrayElementAt(Object bean, String field, int index) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getFloat(arrayObj, (long) (ARRAY_FLOAT_BASE_OFFSET + index * ARRAY_FLOAT_INDEX_SCALE));
    }

    @Override
    public void set(Object bean, String field, float value) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        UNSAFE.putFloat(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(Object bean, String field, int index, float value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putFloat(arrayObj, (long) (ARRAY_FLOAT_BASE_OFFSET + index * ARRAY_FLOAT_INDEX_SCALE), value);
    }

    @Override
    public IPropertyAccessor getInstance() {
        return FLOAT_PROPERTY_ACCESSOR;
    }
}
