package woo.ba.ben.core;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
        this.offset = isStatic() ? unsafe.staticFieldOffset(field) : unsafe.objectFieldOffset(field);
    }

    public boolean isArray() {
        return type.isArray();
    }

    public Class getArrayType() {
        return isArray() ? type.getComponentType() : null;
    }

    public Class getFlatType() {
        return isArray() ? type.getComponentType() : type;
    }

    public boolean isPrimitive() {
        return type.isPrimitive();
    }

    public boolean hasParameterizedType() {
        return realField.getGenericType() instanceof ParameterizedType;
    }

    public Type[] getParameterizedTypes() {
        final Type genericType = realField.getGenericType();
        if (genericType instanceof ParameterizedType) {
            return ((ParameterizedType) genericType).getActualTypeArguments();
        }
        return null;
    }

    public Type getFirstParameterizedType() {
        final Type genericType = realField.getGenericType();
        if (genericType instanceof ParameterizedType) {
            final Type[] typeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
            if (typeArguments.length > 0) {
                return typeArguments[0];
            }
        }
        return null;
    }

    public boolean isStatic() {
        return Modifier.isStatic(realField.getModifiers());
    }


//    protected boolean isArray;
//    protected Class<?> componentType;
//    protected int length;
//    protected int baseOffset; //ARRAY_INT_BASE_OFFSET, etc...
//    protected int indexScale; //ARRAY_INT_INDEX_SCALE, etc...

}
