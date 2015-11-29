package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_SHORT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_SHORT_INDEX_SCALE;

class ShortValueAccessor extends BaseValueAccessor implements ShortPropertyAccessor {
    private final static ShortPropertyAccessor SHORT_PROPERTY_ACCESSOR = new ShortValueAccessor();

    private ShortValueAccessor() {
    }

    static ShortPropertyAccessor getInstance() {
        return SHORT_PROPERTY_ACCESSOR;
    }

    @Override
    public short get(final Object bean, final String field) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        return UNSAFE.getShort(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public short getArrayElementAt(final Object bean, final String field, final int index) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getShort(arrayObj, (long) (ARRAY_SHORT_BASE_OFFSET + index * ARRAY_SHORT_INDEX_SCALE));
    }

    @Override
    public void set(final Object bean, final String field, final short value) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        UNSAFE.putShort(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final short value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putShort(arrayObj, (long) (ARRAY_SHORT_BASE_OFFSET + index * ARRAY_SHORT_INDEX_SCALE), value);
    }
}
