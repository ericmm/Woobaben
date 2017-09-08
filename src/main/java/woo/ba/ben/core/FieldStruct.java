package woo.ba.ben.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

/*
 * Represent the field object
 *
 */
final class FieldStruct {
    final String name;
    final Class type;
    final long offset;
    final int modifiers;

    FieldStruct(final Field field) {
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

    @Override
    public boolean equals(final Object o) {
        return this == o || o != null && FieldStruct.class == o.getClass() && offset == ((FieldStruct) o).offset;
    }

    @Override
    public int hashCode() {
        return (int) (offset ^ (offset >>> 32));
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
