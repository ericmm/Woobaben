package woo.ba.ben.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public class FieldStruct {

    public final String name;
    public final Class type;
    public final long offset;
    public final Field realField;

    FieldStruct(final Field field) {
        if (field == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        this.realField = field;
        this.name = field.getName();
        this.type = field.getType();
        this.offset = isStatic() ? UNSAFE.staticFieldOffset(field) : UNSAFE.objectFieldOffset(field);
    }

    public boolean isArray() {
        return type.isArray();
    }

    public Class getArrayType() {
        return isArray() ? type.getComponentType() : null;
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
        final Type[] typeArguments = getParameterizedTypes();
        if(typeArguments!=null && typeArguments.length > 0) {
            return typeArguments[0];
        }
        return null;
    }

    public boolean isStatic() {
        return Modifier.isStatic(realField.getModifiers());
    }
    public boolean isTransient() {
        return Modifier.isTransient(realField.getModifiers());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final FieldStruct that = (FieldStruct) o;

        return realField.equals(that.realField);
    }

    @Override
    public int hashCode() {
        return realField.hashCode();
    }

    @Override
    public String toString() {
        return "FieldStruct{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", offset=" + offset +
                ", realField=" + realField +
                '}';
    }
}
