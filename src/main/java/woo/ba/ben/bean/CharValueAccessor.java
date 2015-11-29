package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_CHAR_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_CHAR_INDEX_SCALE;

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
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getChar(arrayObj, (long) (ARRAY_CHAR_BASE_OFFSET + index * ARRAY_CHAR_INDEX_SCALE));
    }

    @Override
    public void set(final Object bean, final String field, final char value) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        UNSAFE.putChar(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final char value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putChar(arrayObj, (long) (ARRAY_CHAR_BASE_OFFSET + index * ARRAY_CHAR_INDEX_SCALE), value);
    }
}
