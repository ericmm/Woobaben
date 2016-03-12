package woo.ba.ben.field;


import woo.ba.ben.core.FieldStruct;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class FloatValueAccessor extends BaseValueAccessor implements FloatPropertyAccessor {
    private final static FloatPropertyAccessor FLOAT_PROPERTY_ACCESSOR = new FloatValueAccessor();

    private FloatValueAccessor() {
    }

    static FloatPropertyAccessor getInstance() {
        return FLOAT_PROPERTY_ACCESSOR;
    }

    @Override
    public float get(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getFloat(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    @Override
    public float getArrayElementAt(final Object obj, final String field, final int index) {
        final float[] array = (float[]) getArrayObject(obj, field, float.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    @Override
    public void set(final Object obj, final String field, final float value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putFloat(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object obj, final String field, final int index, final float value) {
        final float[] array = (float[]) getArrayObject(obj, field, float.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }
}
