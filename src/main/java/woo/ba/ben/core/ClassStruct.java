package woo.ba.ben.core;

import com.google.common.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.cache.CacheBuilder.newBuilder;
import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Collections.EMPTY_MAP;
import static java.util.Collections.unmodifiableMap;
import static sun.misc.Unsafe.INVALID_FIELD_OFFSET;
import static woo.ba.ben.core.ConfigReader.getPropertyAsLong;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

final class ClassStruct {
    static final String FIELD_SEPARATOR = "@";
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassStruct.class);
    private static final Cache<Class, ClassStruct> CACHE = newBuilder()
            .maximumSize(getPropertyAsLong("class.cache.size"))
            .build();

    final Class realClass;
    Map<String, FieldStruct> fieldMap;

    private ClassStruct(final Class realClazz) {
        if (realClazz == null || isUnsupportedClass(realClazz)) {
            throw new IllegalArgumentException("The class cannot be null, array, annotation, enum, interface or abstract.");
        }

        realClass = realClazz;
        parseFields(realClazz);
        CACHE.put(realClazz, this);
    }

    static ClassStruct classStruct(final Class realClass) {
        ClassStruct struct = CACHE.getIfPresent(realClass);
        if (struct != null) {
            return struct;
        }
        struct = new ClassStruct(realClass);
        CACHE.put(realClass, struct);
        return struct;
    }

    static Class getObjectClass(final Object obj) {
        return obj instanceof Class ? (Class) obj : obj.getClass();
    }

    static boolean isAnnotationOrEnumOrInterface(final Class clazz) {
        return clazz.isAnnotation() || clazz.isEnum() || clazz.isInterface();
    }

    static long getArrayBlockSize(final Class arrayClass, final int length) {
        if (length < 0) {
            return INVALID_FIELD_OFFSET;
        }
        return UNSAFE.arrayIndexScale(arrayClass) * length;
    }

    static long getArrayStartOffset(final Class arrayClass) {
        return UNSAFE.arrayBaseOffset(arrayClass);
    }

    private static boolean isUnsupportedClass(final Class clazz) {
        return isAnnotationOrEnumOrInterface(clazz) || clazz.isArray() || isAbstract(clazz.getModifiers());
    }

    private static int getFieldCount(final Class realClass) {
        int result = 0;
        Class currentClass = realClass;
        while (currentClass.getSuperclass() != null) { //except Object.class
            result += currentClass.getDeclaredFields().length;
            currentClass = currentClass.getSuperclass();
        }
        return result;
    }

    @Override
    public String toString() {
        return "ClassStruct{" + "realClass=" + realClass + '}';
    }

    @Override
    public boolean equals(final Object o) {
        return this == o || o != null && ClassStruct.class == o.getClass() && realClass.equals(((ClassStruct) o).realClass);
    }

    @Override
    public int hashCode() {
        return realClass.hashCode();
    }

    FieldStruct getField(final String fieldName) {
        return fieldMap.get(fieldName);
    }

    FieldStruct getField(final String fieldName, final Class clazz) {
        return fieldMap.get(getMaskedFieldName(fieldName, clazz));
    }

    private void parseFields(final Class realClass) {
        final int fieldCount = getFieldCount(realClass);
        if (fieldCount == 0) {
            fieldMap = EMPTY_MAP;
            return;
        }

        Field[] declaredFields;
        FieldStruct fieldStruct;
        Class currentClass = realClass;
        fieldMap = new HashMap<>(fieldCount);
        while (currentClass.getSuperclass() != null) { //except Object.class
            declaredFields = currentClass.getDeclaredFields();

            for (int i = declaredFields.length; --i >= 0; ) {
                fieldStruct = new FieldStruct(declaredFields[i]);
                buildFieldMap(fieldStruct, currentClass);
            }
            currentClass = currentClass.getSuperclass();
        }
        fieldMap = unmodifiableMap(fieldMap);
    }

    private void buildFieldMap(final FieldStruct fieldStruct, final Class currentClass) {
        if (fieldMap.containsKey(fieldStruct.name)) {
            final String maskedFieldName = getMaskedFieldName(fieldStruct.name, currentClass);
            LOGGER.info(fieldStruct.name + " already exists on " + realClass.getName() + ", will use [" + maskedFieldName + "] as field name");
            fieldMap.put(maskedFieldName, fieldStruct);
        } else {
            fieldMap.put(fieldStruct.name, fieldStruct);
        }
    }

    private String getMaskedFieldName(final String fieldName, final Class clazz) {
        return fieldName + FIELD_SEPARATOR + clazz.getName();
    }

    /*
    //!!Unsafe - not verified!!
     static long unsafeSizeOf(final Object object) {
        if (object == null) {
            return INVALID_FIELD_OFFSET;
        }
        return UNSAFE.getAddress(unsignedInt(UNSAFE.getInt(object, 4L)) + 12L);
    }
    */
}
