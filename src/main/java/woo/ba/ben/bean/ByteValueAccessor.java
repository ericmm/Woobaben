package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_BYTE_INDEX_SCALE;

public class ByteValueAccessor extends BaseValueAccessor implements BytePropertyAccessor {
    private final static BytePropertyAccessor BYTE_PROPERTY_ACCESSOR = new ByteValueAccessor();

    private ByteValueAccessor() {
    }

    @Override
    public byte get(Object bean, String field) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        return UNSAFE.getByte(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public byte getArrayElementAt(Object bean, String field, int index) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getByte(arrayObj, (long) (ARRAY_BYTE_BASE_OFFSET + index * ARRAY_BYTE_INDEX_SCALE));
    }

    @Override
    public void set(Object bean, String field, byte value) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        UNSAFE.putByte(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(Object bean, String field, int index, byte value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putByte(arrayObj, (long) (ARRAY_BYTE_BASE_OFFSET + index * ARRAY_BYTE_INDEX_SCALE), value);
    }

    public static IPropertyAccessor getInstance() {
        return BYTE_PROPERTY_ACCESSOR;
    }
}
