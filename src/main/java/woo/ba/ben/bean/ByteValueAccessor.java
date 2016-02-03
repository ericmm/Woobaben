package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

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
        final byte[] array = (byte[]) getArrayObject(bean, field, byte.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    @Override
    public void set(final Object bean, final String field, final byte value) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        UNSAFE.putByte(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final byte value) {
        final byte[] array = (byte[]) getArrayObject(bean, field, byte.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }
}
