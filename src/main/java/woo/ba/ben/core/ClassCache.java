package woo.ba.ben.core;


import java.util.ArrayList;
import java.util.List;

public class ClassCache {
    private static final int DEFAULT_CACHE_SIZE = 1024 * 10;
    private static final SimpleMap<Class, ClassStruct> CACHE = new SimpleArrayMap<>(DEFAULT_CACHE_SIZE);

    public void put(final Class realClass) {
        if (realClass == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        final List<Class> classAndSuperClasses = getSuperClasses(realClass);
        createClassStructs(classAndSuperClasses);
        linkParent(classAndSuperClasses);
    }


    private void createClassStructs(final List<Class> classAndSuperClasses) {
        for (final Class currentClass : classAndSuperClasses) {
            ClassStruct classStruct = CACHE.get(currentClass);
            if (classStruct != null) {
                break;
            }
            classStruct = new ClassStruct(currentClass);
            CACHE.put(currentClass, classStruct);
        }
    }

    private static List<Class> getSuperClasses(final Class realClass) {
        final List<Class> superClasses = new ArrayList<>();

        Class currentClass = realClass;
        while (currentClass.getSuperclass() != null) { //except Object.class
            superClasses.add(currentClass);
            currentClass = currentClass.getSuperclass();
        }

        return superClasses;
    }


    private void linkParent(final List<Class> classAndSuperClasses) {
        for (int i = classAndSuperClasses.size() - 1; i >= 0; i--) {
            //eldest super class except Object.class
            Class clazz = classAndSuperClasses.get(i);
            ClassStruct classStruct = CACHE.get(clazz);
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
