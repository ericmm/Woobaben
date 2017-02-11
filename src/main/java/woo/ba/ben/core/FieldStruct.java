package woo.ba.ben.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class FieldStruct {
    final String name;
    final Class type;
    final long offset;
    private final int modifiers;

    FieldStruct(final Field field) {
        Objects.requireNonNull(field, "The field parameter cannot be null");

        this.name = field.getName();
        this.type = field.getType();
        this.modifiers = field.getModifiers();
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

    boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }

    boolean isTransient() {
        return Modifier.isTransient(modifiers);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final FieldStruct that = (FieldStruct) o;

        if (offset != that.offset) return false;
        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (int) (offset ^ (offset >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "FieldStruct{" + "name='" + name + '\'' +
                ", type=" + type +
                ", offset=" + offset +
                ", modifiers=" + modifiers +
                '}';
    }
}
