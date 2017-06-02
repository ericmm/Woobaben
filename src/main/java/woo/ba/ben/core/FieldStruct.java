package woo.ba.ben.core;

import java.lang.reflect.Field;

import static java.lang.reflect.Modifier.isStatic;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

/*
 * Represent the field object, two FieldStruct can only be compared if they are in the same class
 * offset is unique within class
 */
final class FieldStruct implements Comparable<FieldStruct> {
    final String name;
    final Class type;
    final long offset;
    final int modifiers;

    FieldStruct(final Field field) {
        this.name = field.getName();
        this.type = field.getType();
        this.modifiers = field.getModifiers();
        this.offset = isStatic(modifiers) ? UNSAFE.staticFieldOffset(field) : UNSAFE.objectFieldOffset(field);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FieldStruct that = (FieldStruct) o;
        return offset == that.offset;
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

    @Override
    public int compareTo(final FieldStruct f) {
        return (int) (this.offset - f.offset);
    }
}
