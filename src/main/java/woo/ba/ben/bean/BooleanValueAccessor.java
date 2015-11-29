package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_BOOLEAN_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_BOOLEAN_INDEX_SCALE;

class BooleanValueAccessor extends BaseValueAccessor implements BooleanPropertyAccessor {
    private final static BooleanPropertyAccessor BOOLEAN_PROPERTY_ACCESSOR = new BooleanValueAccessor();

    private BooleanValueAccessor() {
    }

    static BooleanPropertyAccessor getInstance() {
        return BOOLEAN_PROPERTY_ACCESSOR;
    }

    @Override
    public boolean get(final Object bean, final String field) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        return UNSAFE.getBoolean(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public boolean getArrayElementAt(final Object bean, final String field, final int index) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getBoolean(arrayObj, (long) (ARRAY_BOOLEAN_BASE_OFFSET + index * ARRAY_BOOLEAN_INDEX_SCALE));
    }

    @Override
    public void set(final Object bean, final String field, final boolean value) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        UNSAFE.putBoolean(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final boolean value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putBoolean(arrayObj, (long) (ARRAY_BOOLEAN_BASE_OFFSET + index * ARRAY_BOOLEAN_INDEX_SCALE), value);
    }
}
