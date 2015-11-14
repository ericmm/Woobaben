package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_CHAR_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_CHAR_INDEX_SCALE;

public class CharValueAccessor extends BaseValueAccessor implements CharPropertyAccessor {
    private final static CharPropertyAccessor CHAR_PROPERTY_ACCESSOR = new CharValueAccessor();

    private CharValueAccessor() {
    }

    @Override
    public char get(Object bean, String field) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        return UNSAFE.getChar(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public char getArrayElementAt(Object bean, String field, int index) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getChar(arrayObj, (long) (ARRAY_CHAR_BASE_OFFSET + index * ARRAY_CHAR_INDEX_SCALE));
    }

    @Override
    public void set(Object bean, String field, char value) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        UNSAFE.putChar(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(Object bean, String field, int index, char value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putChar(arrayObj, (long) (ARRAY_CHAR_BASE_OFFSET + index * ARRAY_CHAR_INDEX_SCALE), value);
    }

    public static IPropertyAccessor getInstance() {
        return CHAR_PROPERTY_ACCESSOR;
    }
}
