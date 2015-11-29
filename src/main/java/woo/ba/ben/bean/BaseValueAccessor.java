package woo.ba.ben.bean;

import sun.misc.Unsafe;
import woo.ba.ben.core.ClassStructFactory;
import woo.ba.ben.core.FieldStruct;
import woo.ba.ben.core.UnsafeFactory;

import java.lang.reflect.Array;

public abstract class BaseValueAccessor {
    protected Unsafe UNSAFE = UnsafeFactory.get();
    protected ClassStructFactory CLASS_STRUCT_FACTORY = ClassStructFactory.getInstance();

    protected Class getBeanClass(final Object bean) {
        return bean instanceof Class ? (Class) bean : bean.getClass();
    }

    protected FieldStruct getFieldStruct(final Object bean, final String field) {
        if (bean == null) {
            throw new IllegalArgumentException("Bean cannot be null!");
        }
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = CLASS_STRUCT_FACTORY.get(beanClass).getField(field);
        if (fieldStruct == null) {
            throw new IllegalArgumentException("Field [" + field + "] does not exist on Class [" + beanClass + "] or its Super Classes!");
        }
        return fieldStruct;
    }

    protected Object getArrayObject(final Object bean, final String field) {
        final Class beanClass = getBeanClass(bean);
        final FieldStruct fieldStruct = getFieldStruct(bean, field);
        if (!fieldStruct.isArray()) {
            throw new IllegalArgumentException("Field [" + field + "] on Class [" + beanClass + "] is not an array!");
        }
        final Object arrayObj = UNSAFE.getObject(fieldStruct.isStatic() ? beanClass : bean, fieldStruct.offset);
        if (arrayObj == null) {
            throw new IllegalArgumentException("Field [" + field + "] on Class [" + beanClass + "] is NULL");
        }
        return arrayObj;
    }

    protected void checkArrayIndex(final Object arrayObj, final int index) {
        final int length = Array.getLength(arrayObj);
        if (index >= length) {
            throw new IndexOutOfBoundsException("Array index [" + index + "] is greater than array length [" + length + "]!");
        }
    }
}
