package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_INT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_INT_INDEX_SCALE;

public class IntValueAccessor extends BaseValueAccessor implements IntPropertyAccessor {
    private final static IntPropertyAccessor INT_PROPERTY_ACCESSOR = new IntValueAccessor();

    private IntValueAccessor() {
    }

    @Override
    public int get(Object bean, String field) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        return UNSAFE.getInt(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public int getArrayElementAt(Object bean, String field, int index) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getInt(arrayObj, (long) (ARRAY_INT_BASE_OFFSET + index * ARRAY_INT_INDEX_SCALE));
    }

    @Override
    public void set(Object bean, String field, int value) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        UNSAFE.putInt(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(Object bean, String field, int index, int value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putInt(arrayObj, (long) (ARRAY_INT_BASE_OFFSET + index * ARRAY_INT_INDEX_SCALE), value);
    }

    @Override
    public IPropertyAccessor getInstance() {
        return INT_PROPERTY_ACCESSOR;
    }
}
