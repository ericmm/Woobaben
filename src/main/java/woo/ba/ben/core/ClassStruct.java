package woo.ba.ben.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.unmodifiableList;

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
    private List<FieldStruct> sortedInstanceFields = new ArrayList<>();
    private List<FieldStruct> sortedStaticFields = new ArrayList<>();

    ClassStruct(final Class realClass, final ClassStruct parent) {
        this.realClass = realClass;
        this.parent = parent;

        parseFields(realClass);
        addFieldsFromParents(parent);
        sortFields();
    }

    private static void sortFieldByOffset(final List<FieldStruct> fieldStructs) {
        if (fieldStructs.size() > 1) {
            Collections.sort(fieldStructs, FIELD_STRUCT_OFFSET_COMPARATOR);
        }
    }

    private void addFieldsFromParents(final ClassStruct parent) {
        ClassStruct parentClassStruct = parent;
        if(parent != null) {
            sortedStaticFields.addAll(parent.getSortedStaticFields());
        }

        while (parentClassStruct != null) {
            sortedInstanceFields.addAll(parentClassStruct.getSortedInstanceFields());

            parentClassStruct = parentClassStruct.parent;
        }
    }

    public FieldStruct getField(final String fieldName) {
        int matchedIndex = getMatchedIndex(sortedInstanceFields, fieldName);
        if (matchedIndex > -1) {
            return sortedInstanceFields.get(matchedIndex);
        }

        matchedIndex = getMatchedIndex(sortedStaticFields, fieldName);
        if (matchedIndex > -1) {
            return sortedStaticFields.get(matchedIndex);
        }

        return null;
    }

    public String getClassName() {
        return realClass.getName();
    }

    public List<FieldStruct> getSortedInstanceFields() {
        return sortedInstanceFields;
    }

    public List<FieldStruct> getSortedStaticFields() {
        return sortedStaticFields;
    }

    public boolean hasInstanceFields() {
        return sortedInstanceFields.size() > 0;
    }

    public boolean hasStaticFields() {
        return sortedStaticFields.size() > 0;
    }

    public long getMinOffsetForInstanceField() {
        if (hasInstanceFields()) {
            return sortedInstanceFields.get(0).offset;
        }
        return OFFSET_NOT_AVAILABLE;
    }

    public long getMaxOffsetForInstanceField() {
        if (hasInstanceFields()) {
            return sortedInstanceFields.get(sortedInstanceFields.size() - 1).offset;
        }
        return OFFSET_NOT_AVAILABLE;
    }

    public long getMinOffsetForStaticField() {
        if (hasInstanceFields()) {
            return sortedStaticFields.get(0).offset;
        }
        return OFFSET_NOT_AVAILABLE;
    }

    public long getMaxOffsetForStaticField() {
        if (hasInstanceFields()) {
            return sortedStaticFields.get(sortedStaticFields.size() - 1).offset;
        }
        return OFFSET_NOT_AVAILABLE;
    }

    private void parseFields(final Class currentClass) {
        final Field[] declaredFields = currentClass.getDeclaredFields();
        FieldStruct fieldStruct;
        for (final Field field : declaredFields) {
            fieldStruct = new FieldStruct(field);
            if (!fieldStruct.isStatic()) {
                sortedInstanceFields.add(fieldStruct);
            } else {
                sortedStaticFields.add(fieldStruct);
            }
        }
    }

    private int getMatchedIndex(final List<FieldStruct> fieldStructs, final String fieldName) {
        if (fieldStructs.size() > 0) {
            FieldStruct struct;
            for (int i = fieldStructs.size() - 1; i >= 0; i--) {
                struct = fieldStructs.get(i);
                if (fieldName.equals(struct.name)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void sortFields() {
        sortFieldByOffset(sortedInstanceFields);
        sortFieldByOffset(sortedStaticFields);
        sortedInstanceFields = unmodifiableList(sortedInstanceFields);
        sortedStaticFields = unmodifiableList(sortedStaticFields);
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
