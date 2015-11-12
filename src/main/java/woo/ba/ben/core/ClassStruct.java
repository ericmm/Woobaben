package woo.ba.ben.core;

import java.lang.reflect.Field;

public class ClassStruct {
    public final Class realClass;
    public final ClassStruct parent;

    private SimpleMap<String, FieldStruct> fieldMap;

    ClassStruct(final Class realClass, final ClassStruct parent) {
        this.realClass = realClass;
        this.parent = parent;

        parseFields(realClass);
    }

    public String getClassName() {
        return realClass.getName();
    }

    public FieldStruct getDeclaredField(final String fieldName) {
        return fieldMap == null ? null : fieldMap.get(fieldName);
    }

    public FieldStruct getField(final String fieldName) {
        ClassStruct current = this;
        while (current.realClass.getSuperclass() != null) {
            final FieldStruct fieldStruct = current.getDeclaredField(fieldName);
            if (fieldStruct != null) {
                return fieldStruct;
            }
            if (current.parent == null) {
                break;
            }
            current = current.parent;
        }
        return null;
    }

    private void parseFields(final Class currentClass) {
        final Field[] declaredFields = currentClass.getDeclaredFields();
        if (declaredFields.length > 0) {
            fieldMap = new SimpleArrayMap<>(declaredFields.length);
            for (final Field field : declaredFields) {
                fieldMap.put(field.getName(), new FieldStruct(field));
            }
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassStruct that = (ClassStruct) o;

        return realClass.equals(that.realClass);
    }

    @Override
    public int hashCode() {
        return realClass.hashCode();
    }

    @Override
    public String toString() {
        return "ClassStruct{" +
                "realClass=" + realClass +
                ", parent=" + parent + '}';
    }
}
