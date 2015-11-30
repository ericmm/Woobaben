package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

class CharValueAccessor extends BaseValueAccessor implements CharPropertyAccessor {
    private final static CharPropertyAccessor CHAR_PROPERTY_ACCESSOR = new CharValueAccessor();

    private CharValueAccessor() {
    }

    static CharPropertyAccessor getInstance() {
        return CHAR_PROPERTY_ACCESSOR;
    }

    @Override
    public char get(final Object bean, final String field) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        return UNSAFE.getChar(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public char getArrayElementAt(final Object bean, final String field, final int index) {
        final char[] array = (char[]) getArrayObject(bean, field, char.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    @Override
    public void set(final Object bean, final String field, final char value) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        UNSAFE.putChar(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final char value) {
        final char[] array = (char[]) getArrayObject(bean, field, char.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }
}
