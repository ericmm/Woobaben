package woo.ba.ben.field;


import woo.ba.ben.core.FieldStruct;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class TypedObjectValueAccessor extends BaseValueAccessor implements TypedObjectPropertyAccessor {
    private final static TypedObjectPropertyAccessor TYPED_OBJECT_PROPERTY_ACCESSOR = new TypedObjectValueAccessor();

    private TypedObjectValueAccessor() {
    }

    static TypedObjectPropertyAccessor getInstance() {
        return TYPED_OBJECT_PROPERTY_ACCESSOR;
    }

    @Override
    public Object get(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getObject(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    @Override
    public Object getArrayElementAt(final Object obj, final String field, final int index) {
        final Object[] array = (Object[]) getArrayObject(obj, field, Object.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    @Override
    public void set(final Object obj, final String field, final Object value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putObject(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    @Override
    public void setArrayElementAt(final Object obj, final String field, final int index, final Object value) {
        final Object[] array = (Object[]) getArrayObject(obj, field, Object.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }
}
