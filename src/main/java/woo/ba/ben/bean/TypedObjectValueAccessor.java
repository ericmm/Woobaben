package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_OBJECT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_OBJECT_INDEX_SCALE;

public class TypedObjectValueAccessor extends BaseValueAccessor implements TypedObjectPropertyAccessor {
    private final static TypedObjectPropertyAccessor TYPED_OBJECT_PROPERTY_ACCESSOR = new TypedObjectValueAccessor();

    private TypedObjectValueAccessor() {
    }

    @Override
    public Object get(final Object bean, final String field) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        return UNSAFE.getObject(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset);
    }

    @Override
    public Object getArrayElementAt(final Object bean, final String field, final int index) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        return UNSAFE.getObject(arrayObj, (long) (ARRAY_OBJECT_BASE_OFFSET + index * ARRAY_OBJECT_INDEX_SCALE));
    }

    @Override
    public void set(final Object bean, final String field, final Object value) {
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        UNSAFE.putObject(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final Object value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putObject(arrayObj, (long) (ARRAY_OBJECT_BASE_OFFSET + index * ARRAY_OBJECT_INDEX_SCALE), value);
    }

    @Override
    public IPropertyAccessor getInstance() {
        return TYPED_OBJECT_PROPERTY_ACCESSOR;
    }
}
