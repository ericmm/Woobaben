package woo.ba.ben.core;

import java.lang.reflect.Field;
import java.util.*;

public class ClassStruct {
    private static final Comparator<FieldStruct> FIELD_STRUCT_OFFSET_COMPARATOR = new Comparator<FieldStruct>() {
        @Override
        public int compare(final FieldStruct f1, final FieldStruct f2) {
//            if (f1 == f2) {
//                return 0;
//            } else if (f1 == null && f2 != null) {
//                return -1;
//            } else if (f1 != null && f2 == null) {
//                return 1;
//            } else {
            return (int) (f1.offset - f2.offset);
//            }
        }
    };

    public final Class realClass;

    private FieldStruct[] instanceFields;
    private Map<String, FieldStruct> fieldMap;

    public ClassStruct(final Class realClass) {
        if (realClass == null) {
            throw new IllegalArgumentException("Unsupported class: " + realClass);
        }

        this.realClass = realClass;

        final int fieldCount = getFieldCount(realClass);
        if (fieldCount > 0) {
            final List<FieldStruct> instanceFieldList = parseFields(realClass, fieldCount);
            if (instanceFieldList.size() > 0) {
                instanceFields = initInstanceFields(instanceFieldList);
            }
        }
    }

    public static Field getField(Class clazz, String filedName) throws NoSuchFieldException {
        Field[] declaredFields;
        Class currentClass = clazz;
        while (currentClass.getSuperclass() != null) { //except Object.class
            declaredFields = currentClass.getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                if (declaredFields[i].getName().equals(filedName)) {
                    return declaredFields[i];
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        throw new NoSuchFieldException("No filed [" + filedName + "] found on class [" + clazz + "]");
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

    public FieldStruct[] getInstanceFields() {
        return instanceFields;
    }

    public boolean hasInstanceFields() {
        return instanceFields != null;
    }

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

    private FieldStruct[] initInstanceFields(final List<FieldStruct> instanceFieldList) {
        Collections.sort(instanceFieldList, FIELD_STRUCT_OFFSET_COMPARATOR);
        FieldStruct[] instanceFields = new FieldStruct[instanceFieldList.size()];
        return instanceFieldList.toArray(instanceFields);
    }

    private List<FieldStruct> parseFields(final Class realClass, final int fieldCount) {
        fieldMap = new HashMap<>(fieldCount);
        List<FieldStruct> instanceFields = new ArrayList<>(fieldCount);

        FieldStruct fieldStruct;
        Field[] declaredFields;
        Class currentClass = realClass;
        while (currentClass.getSuperclass() != null) { //except Object.class
            declaredFields = currentClass.getDeclaredFields();
            for (int i = declaredFields.length; --i >= 0; ) {
                fieldStruct = new FieldStruct(declaredFields[i]);
                fieldMap.put(fieldStruct.name, fieldStruct);

                if (!fieldStruct.isStatic()) {
                    instanceFields.add(fieldStruct);
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return instanceFields;
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
