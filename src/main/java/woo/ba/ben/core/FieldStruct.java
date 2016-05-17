package woo.ba.ben.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class FieldStruct {

    final String name;
    final Class type;
    final long offset;
    final Field realField;

    FieldStruct(final Field field) {
        assert field != null;

        this.realField = field;
        this.name = field.getName();
        this.type = field.getType();
        this.offset = isStatic() ? UNSAFE.staticFieldOffset(field) : UNSAFE.objectFieldOffset(field);
    }

    boolean isArray() {
        return type.isArray();
    }

    Class getArrayType() {
        return isArray() ? type.getComponentType() : null;
    }

    boolean isPrimitive() {
        return type.isPrimitive();
    }

    boolean hasParameterizedType() {
        return realField.getGenericType() instanceof ParameterizedType;
    }

    Type[] getParameterizedTypes() {
        final Type genericType = realField.getGenericType();
        if (genericType instanceof ParameterizedType) {
            return ((ParameterizedType) genericType).getActualTypeArguments();
        }
        return null;
    }

    Type getFirstParameterizedType() {
        final Type[] typeArguments = getParameterizedTypes();
        if (typeArguments != null && typeArguments.length > 0) {
            return typeArguments[0];
        }
        return null;
    }

    boolean isStatic() {
        return Modifier.isStatic(realField.getModifiers());
    }

    boolean isTransient() {
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
