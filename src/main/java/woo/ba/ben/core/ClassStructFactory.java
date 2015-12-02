package woo.ba.ben.core;


import java.util.ArrayList;
import java.util.List;

public class ClassStructFactory {
    private static final int DEFAULT_CACHE_SIZE = 1024 * 1024;
    private static final SimpleMap<Class, ClassStruct> CACHE = new SimpleArrayMap<>(DEFAULT_CACHE_SIZE);

    private ClassStructFactory() {
    }

    public static boolean isValidClass(final Class realClass) {
        return realClass != null && realClass != Object.class
                && !realClass.isAnnotation() && !realClass.isInterface();
    }

    public static ClassStruct get(final Class realClass) {
        final ClassStruct classStruct = CACHE.get(realClass);
        if (classStruct != null) {
            return classStruct;
        }

        processClass(realClass);
        return CACHE.get(realClass);
    }

    private static void processClass(final Class realClass) {
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
            CACHE.put(clazz, classStruct);
        }
    }

    private static ClassStruct getParentClassStruct(final Class clazz) {
        final Class superclass = clazz.getSuperclass();
        return superclass == Object.class ? null : CACHE.get(superclass);
    }

    private static List<Class> getClassChain(final Class realClass) {
        final List<Class> superClasses = new ArrayList<>();

        Class currentClass = realClass;
        while (currentClass.getSuperclass() != null) { //except Object.class
            superClasses.add(currentClass);
            currentClass = currentClass.getSuperclass();
        }
        return superClasses;
    }
}
