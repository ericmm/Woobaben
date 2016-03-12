package woo.ba.ben.field;


import woo.ba.ben.core.FieldStruct;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class ShortValueAccessor extends BaseValueAccessor implements ShortPropertyAccessor {
    private final static ShortPropertyAccessor SHORT_PROPERTY_ACCESSOR = new ShortValueAccessor();

    private ShortValueAccessor() {
    }

    static ShortPropertyAccessor getInstance() {
        return SHORT_PROPERTY_ACCESSOR;
    }

    @Override
    public short get(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getShort(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    @Override
    public short getArrayElementAt(final Object obj, final String field, final int index) {
        final short[] array = (short[]) getArrayObject(obj, field, short.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    @Override
    public void set(final Object obj, final String field, final short value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putShort(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object obj, final String field, final int index, final short value) {
        final short[] array = (short[]) getArrayObject(obj, field, short.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }
}
