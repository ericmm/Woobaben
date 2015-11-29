package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_BYTE_INDEX_SCALE;

class ByteValueAccessor extends BaseValueAccessor implements BytePropertyAccessor {
    private final static BytePropertyAccessor BYTE_PROPERTY_ACCESSOR = new ByteValueAccessor();

    private ByteValueAccessor() {
    }

    static BytePropertyAccessor getInstance() {
        return BYTE_PROPERTY_ACCESSOR;
    }

    @Override
    public byte get(final Object bean, final String field) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        return UNSAFE.getByte(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public byte getArrayElementAt(final Object bean, final String field, final int index) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getByte(arrayObj, (long) (ARRAY_BYTE_BASE_OFFSET + index * ARRAY_BYTE_INDEX_SCALE));
    }

    @Override
    public void set(final Object bean, final String field, final byte value) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        UNSAFE.putByte(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final byte value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putByte(arrayObj, (long) (ARRAY_BYTE_BASE_OFFSET + index * ARRAY_BYTE_INDEX_SCALE), value);
    }
}
