package woo.ba.ben.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class FieldStruct {
    final String name;
    final Class type;
    final long offset;
    final int modifiers;

    FieldStruct(final Field field) {
        assert field != null;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldStruct that = (FieldStruct) o;

        if (offset != that.offset) return false;
        if (modifiers != that.modifiers) return false;
        if (!name.equals(that.name)) return false;
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
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
