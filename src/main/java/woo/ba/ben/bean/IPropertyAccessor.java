package woo.ba.ben.bean;


import sun.misc.Unsafe;
import woo.ba.ben.core.ClassStructFactory;
import woo.ba.ben.core.FieldStruct;
import woo.ba.ben.core.UnsafeFactory;

public interface IPropertyAccessor {
    Unsafe UNSAFE = UnsafeFactory.get();
    ClassStructFactory CLASS_STRUCT_FACTORY = ClassStructFactory.getInstance();

    default Class getBeanClass(final Object bean) {
        return bean instanceof Class ? (Class) bean : bean.getClass();
    }

    default FieldStruct getFieldStruct(final Class beanClass, final String field) {
        return CLASS_STRUCT_FACTORY.get(beanClass).getField(field);
    }
}
