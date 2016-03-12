package woo.ba.ben.field;


import woo.ba.ben.core.FieldStruct;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class IntValueAccessor extends BaseValueAccessor implements IntPropertyAccessor {
    private final static IntPropertyAccessor INT_PROPERTY_ACCESSOR = new IntValueAccessor();

    private IntValueAccessor() {
    }

    static IntPropertyAccessor getInstance() {
        return INT_PROPERTY_ACCESSOR;
    }

    @Override
    public int get(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getInt(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    @Override
    public int getArrayElementAt(final Object obj, final String field, final int index) {
        final int[] array = (int[]) getArrayObject(obj, field, int.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    @Override
    public void set(final Object obj, final String field, final int value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putInt(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object obj, final String field, final int index, final int value) {
        final int[] array = (int[]) getArrayObject(obj, field, int.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }
}
