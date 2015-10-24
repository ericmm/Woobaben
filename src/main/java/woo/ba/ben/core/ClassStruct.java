package woo.ba.ben.core;


import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class ClassStruct {
    public final String name;
    public final Class realClass;
    private ClassStruct parent;
    private SimpleMap<String, FieldStruct> fieldMap;

    public ClassStruct(final Class realClass, final Unsafe unsafe) {
        if (realClass == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        this.name = realClass.getName();
        this.realClass = realClass;

        parseFields(realClass, unsafe);
    }


    public FieldStruct getDeclaredField(final String fieldName) {
        return fieldMap == null ? null : fieldMap.get(fieldName);
    }

    public FieldStruct getField(final String fieldName) {
        ClassStruct current = this;
        while (current.parent != null) {
            FieldStruct fieldStruct = current.getDeclaredField(fieldName);
            if (fieldStruct != null) {
                return fieldStruct;
            }
            current = current.parent;
        }
        return null;
    }

    public ClassStruct getParent() {
        return parent;
    }

    public void setParent(ClassStruct parent) {
        this.parent = parent;
    }

    private void parseFields(final Class currentClass, final Unsafe unsafe) {
        final Field[] declaredFields = currentClass.getDeclaredFields();
        if (declaredFields.length > 0) {
            fieldMap = new SimpleArrayMap<>(declaredFields.length);
            for (final Field field : declaredFields) {
                fieldMap.put(field.getName(), new FieldStruct(field, unsafe));
            }
        }
    }

}
