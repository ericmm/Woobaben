package woo.ba.ben.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class ClassStruct {
    public final Class realClass;
    public final ClassStruct parent;

    private SimpleMap<String, FieldStruct> fieldMap;
    private List<FieldStruct> sortedInstanceFields;

    ClassStruct(final Class realClass, final ClassStruct parent) {
        this.realClass = realClass;
        this.parent = parent;

        parseFields(realClass);
        sortFields(sortedInstanceFields);

        if(sortedInstanceFields != null) {
            sortedInstanceFields = unmodifiableList(sortedInstanceFields);
        }
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
            sortedInstanceFields = new ArrayList<>(declaredFields.length);

            FieldStruct fieldStruct;
            for (final Field field : declaredFields) {
                fieldStruct = new FieldStruct(field);
                fieldMap.put(field.getName(), fieldStruct);

                if(!fieldStruct.isStatic()) {
                    sortedInstanceFields.add(fieldStruct);
                }
            }
        }
    }

    private void sortFields(final List<FieldStruct> instanceFields) {
        if (instanceFields != null && instanceFields.size() > 1) {
            Collections.sort(instanceFields, new Comparator<FieldStruct>() {
                @Override
                public int compare(final FieldStruct o1, final FieldStruct o2) {
                    return (int) (o1.offset - o2.offset);
                }
            });
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ClassStruct that = (ClassStruct) o;

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
