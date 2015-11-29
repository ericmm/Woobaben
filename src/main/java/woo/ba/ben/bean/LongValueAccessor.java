package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_LONG_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_LONG_INDEX_SCALE;

class LongValueAccessor extends BaseValueAccessor implements LongPropertyAccessor {
    private final static LongPropertyAccessor LONG_PROPERTY_ACCESSOR = new LongValueAccessor();

    private LongValueAccessor() {
    }

    static LongPropertyAccessor getInstance() {
        return LONG_PROPERTY_ACCESSOR;
    }

    @Override
    public long get(Object bean, String field) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        return UNSAFE.getLong(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public long getArrayElementAt(Object bean, String field, int index) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getLong(arrayObj, (long) (ARRAY_LONG_BASE_OFFSET + index * ARRAY_LONG_INDEX_SCALE));
    }

    @Override
    public void set(Object bean, String field, long value) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        UNSAFE.putLong(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(Object bean, String field, int index, long value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putLong(arrayObj, (long) (ARRAY_LONG_BASE_OFFSET + index * ARRAY_LONG_INDEX_SCALE), value);
    }
}
