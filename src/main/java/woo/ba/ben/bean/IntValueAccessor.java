package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_INT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_INT_INDEX_SCALE;

class IntValueAccessor extends BaseValueAccessor implements IntPropertyAccessor {
    private final static IntPropertyAccessor INT_PROPERTY_ACCESSOR = new IntValueAccessor();

    private IntValueAccessor() {
    }

    static IntPropertyAccessor getInstance() {
        return INT_PROPERTY_ACCESSOR;
    }

    @Override
    public int get(final Object bean, final String field) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        return UNSAFE.getInt(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public int getArrayElementAt(final Object bean, final String field, final int index) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getInt(arrayObj, (long) (ARRAY_INT_BASE_OFFSET + index * ARRAY_INT_INDEX_SCALE));
    }

    @Override
    public void set(final Object bean, final String field, final int value) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        UNSAFE.putInt(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final int value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putInt(arrayObj, (long) (ARRAY_INT_BASE_OFFSET + index * ARRAY_INT_INDEX_SCALE), value);
    }
}
