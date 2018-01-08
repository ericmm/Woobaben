package woo.ba.ben.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static woo.ba.ben.core.MethodHandleUtils.*;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

/*
 * Represent the field object
 *
 */
final class FieldStruct {
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldStruct.class);

    final String name;
    final Class type;
    final long offset;
    final int modifiers;
    final MethodHandle getMH;
    final MethodHandle setMH;

    FieldStruct(final Field field) {
        this.name = field.getName();
        this.type = field.getType();
        this.modifiers = field.getModifiers();
        this.offset = isStatic() ? UNSAFE.staticFieldOffset(field) : UNSAFE.objectFieldOffset(field);
        this.getMH = findFieldGetter(field.getDeclaringClass());
        this.setMH = findFieldSetter(field.getDeclaringClass());
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
        return "FieldStruct{name='" + name +
                "', type=" + type +
                ", offset=" + offset +
                ", modifiers=" + modifiers +
                ", getMH=" + getMH +
                ", setMH=" + setMH +
                '}';
    }

    private MethodHandle findFieldSetter(final Class<?> fieldOwner) {
        try {
            return isStatic()
                    ? setter(fieldOwner, name, type)
                    : staticSetter(fieldOwner, name, type);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.warn("Cannot find setter for field [{}] of type [{}] on class [{}]", name, type, fieldOwner);
            return null;
        }
    }

    private MethodHandle findFieldGetter(final Class<?> fieldOwner) {
        try {
            return isStatic()
                    ? getter(fieldOwner, name, type)
                    : staticGetter(fieldOwner, name, type);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.warn("Cannot find getter for field [{}] of type [{}] on class [{}]", name, type, fieldOwner);
            return null;
        }
    }
}
