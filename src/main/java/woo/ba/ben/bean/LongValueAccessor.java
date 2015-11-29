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
    public long get(final Object bean, final String field) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        return UNSAFE.getLong(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public long getArrayElementAt(final Object bean, final String field, final int index) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getLong(arrayObj, (long) (ARRAY_LONG_BASE_OFFSET + index * ARRAY_LONG_INDEX_SCALE));
    }

    @Override
    public void set(final Object bean, final String field, final long value) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        UNSAFE.putLong(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final long value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putLong(arrayObj, (long) (ARRAY_LONG_BASE_OFFSET + index * ARRAY_LONG_INDEX_SCALE), value);
    }
}
