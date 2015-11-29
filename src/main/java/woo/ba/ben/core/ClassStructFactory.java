package woo.ba.ben.core;


import java.util.ArrayList;
import java.util.List;

import static woo.ba.ben.core.ClassStruct.OFFSET_NOT_AVAILABLE;

public class ClassStructFactory {
    private static final int DEFAULT_CACHE_SIZE = 1024 * 1024;
    private static final SimpleMap<Class, ClassStruct> CACHE = new SimpleArrayMap<>(DEFAULT_CACHE_SIZE);
    private static final ClassStructFactory INSTANCE = new ClassStructFactory();

    private ClassStructFactory() {
    }

    public static ClassStructFactory getInstance() {
        return INSTANCE;
    }

    public static boolean isValidClass(final Class realClass) {
        return realClass != null && realClass != Object.class
                && !realClass.isAnnotation() && !realClass.isInterface();
    }

    public ClassStruct get(final Class realClass) {
        final ClassStruct classStruct = CACHE.get(realClass);
        if (classStruct != null) {
            return classStruct;
        }

        processClass(realClass);
        return CACHE.get(realClass);
    }

    private void processMinMaxOffset(final ClassStruct classStruct) {
        final List<ClassStruct> parentClassStructs = getParentClassStructs(classStruct);
        classStruct.setMinimumOffset(getMinimumOffsetFromClassChain(parentClassStructs));
        classStruct.setMaximumOffset(getMaximumOffsetFromClassChain(parentClassStructs));
    }

    private long getMinimumOffsetFromClassChain(final List<ClassStruct> parentClassStructs) {
        long minimumOffset = OFFSET_NOT_AVAILABLE;
        for (int i = parentClassStructs.size() - 1; i >= 0; i--) { //oldest super class first
            final ClassStruct struct = parentClassStructs.get(i);
            if (!struct.hasInstanceFields()) {
                continue;
            }

            final FieldStruct firstFieldStructOnOldest = struct.getSortedInstanceFields().get(0);
            minimumOffset = firstFieldStructOnOldest.offset;
            break;
        }
        return minimumOffset;
    }

    private long getMaximumOffsetFromClassChain(final List<ClassStruct> parentClassStructs) {
        long maximumOffset = OFFSET_NOT_AVAILABLE;
        for (int i = 0; i < parentClassStructs.size(); i++) { //youngest class first
            final ClassStruct struct = parentClassStructs.get(i);
            if (!struct.hasInstanceFields()) {
                continue;
            }

            final FieldStruct lastFieldStructOnYoungest = struct.getSortedInstanceFields().get(struct.getSortedInstanceFields().size() - 1);
            maximumOffset = lastFieldStructOnYoungest.offset;
            break;
        }
        return maximumOffset;
    }

    private List<ClassStruct> getParentClassStructs(final ClassStruct classStruct) {
        final List<ClassStruct> parentClassStructs = new ArrayList<>();

        ClassStruct currentClassStruct = classStruct;
        parentClassStructs.add(currentClassStruct);
        while (currentClassStruct.parent != null) {
            parentClassStructs.add(currentClassStruct.parent);
            currentClassStruct = currentClassStruct.parent;
        }
        return parentClassStructs;
    }

    private void processClass(final Class realClass) {
        if (!isValidClass(realClass)) {
            throw new IllegalArgumentException("Argument is invalid");
        }

        final List<Class> classChain = getClassChain(realClass);

        Class clazz;
        ClassStruct parentClassStruct, classStruct;
        for (int i = classChain.size() - 1; i >= 0; i--) { //eldest super class except Object.class
            clazz = classChain.get(i);
            if (CACHE.get(clazz) != null) {
                continue;
            }

            parentClassStruct = getParentClassStruct(clazz);
            classStruct = new ClassStruct(clazz, parentClassStruct);
            processMinMaxOffset(classStruct);
            CACHE.put(clazz, classStruct);
        }
    }

    private ClassStruct getParentClassStruct(final Class clazz) {
        final Class superclass = clazz.getSuperclass();
        return superclass == Object.class ? null : CACHE.get(superclass);
    }

    private List<Class> getClassChain(final Class realClass) {
        final List<Class> superClasses = new ArrayList<>();

        Class currentClass = realClass;
        while (currentClass.getSuperclass() != null) { //except Object.class
            superClasses.add(currentClass);
            currentClass = currentClass.getSuperclass();
        }
        return superClasses;
    }
}
