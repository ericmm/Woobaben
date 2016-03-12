package woo.ba.ben.field;


import woo.ba.ben.core.FieldStruct;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class BooleanValueAccessor extends BaseValueAccessor implements BooleanPropertyAccessor {
    private final static BooleanPropertyAccessor BOOLEAN_PROPERTY_ACCESSOR = new BooleanValueAccessor();

    private BooleanValueAccessor() {
    }

    static BooleanPropertyAccessor getInstance() {
        return BOOLEAN_PROPERTY_ACCESSOR;
    }

    @Override
    public boolean get(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getBoolean(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
    }

    @Override
    public boolean getArrayElementAt(final Object obj, final String field, final int index) {
        final boolean[] array = (boolean[]) getArrayObject(obj, field, boolean.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    @Override
    public void set(final Object obj, final String field, final boolean value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putBoolean(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object obj, final String field, final int index, final boolean value) {
        final boolean[] array = (boolean[]) getArrayObject(obj, field, boolean.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }
}
