package woo.ba.ben.bean;


import sun.misc.Unsafe;
import woo.ba.ben.core.ClassStructFactory;
import woo.ba.ben.core.UnsafeFactory;

public interface IPropertyAccessor {
    Unsafe UNSAFE = UnsafeFactory.get();
    ClassStructFactory CLASS_STRUCT_FACTORY = ClassStructFactory.getInstance();

    IPropertyAccessor getInstance();
}
