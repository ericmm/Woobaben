package woo.ba.ben.core;


import java.util.ArrayList;
import java.util.List;

public class ClassCache {
    private static final int DEFAULT_CACHE_SIZE = 1024 * 1024;
    private static final SimpleMap<Class, ClassStruct> CACHE = new SimpleArrayMap<>(DEFAULT_CACHE_SIZE);
    private static final ClassCache INSTANCE = new ClassCache();

    private ClassCache() {
    }

    public static ClassCache getInstance() {
        return INSTANCE;
    }

    public static boolean isValidClass(final Class realClass) {
        return realClass != null && realClass != Object.class
                && !realClass.isAnnotation() && !realClass.isInterface();
    }

    public ClassStruct put(final Class realClass) {
        if (!isValidClass(realClass)) {
            throw new IllegalArgumentException("Argument is invalid");
        }

        final List<Class> classChain = getClassChain(realClass);
        for (int i = classChain.size() - 1; i >= 0; i--) { //eldest super class except Object.class
            final Class clazz = classChain.get(i);
            if (CACHE.get(clazz) != null) {
                continue;
            }

            final ClassStruct parent = CACHE.get(clazz.getSuperclass());
            final ClassStruct classStruct = new ClassStruct(clazz, parent);
            CACHE.put(clazz, classStruct);
        }

        return CACHE.get(realClass);
    }

    public ClassStruct get(final Class realClass) {
        return CACHE.get(realClass);
    }

    public int size() {
        return CACHE.size();
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
