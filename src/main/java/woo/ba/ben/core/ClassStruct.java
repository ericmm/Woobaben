package woo.ba.ben.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.sort;
import static java.util.Collections.unmodifiableList;
import static woo.ba.ben.core.UnsafeFactory.getTypeSize;

public class ClassStruct {
    public static final int OFFSET_NOT_AVAILABLE = -1;
    private static final Comparator<FieldStruct> FIELD_STRUCT_OFFSET_COMPARATOR = new Comparator<FieldStruct>() {
        @Override
        public int compare(final FieldStruct f1, final FieldStruct f2) {
            return (int) (f1.offset - f2.offset);
        }
    };

    public final Class realClass;
    public final ClassStruct parent;

    private SimpleMap<String, FieldStruct> fieldMap;
    private List<FieldStruct> sortedInstanceFields;

    ClassStruct(final Class realClass, final ClassStruct parent) {
        this.realClass = realClass;
        this.parent = parent;

        parseFields(realClass);
        addInherentFieldsFromParent(parent);
        sortFields();
    }

    private static void sortFieldByOffset(final List<FieldStruct> fieldStructs) {
        if (hasFields(fieldStructs)) {
            sort(fieldStructs, FIELD_STRUCT_OFFSET_COMPARATOR);
        }
    }

    private static boolean hasFields(final List<FieldStruct> fields) {
        return fields != null && fields.size() > 0;
    }

    private void addInherentFieldsFromParent(final ClassStruct parent) {
        if (parent != null && parent.getSortedInstanceFields() != null) {
            sortedInstanceFields = getOrCreateList(sortedInstanceFields);
            sortedInstanceFields.addAll(parent.getSortedInstanceFields());
        }
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

    public String getClassName() {
        return realClass.getName();
    }

    public List<FieldStruct> getSortedInstanceFields() {
        return sortedInstanceFields;
    }

    public boolean hasInstanceFields() {
        return hasFields(sortedInstanceFields);
    }

    public long getFristInstanceFieldStartPosition() {
        if (hasInstanceFields()) {
            return sortedInstanceFields.get(0).offset;
        }
        return OFFSET_NOT_AVAILABLE;
    }

    public long getLastInstanceFieldEndPosition() {
        if (hasInstanceFields()) {
            final FieldStruct lastFieldStruct = sortedInstanceFields.get(sortedInstanceFields.size() - 1);
            return lastFieldStruct.offset + getTypeSize(lastFieldStruct.type);
        }
        return OFFSET_NOT_AVAILABLE;
    }

    private void parseFields(final Class currentClass) {
        final Field[] declaredFields = currentClass.getDeclaredFields();
        if (declaredFields.length > 0) {
            FieldStruct fieldStruct;
            fieldMap = new SimpleArrayMap<>(declaredFields.length);
            for (final Field field : declaredFields) {
                fieldStruct = new FieldStruct(field);
                fieldMap.put(field.getName(), fieldStruct);

                if (!fieldStruct.isStatic()) {
                    sortedInstanceFields = getOrCreateList(sortedInstanceFields);
                    sortedInstanceFields.add(fieldStruct);
                }
            }
        }
    }

    private List<FieldStruct> getOrCreateList(final List<FieldStruct> fields) {
        if (fields == null) {
            return new ArrayList<>();
        }
        return fields;
    }

//    public FieldStruct getField(final String fieldName) {
//        int matchedIndex = getMatchedIndex(sortedInstanceFields, fieldName);
//        if (matchedIndex > -1) {
//            return sortedInstanceFields.get(matchedIndex);
//        }
//
//        matchedIndex = getMatchedIndex(sortedStaticFields, fieldName);
//        if (matchedIndex > -1) {
//            return sortedStaticFields.get(matchedIndex);
//        }
//
//        return null;
//    }

//    private int getMatchedIndex(final List<FieldStruct> fieldStructs, final String fieldName) {
//        if (hasFields(fieldStructs)) {
//            FieldStruct struct;
//            for (int i = fieldStructs.size() - 1; i >= 0; i--) {
//                struct = fieldStructs.get(i);
//                if (fieldName.equals(struct.name)) {
//                    return i;
//                }
//            }
//        }
//        return -1;
//    }

    private void sortFields() {
        sortFieldByOffset(sortedInstanceFields);
        if (hasInstanceFields()) {
            sortedInstanceFields = unmodifiableList(sortedInstanceFields);
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
