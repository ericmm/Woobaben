package woo.ba.ben.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.reflect.Modifier.isStatic;

class ClassStruct {
    final String className;
    private long firstInstanceFieldStartOffset = Long.MAX_VALUE;
    private long lastInstanceFieldEndOffset = -1;
    private Map<String, FieldStruct> instanceFieldMap;

    ClassStruct(final Class realClass) {
        if (!isValidClass(realClass)) {
            throw new IllegalArgumentException("Unsupported class: " + realClass);
        }

        this.className = realClass.getName();

        final List<Class> classChain = getClassChain(realClass);
        final int instanceFieldCount = getInstanceFieldCount(classChain);
        if (instanceFieldCount > 0) {
            instanceFieldMap = new HashMap<>(instanceFieldCount);
            processClassChain(classChain);
        }
    }

    FieldStruct getInstanceField(final String fieldName) {
        return instanceFieldMap == null ? null : instanceFieldMap.get(fieldName);
    }

    long getFirstInstanceFieldStartOffset() {
        return firstInstanceFieldStartOffset;
    }

    long getLastInstanceFieldEndOffset() {
        return lastInstanceFieldEndOffset;
    }

    boolean hasInstanceFields() {
        return lastInstanceFieldEndOffset > 0;
    }

    int getFieldCount() {
        return instanceFieldMap == null ? 0 : instanceFieldMap.size();
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

    private void parseFields(final Class currentClass) {
        FieldStruct fieldStruct;
        final Field[] declaredFields = currentClass.getDeclaredFields();
        for (int i = declaredFields.length; --i >= 0; ) {
            if (!isStatic(declaredFields[i].getModifiers())) {
                fieldStruct = new FieldStruct(declaredFields[i]);
                instanceFieldMap.put(declaredFields[i].getName(), fieldStruct);

                if (fieldStruct.offset < firstInstanceFieldStartOffset) {
                    firstInstanceFieldStartOffset = fieldStruct.offset;
                }

                if (fieldStruct.offset > lastInstanceFieldEndOffset) {
                    lastInstanceFieldEndOffset = fieldStruct.offset;
                }
            }
        }
    }

    private void processClassChain(final List<Class> classChain) {
        final int length = classChain.size();
        for (int i = length; --i >= 0; ) {
            parseFields(classChain.get(i));
        }
    }

    private int getInstanceFieldCount(final List<Class> classChain) {
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
        return realClass != null && realClass != Object.class
                && !realClass.isAnnotation() && !realClass.isInterface();
    }
}
