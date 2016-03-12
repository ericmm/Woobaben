package woo.ba.ben.field;


import woo.ba.ben.core.FieldStruct;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class DoubleValueAccessor extends BaseValueAccessor implements DoublePropertyAccessor {
    private final static DoublePropertyAccessor DOUBLE_PROPERTY_ACCESSOR = new DoubleValueAccessor();

    private DoubleValueAccessor() {
    }

    static DoublePropertyAccessor getInstance() {
        return DOUBLE_PROPERTY_ACCESSOR;
    }

    @Override
    public double get(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getDouble(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    @Override
    public double getArrayElementAt(final Object obj, final String field, final int index) {
        final double[] array = (double[]) getArrayObject(obj, field, double.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    @Override
    public void set(final Object obj, final String field, final double value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putDouble(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object obj, final String field, final int index, final double value) {
        final double[] array = (double[]) getArrayObject(obj, field, double.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }
}
