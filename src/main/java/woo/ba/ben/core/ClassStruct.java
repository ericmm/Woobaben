package woo.ba.ben.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.sort;
import static java.util.Collections.unmodifiableList;
import static sun.misc.Unsafe.INVALID_FIELD_OFFSET;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;
import static woo.ba.ben.core.UnsafeFactory.getTypeSize;

public class ClassStruct {
    public static final String FIELD_SEPARATOR = ".";

    private static final Comparator<FieldStruct> FIELD_STRUCT_OFFSET_COMPARATOR = (f1, f2) -> (int) (f1.offset - f2.offset);

    public final Class realClass;

    private List<FieldStruct> instanceFields;
    private List<FieldStruct> transientFields;
    private Map<String, FieldStruct> fieldMap;

    public ClassStruct(final Class realClass) {
        if (realClass == null) {
            throw new IllegalArgumentException("Input parameter should not be null");
        }

        this.realClass = realClass;

        if (!realClass.isArray()) {
            final int fieldCount = getFieldCount(realClass);
            if (fieldCount > 0) {
                parseFields(realClass, fieldCount);
                sort(instanceFields, FIELD_STRUCT_OFFSET_COMPARATOR);
                instanceFields = unmodifiableList(instanceFields);
                transientFields = unmodifiableList(transientFields);
            }
        }
    }

    public static Class getObjectClass(final Object obj) {
        return obj instanceof Class ? (Class) obj : obj.getClass();
    }

    public FieldStruct getField(final String fieldName) {
        return fieldMap == null ? null : fieldMap.get(fieldName);
    }

    public int getFieldCount() {
        return fieldMap == null ? 0 : fieldMap.size();
    }

    public List<FieldStruct> getInstanceFields() {
        return instanceFields;
    }

    public boolean hasInstanceFields() {
        return instanceFields != null && !instanceFields.isEmpty();
    }

    public List<FieldStruct> getTransientFields() {
        return transientFields;
    }

    public boolean hasTransientFields() {
        return transientFields != null && !transientFields.isEmpty();
    }

    public long getStartOffset() {
        if (realClass.isArray()) {
            return UNSAFE.arrayBaseOffset(realClass);
        }

        if (!hasInstanceFields()) {
            return INVALID_FIELD_OFFSET;
        }

        return instanceFields.get(0).offset;
    }

    public long getInstanceBlockSize() {
        if (!hasInstanceFields()) {
            return INVALID_FIELD_OFFSET;
        }

        final FieldStruct lastFieldStruct = instanceFields.get(instanceFields.size() - 1);
        final long size = lastFieldStruct.offset - instanceFields.get(0).offset;
        return size + getTypeSize(lastFieldStruct.type);
    }

    public long getArrayBlockSize(final int length) {
        if (!realClass.isArray() || length <= 0) {
            return INVALID_FIELD_OFFSET;
        }

        return UNSAFE.arrayIndexScale(realClass) * length;
    }

    /*
    //!!Unsafe - not verified!!
    public static long unsafeSizeOf(final Object object) {
        if (object == null) {
            return INVALID_FIELD_OFFSET;
        }
        return UNSAFE.getAddress(unsignedInt(UNSAFE.getInt(object, 4L)) + 12L);
    }
    */

    @Override
    public String toString() {
        return "ClassStruct{" + "realClass=" + realClass + '}';
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

    private void parseFields(final Class realClass, final int fieldCount) {
        fieldMap = new HashMap<>(fieldCount);
        instanceFields = new ArrayList<>(fieldCount);
        transientFields = new ArrayList<>(fieldCount);

        FieldStruct fieldStruct;
        Field[] declaredFields;
        Class currentClass = realClass;
        while (currentClass.getSuperclass() != null) { //except Object.class
            declaredFields = currentClass.getDeclaredFields();

            for (int i = declaredFields.length; --i >= 0; ) {
                fieldStruct = new FieldStruct(declaredFields[i]);
                if (fieldMap.containsKey(fieldStruct.name)) {
                    fieldMap.put(currentClass.getSimpleName() + FIELD_SEPARATOR + fieldStruct.name, fieldStruct);
                } else {
                    fieldMap.put(fieldStruct.name, fieldStruct);
                }

                if (!fieldStruct.isStatic()) {
                    instanceFields.add(fieldStruct);
                }

                if (fieldStruct.isTransient()) {
                    transientFields.add(fieldStruct);
                }
            }
            currentClass = currentClass.getSuperclass();
        }
    }

    private int getFieldCount(final Class realClass) {
        int result = 0;
        Class currentClass = realClass;
        while (currentClass.getSuperclass() != null) { //except Object.class
            result += currentClass.getDeclaredFields().length;
            currentClass = currentClass.getSuperclass();
        }
        return result;
    }

}
