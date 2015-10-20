package woo.ba.ben.core;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class FieldStruct {
    public final String name;
    public final Class type;
    public final long offset;
    public final Field realField;


    public FieldStruct(final Field field, final Unsafe unsafe) {
        if (field == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        this.realField = field;
        this.name = field.getName();
        this.type = field.getType();
        this.offset = unsafe.objectFieldOffset(field);
    }

    public boolean isArray() {
        return type.isArray();
    }

    public Class getArrayType() {
        return isArray()? type.getComponentType() : null;
    }

    public Class getFlatType() {
        return isArray()? type.getComponentType() : type;
    }

    public boolean isPrimitive() {
        return type.isPrimitive();
    }


//    protected boolean isArray;
//    protected Class<?> componentType;
//    protected int length;
//    protected int baseOffset; //ARRAY_INT_BASE_OFFSET, etc...
//    protected int indexScale; //ARRAY_INT_INDEX_SCALE, etc...

}
