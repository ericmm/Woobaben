package woo.ba.ben.field;


import woo.ba.ben.core.FieldStruct;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class CharValueAccessor extends BaseValueAccessor implements CharPropertyAccessor {
    private final static CharPropertyAccessor CHAR_PROPERTY_ACCESSOR = new CharValueAccessor();

    private CharValueAccessor() {
    }

    static CharPropertyAccessor getInstance() {
        return CHAR_PROPERTY_ACCESSOR;
    }

    @Override
    public char get(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getChar(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    @Override
    public char getArrayElementAt(final Object obj, final String field, final int index) {
        final char[] array = (char[]) getArrayObject(obj, field, char.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    @Override
    public void set(final Object obj, final String field, final char value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putChar(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object obj, final String field, final int index, final char value) {
        final char[] array = (char[]) getArrayObject(obj, field, char.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }
}
