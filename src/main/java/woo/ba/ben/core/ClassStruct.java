package woo.ba.ben.core;


import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ClassStruct {
    public static final ClassStruct ROOT_CLASS_STRUCT = new ClassStruct(Object.class, null);

    public final String name;
    public final Class realClass;
    private ClassStruct parent;
    private SimpleMap<String, FieldStruct> fieldMap;

    private ClassStruct(final Class realClass, final Unsafe unsafe) {
        if (realClass == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        this.name = realClass.getName();
        this.realClass = realClass;

        if (Object.class != realClass) {
            processClass(realClass, unsafe);
        }
    }

    public static void put(final SimpleMap<Class, ClassStruct> classCache, final Class realClass, final Unsafe unsafe) {
        if (classCache == null || realClass == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        final List<Class> classAndSuperClasses = getSuperClasses(realClass);
        createClassStructs(classCache, unsafe, classAndSuperClasses);
        linkParent(classCache, classAndSuperClasses);
    }

    public FieldStruct getDeclaredField(final String fieldName) {
        return fieldMap == null ? null : fieldMap.get(fieldName);
    }

    public FieldStruct getField(final String fieldName) {
        ClassStruct current = this;
        while (current.getParent() != null) {
            FieldStruct fieldStruct = current.getDeclaredField(fieldName);
            if (fieldStruct != null) {
                return fieldStruct;
            }

            current = current.getParent();
        }

        return null;
    }


    public ClassStruct getParent() {
        return parent;
    }

    private static void linkParent(final SimpleMap<Class, ClassStruct> classCache, final List<Class> classAndSuperClasses) {
        for (int i = classAndSuperClasses.size() - 1; i >= 0; i--) {
            //eldest super class except Object.class
            Class clazz = classAndSuperClasses.get(i);
            ClassStruct classStruct = classCache.get(clazz);
            if (classStruct.parent != null) {
                continue;
            }

            //link to parent
            if (clazz.getSuperclass() == Object.class) {
                classStruct.parent = ROOT_CLASS_STRUCT;
            } else {
                classStruct.parent = classCache.get(clazz.getSuperclass());
            }
        }
    }

    private static void createClassStructs(final SimpleMap<Class, ClassStruct> classCache, final Unsafe unsafe, final List<Class> classAndSuperClasses) {
        for (final Class currentClass : classAndSuperClasses) {
            ClassStruct classStruct = classCache.get(currentClass);
            if (classStruct != null) {
                break;
            }
            classStruct = new ClassStruct(currentClass, unsafe);
            classCache.put(currentClass, classStruct);
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

    private void processClass(final Class currentClass, final Unsafe unsafe) {
        final Field[] declaredFields = currentClass.getDeclaredFields();
        if (declaredFields.length > 0) {
            fieldMap = new SimpleArrayMap<>(declaredFields.length);
            for (final Field field : declaredFields) {
                fieldMap.put(field.getName(), new FieldStruct(field, unsafe));
            }
        }
    }

}
