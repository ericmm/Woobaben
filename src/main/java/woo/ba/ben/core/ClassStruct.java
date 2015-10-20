package woo.ba.ben.core;


import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class ClassStruct {
    public static final ClassStruct ROOT_CLASS = new ClassStruct();

    public final String name;
    public final Class realClass;
    private ClassStruct parent;

    private SimpleMap<String, FieldStruct> fieldMap;

    private ClassStruct() {
        this.name = Object.class.getName();
        this.realClass = Object.class;
        this.parent = null;
    }

    private ClassStruct(final Class realClass, final Unsafe unsafe) {
        if (realClass == null || realClass == Object.class) {
            throw new IllegalArgumentException("Argument is null or class is Object.class");
        }

        this.name = realClass.getName();
        this.realClass = realClass;

        processClass(realClass, unsafe);
    }

    public static void put(final SimpleMap<Class, ClassStruct> classCache, final Class realClass, final Unsafe unsafe) {
        if (classCache == null || realClass == null || realClass == Object.class) {
            throw new IllegalArgumentException("Argument is null or class is Object.class");
        }

        //get parent class list

        //process from the oldest parent


//        Class currentClass = realClass;
//        while (currentClass.getSuperclass() != null) { // don't process Object.class
//            if (classCache.get(currentClass) != null) {
//                return;
//            }
//
//            final ClassStruct classStruct = new ClassStruct(currentClass, unsafe);
//            ClassStruct parentClassStruct = classCache.get(currentClass.getSuperclass());
//            if(parentClassStruct == null) {
//                parentClassStruct = new ClassStruct(currentClass.getSuperclass(), unsafe);
//            }
//            classStruct.parent = parentClassStruct;
//
//            classCache.put(currentClass, classStruct);
//            currentClass = currentClass.getSuperclass();
//        }

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

    public FieldStruct getField(final String fieldName) {
        return fieldMap == null ? null : fieldMap.get(fieldName);
    }

    public ClassStruct getParent() {
        return parent;
    }
}
