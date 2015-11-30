package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

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
        final short[] array = (short[]) getArrayObject(bean, field, short.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    @Override
    public void set(final Object bean, final String field, final short value) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        UNSAFE.putShort(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final short value) {
        final short[] array = (short[]) getArrayObject(bean, field, short.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }
}
