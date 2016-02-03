package woo.ba.ben.bean;


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
    public boolean get(final Object bean, final String field) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        return UNSAFE.getBoolean(fieldStruct.isStatic() ? beanClass : bean, fieldStruct.offset);
    }

    @Override
    public boolean getArrayElementAt(final Object bean, final String field, final int index) {
        final boolean[] array = (boolean[]) getArrayObject(bean, field, boolean.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    @Override
    public void set(final Object bean, final String field, final boolean value) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        UNSAFE.putBoolean(fieldStruct.isStatic() ? beanClass : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final boolean value) {
        final boolean[] array = (boolean[]) getArrayObject(bean, field, boolean.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }
}
