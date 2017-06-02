package woo.ba.ben.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;
import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.EMPTY_MAP;
import static java.util.Collections.sort;
import static java.util.Collections.unmodifiableList;
import static sun.misc.Unsafe.INVALID_FIELD_OFFSET;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;
import static woo.ba.ben.core.UnsafeFactory.getTypeSize;

final class ClassStruct {
    static final String FIELD_SEPARATOR = "@";

    //TODO: think about GC strategy, allow unused Classes to be cleaned
    private static final Map<Class, ClassStruct> CACHE = new HashMap<>(1024);
    private static List<FieldStruct> staticFields;

    final Class realClass;
    private List<FieldStruct> instanceFields;
    private List<FieldStruct> transientFields;
    private Map<String, FieldStruct> fieldMap;

    private ClassStruct(final Class realClass) {
        if (realClass == null || realClass.isArray() || isAnnotationOrEnumOrInterface(realClass)) {
            throw new IllegalArgumentException("Class cannot be null, Array, Annotation, Enum or Interface.");
        }

        this.realClass = realClass;
        parseFields(realClass);
        sortAndProtectFields();
        CACHE.put(realClass, this);
    }

    // not thread-safe, but it's acceptable
    static ClassStruct classStruct(final Class realClass) {
        if (CACHE.containsKey(realClass)) {
            return CACHE.get(realClass);
        }
        final ClassStruct struct = new ClassStruct(realClass);
        CACHE.put(realClass, struct);
        return struct;
    }

    static Class getObjectClass(final Object obj) {
        return obj instanceof Class ? (Class) obj : obj.getClass();
    }

    static boolean isAnnotationOrEnumOrInterface(final Class clazz) {
        return clazz.isAnnotation() || clazz.isEnum() || clazz.isInterface();
    }

    private static int getFieldCount(final Class realClass) {
        int result = 0;
        Class currentClass = realClass;
        while (currentClass.getSuperclass() != null) { //except Object.class
            result += currentClass.getDeclaredFields().length;
            currentClass = currentClass.getSuperclass();
        }
        return result;
    }

    static long getArrayBlockSize(final Class arrayClass, final int length) {
        if (length < 0) {
            return INVALID_FIELD_OFFSET;
        }
        return UNSAFE.arrayIndexScale(arrayClass) * length;
    }

    static long getArrayStartOffset(final Class arrayClass) {
        return UNSAFE.arrayBaseOffset(arrayClass);
    }

    FieldStruct getField(final String fieldName) {
        return fieldMap.get(fieldName);
    }

    List<FieldStruct> getInstanceFields() {
        return instanceFields;
    }

    boolean hasInstanceFields() {
        return !instanceFields.isEmpty();
    }

    long getStartOffset() {
        if (instanceFields.isEmpty()) {
            return INVALID_FIELD_OFFSET;
        }

        return instanceFields.get(0).offset;
    }

    long getInstanceBlockSize() {
        if (instanceFields.isEmpty()) {
            return INVALID_FIELD_OFFSET;
        }

        final FieldStruct lastFieldStruct = instanceFields.get(instanceFields.size() - 1);
        final long size = lastFieldStruct.offset - instanceFields.get(0).offset;
        return size + getTypeSize(lastFieldStruct.type);
    }

    @Override
    public String toString() {
        return "ClassStruct{" + "realClass=" + realClass + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final ClassStruct that = (ClassStruct) o;
        return realClass.equals(that.realClass);
    }

    @Override
    public int hashCode() {
        return realClass.hashCode();
    }

    private void parseFields(final Class realClass) {
        final int fieldCount = getFieldCount(realClass);
        if (fieldCount == 0) {
            createEmptyCollections();
            return;
        }

        createCollections(fieldCount);

        FieldStruct fieldStruct;
        Field[] declaredFields;
        Class currentClass = realClass;
        while (currentClass.getSuperclass() != null) { //except Object.class
            declaredFields = currentClass.getDeclaredFields();

            for (int i = declaredFields.length; --i >= 0; ) {
                fieldStruct = new FieldStruct(declaredFields[i]);
                if (fieldMap.containsKey(fieldStruct.name)) {
                    fieldMap.put(fieldStruct.name + FIELD_SEPARATOR + currentClass.getName(), fieldStruct);
                } else {
                    fieldMap.put(fieldStruct.name, fieldStruct);
                }

                if (isStatic(fieldStruct.modifiers)) {
                    staticFields.add(fieldStruct);
                } else {
                    instanceFields.add(fieldStruct);
                }

                if (isTransient(fieldStruct.modifiers)) {
                    transientFields.add(fieldStruct);
                }
            }
            currentClass = currentClass.getSuperclass();
        }
    }

    private void createCollections(final int fieldCount) {
        fieldMap = new HashMap<>(fieldCount);
        staticFields = new ArrayList<>();
        instanceFields = new ArrayList<>(fieldCount);
        transientFields = new ArrayList<>();
    }

    private void createEmptyCollections() {
        fieldMap = EMPTY_MAP;
        staticFields = EMPTY_LIST;
        instanceFields = EMPTY_LIST;
        transientFields = EMPTY_LIST;
    }

    private void sortAndProtectFields() {
        sort(staticFields);
        sort(instanceFields);
        sort(transientFields);

        staticFields = unmodifiableList(staticFields);
        instanceFields = unmodifiableList(instanceFields);
        transientFields = unmodifiableList(transientFields);
    }

    /*
    //!!Unsafe - not verified!!
     static long unsafeSizeOf(final Object object) {
        if (object == null) {
            return INVALID_FIELD_OFFSET;
        }
        return UNSAFE.getAddress(unsignedInt(UNSAFE.getInt(object, 4L)) + 12L);
    }
    */
}
