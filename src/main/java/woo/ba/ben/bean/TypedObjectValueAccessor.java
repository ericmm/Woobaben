package woo.ba.ben.bean;


import woo.ba.ben.core.FieldStruct;

import static sun.misc.Unsafe.ARRAY_OBJECT_BASE_OFFSET;
import static sun.misc.Unsafe.ARRAY_OBJECT_INDEX_SCALE;

class TypedObjectValueAccessor extends BaseValueAccessor implements TypedObjectPropertyAccessor {
    private final static TypedObjectPropertyAccessor TYPED_OBJECT_PROPERTY_ACCESSOR = new TypedObjectValueAccessor();

    private TypedObjectValueAccessor() {
    }

    static TypedObjectPropertyAccessor getInstance() {
        return TYPED_OBJECT_PROPERTY_ACCESSOR;
    }

    @Override
    public Object get(final Object bean, final String field) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
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
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        UNSAFE.putObject(fieldStruct.isStatic() ? getBeanClass(bean) : bean, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object bean, final String field, final int index, final Object value) {
        final Object arrayObj = getArrayObject(bean, field);
        checkArrayIndex(arrayObj, index);
        UNSAFE.putObject(arrayObj, (long) (ARRAY_OBJECT_BASE_OFFSET + index * ARRAY_OBJECT_INDEX_SCALE), value);
    }
}
