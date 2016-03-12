package woo.ba.ben.field;


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
    public byte get(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getByte(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    @Override
    public byte getArrayElementAt(final Object obj, final String field, final int index) {
        final byte[] array = (byte[]) getArrayObject(obj, field, byte.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    @Override
    public void set(final Object obj, final String field, final byte value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putByte(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object obj, final String field, final int index, final byte value) {
        final byte[] array = (byte[]) getArrayObject(obj, field, byte.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }
}
