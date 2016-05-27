package woo.ba.ben.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassStruct {
    public final String className;

    private FieldStruct[] instanceFields;
    private long firstInstanceFieldStartOffset = Long.MAX_VALUE;
    private long lastInstanceFieldEndOffset = -1;
    private Map<String, FieldStruct> fieldMap;

    ClassStruct(final Class realClass) {
        if (!isValidClass(realClass)) {
            throw new IllegalArgumentException("Unsupported class: " + realClass);
        }

        this.className = realClass.getName();

        final List<Class> classChain = getClassChain(realClass);
        final int fieldCount = getFieldCount(classChain);
        if (fieldCount > 0) {
            fieldMap = new HashMap<>(fieldCount);
            final int instanceFieldCount = processClassChain(classChain);
            initInstanceFields(instanceFieldCount);
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

    public FieldStruct getField(final String fieldName) {
        return fieldMap == null ? null : fieldMap.get(fieldName);
    }

    public long getFirstInstanceFieldStartOffset() {
        return firstInstanceFieldStartOffset;
    }

    public long getLastInstanceFieldEndOffset() {
        return lastInstanceFieldEndOffset;
    }

    public boolean hasInstanceFields() {
        return lastInstanceFieldEndOffset > 0;
    }

    public int getFieldCount() {
        return fieldMap == null ? 0 : fieldMap.size();
    }

    public FieldStruct[] getInstanceFields() {
        return instanceFields;
    }

    @Override
    public String toString() {
        return "ClassStruct{" + "className=" + className + '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ClassStruct that = (ClassStruct) o;
        return className.equals(that.className);
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }

    private void initInstanceFields(final int totalInstanceFieldCount) {
        instanceFields = new FieldStruct[totalInstanceFieldCount];
        int index = 0;
        for (final FieldStruct fieldStruct : fieldMap.values()) {
            if (!fieldStruct.isStatic()) {
                instanceFields[index++] = fieldStruct;
            }
        }
    }

    private int parseFields(final Class currentClass) {
        int instanceFieldCount = 0;
        FieldStruct fieldStruct;
        final Field[] declaredFields = currentClass.getDeclaredFields();

        for (int i = declaredFields.length; --i >= 0; ) {
            fieldStruct = new FieldStruct(declaredFields[i]);
            fieldMap.put(fieldStruct.name, fieldStruct);

            if (!fieldStruct.isStatic()) {
                instanceFieldCount++;

                if (fieldStruct.offset < firstInstanceFieldStartOffset) {
                    firstInstanceFieldStartOffset = fieldStruct.offset;
                }

                if (fieldStruct.offset > lastInstanceFieldEndOffset) {
                    lastInstanceFieldEndOffset = fieldStruct.offset;
                }
            }
        }
        return instanceFieldCount;
    }

    private int processClassChain(final List<Class> classChain) {
        int instanceFieldCount = 0;
        final int length = classChain.size();
        for (int i = length; --i >= 0; ) {
            instanceFieldCount += parseFields(classChain.get(i));
        }
        return instanceFieldCount;
    }

    private int getFieldCount(final List<Class> classChain) {
        int result = 0;
        final int length = classChain.size();
        for (int i = length; --i >= 0; ) {
            result += classChain.get(i).getDeclaredFields().length;
        }
        return result;
    }

    private List<Class> getClassChain(final Class realClass) {
        final List<Class> classChain = new ArrayList<>();

        Class currentClass = realClass;
        while (currentClass.getSuperclass() != null) { //except Object.class
            classChain.add(currentClass);
            currentClass = currentClass.getSuperclass();
        }
        return classChain;
    }

    private boolean isValidClass(final Class realClass) {
        return realClass != null && realClass.isAnnotationPresent(CacheAware.class);
    }
}
