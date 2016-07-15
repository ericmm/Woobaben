package woo.ba.ben.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public class FieldStruct {
    public final String name;
    public final Class type;
    public final long offset;
    private final int modifiers;

    public FieldStruct(final Field field) {
        this.name = field.getName();
        this.type = field.getType();
        this.modifiers = field.getModifiers();
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

    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }

    public boolean isTransient() {
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
        final StringBuilder sb = new StringBuilder("FieldStruct{");
        sb.append("name='").append(name).append('\'');
        sb.append(", type=").append(type);
        sb.append(", offset=").append(offset);
        sb.append(", modifiers=").append(modifiers);
        sb.append('}');
        return sb.toString();
    }
}
