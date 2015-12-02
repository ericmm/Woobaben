package woo.ba.ben.bean;

import sun.misc.Unsafe;
import woo.ba.ben.core.ClassStructFactory;
import woo.ba.ben.core.FieldStruct;
import woo.ba.ben.core.UnsafeFactory;

public abstract class BaseValueAccessor {
    protected Unsafe UNSAFE = UnsafeFactory.get();

    protected Class getBeanClass(final Object bean) {
        if (bean == null) {
            throw new IllegalArgumentException("Bean cannot be null!");
        }
        return bean instanceof Class ? (Class) bean : bean.getClass();
    }

    protected FieldStruct getFieldStruct(final Class beanClass, final String field) {
        final FieldStruct fieldStruct = ClassStructFactory.get(beanClass).getField(field);
        if (fieldStruct == null) {
            throw new IllegalArgumentException("Field [" + field + "] does not exist on Class [" + beanClass + "] or its Super Classes!");
        }
        return fieldStruct;
    }

    protected Object getArrayObject(final Object bean, final String field, final Class componentType) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(beanClass, field);
        if (!fieldStruct.isArray() && componentType.isAssignableFrom(fieldStruct.type.getComponentType())) {
            throw new IllegalArgumentException("Field [" + field + "] on Class [" + beanClass + "] is not an array of type [" + componentType.getSimpleName() + "]!");
        }
        final Object arrayObj = UNSAFE.getObject(fieldStruct.isStatic() ? beanClass : bean, fieldStruct.offset);
        if (arrayObj == null) {
            throw new IllegalArgumentException("Field [" + field + "] on Class [" + beanClass + "] is NULL");
        }
        return arrayObj;
    }

    protected void checkArrayIndex(final int length, final int index) {
        if (index >= length) {
            throw new IndexOutOfBoundsException("Array index [" + index + "] is greater than array length [" + length + "]!");
        }
    }
}
