package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_SHORT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_SHORT_INDEX_SCALE;

public class ShortValueAccessor extends BaseValueAccessor implements ShortPropertyAccessor {
    private final static ShortPropertyAccessor SHORT_PROPERTY_ACCESSOR = new ShortValueAccessor();

    private ShortValueAccessor() {
    }

    @Override
    public short get(Object bean, String field) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        return UNSAFE.getShort(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public short getArrayElementAt(Object bean, String field, int index) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getShort(arrayObj, (long) (ARRAY_SHORT_BASE_OFFSET + index * ARRAY_SHORT_INDEX_SCALE));
    }

    @Override
    public void set(Object bean, String field, short value) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        UNSAFE.putShort(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(Object bean, String field, int index, short value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putShort(arrayObj, (long) (ARRAY_SHORT_BASE_OFFSET + index * ARRAY_SHORT_INDEX_SCALE), value);
    }

    @Override
    public IPropertyAccessor getInstance() {
        return SHORT_PROPERTY_ACCESSOR;
    }
}
