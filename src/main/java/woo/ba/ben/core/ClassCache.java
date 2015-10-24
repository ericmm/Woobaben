package woo.ba.ben.core;


import sun.misc.Unsafe;

import java.util.ArrayList;
import java.util.List;

public class ClassCache {
    private static final int DEFAULT_CACHE_SIZE = 1024 * 10;
    private SimpleMap<Class, ClassStruct> cache = new SimpleArrayMap<>();


    public void put(final Class realClass, final Unsafe unsafe) {
        if (realClass == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        final List<Class> classAndSuperClasses = getSuperClasses(realClass);
        createClassStructs(unsafe, classAndSuperClasses);
        linkParent(classAndSuperClasses);
    }


    private void createClassStructs(final Unsafe unsafe, final List<Class> classAndSuperClasses) {
        for (final Class currentClass : classAndSuperClasses) {
            ClassStruct classStruct = cache.get(currentClass);
            if (classStruct != null) {
                break;
            }
            classStruct = new ClassStruct(currentClass, unsafe);
            cache.put(currentClass, classStruct);
        }
    }

    private static List<Class> getSuperClasses(final Class realClass) {
        final List<Class> superClasses = new ArrayList<>();

        Class currentClass = realClass;
        while (currentClass.getSuperclass() != null) {
            superClasses.add(currentClass);
            currentClass = currentClass.getSuperclass();
        }

        return superClasses;
    }


    private void linkParent(final List<Class> classAndSuperClasses) {
        for (int i = classAndSuperClasses.size() - 1; i >= 0; i--) {
            //eldest super class except Object.class
            Class clazz = classAndSuperClasses.get(i);
            ClassStruct classStruct = cache.get(clazz);
            if (classStruct.getParent() != null) {
                continue;
            }

            //link to parent
            if (clazz.getSuperclass() != Object.class) {
                classStruct.setParent(cache.get(clazz.getSuperclass()));
            }
        }
    }

}
