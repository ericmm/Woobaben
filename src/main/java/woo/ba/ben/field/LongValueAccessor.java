package woo.ba.ben.field;


import woo.ba.ben.core.FieldStruct;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class LongValueAccessor extends BaseValueAccessor implements LongPropertyAccessor {
    private final static LongPropertyAccessor LONG_PROPERTY_ACCESSOR = new LongValueAccessor();

    private LongValueAccessor() {
    }

    static LongPropertyAccessor getInstance() {
        return LONG_PROPERTY_ACCESSOR;
    }

    @Override
    public long get(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getLong(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    @Override
    public long getArrayElementAt(final Object obj, final String field, final int index) {
        final long[] array = (long[]) getArrayObject(obj, field, long.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    @Override
    public void set(final Object obj, final String field, final long value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putLong(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object obj, final String field, final int index, final long value) {
        final long[] array = (long[]) getArrayObject(obj, field, long.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }
}
