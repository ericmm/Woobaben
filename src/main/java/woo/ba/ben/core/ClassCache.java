package woo.ba.ben.core;


import java.util.ArrayList;
import java.util.List;

public class ClassCache {
    private static final int DEFAULT_CACHE_SIZE = 1024 * 1024;
    private static final float FILL_FACTOR = 0.9f;
    private static final SimpleMap<Class, ClassStruct> CACHE = new SimpleArrayMap<>(DEFAULT_CACHE_SIZE, FILL_FACTOR);
    private static final ClassCache CLASS_CACHE = new ClassCache();

    private ClassCache() {
    }

    public static ClassCache getInstance() {
        return CLASS_CACHE;
    }

    public ClassStruct put(final Class realClass) {
        if (!isValidClass(realClass)) {
            throw new IllegalArgumentException("Argument is invalid");
        }

        final ClassStruct classStruct = CACHE.get(realClass);
        if (classStruct != null) {
            return classStruct;
        }

        final List<Class> classChain = getClassChain(realClass);
        createClassStruct(classChain);
        linkParent(classChain);

        return CACHE.get(realClass);
    }

    public ClassStruct get(final Class realClass) {
        return CACHE.get(realClass);
    }

    public ClassStruct remove(final Class realClass) {
        //TODO someone may remove the parent
        return CACHE.remove(realClass);
    }

    public int size() {
        return CACHE.size();
    }

    public static boolean isValidClass(final Class realClass) {
        return realClass != null && realClass != Object.class
                && !realClass.isAnnotation() && !realClass.isInterface();
    }

    private void createClassStruct(final List<Class> classAndSuperClasses) {
        for (final Class currentClass : classAndSuperClasses) {
            ClassStruct classStruct = CACHE.get(currentClass);
            if (classStruct != null) {
                break;
            }
            classStruct = new ClassStruct(currentClass);
            CACHE.put(currentClass, classStruct);
        }
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

    private void linkParent(final List<Class> classChain) {
        for (int i = classChain.size() - 1; i >= 0; i--) {
            //eldest super class except Object.class
            final Class clazz = classChain.get(i);
            final ClassStruct classStruct = CACHE.get(clazz);
            if (classStruct.getParent() != null) {
                continue;
            }

            //link to parent
            if (clazz.getSuperclass() != Object.class) {
                classStruct.setParent(CACHE.get(clazz.getSuperclass()));
            }
        }
    }

}
