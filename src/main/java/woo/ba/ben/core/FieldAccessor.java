package woo.ba.ben.core;


import java.lang.reflect.Field;

import static java.lang.reflect.Modifier.isStatic;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public class FieldAccessor {
    private FieldAccessor() {
    }

    //Boolean
    public static boolean getBoolean(final Object obj, final String field) throws NoSuchFieldException {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            return UNSAFE.getBoolean(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
        }

        final Field fieldObj = getFieldObj(objClass, field);
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getBoolean(objClass, UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getBoolean(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static boolean getBooleanArrayElementAt(final Object obj, final String field, final int index) {
        final boolean[] array = (boolean[]) getArrayObject(obj, field, boolean.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public static void setBoolean(final Object obj, final String field, final boolean value) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            UNSAFE.putBoolean(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
        } else {
            final Field fieldObj = getFieldObj(objClass, field);
            if (isStatic(fieldObj.getModifiers())) {
                UNSAFE.putBoolean(objClass, UNSAFE.staticFieldOffset(fieldObj), value);
            } else {
                UNSAFE.putBoolean(obj, UNSAFE.objectFieldOffset(fieldObj), value);
            }
        }
    }

    public static void setBooleanArrayElementAt(final Object obj, final String field, final int index, final boolean value) {
        final boolean[] array = (boolean[]) getArrayObject(obj, field, boolean.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Byte
    public static byte getByte(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            return UNSAFE.getByte(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
        }

        final Field fieldObj = getFieldObj(objClass, field);
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getByte(objClass, UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getByte(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static byte getByteArrayElementAt(final Object obj, final String field, final int index) {
        final byte[] array = (byte[]) getArrayObject(obj, field, byte.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public static void setByte(final Object obj, final String field, final byte value) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            UNSAFE.putByte(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
        } else {
            final Field fieldObj = getFieldObj(objClass, field);
            if (isStatic(fieldObj.getModifiers())) {
                UNSAFE.putByte(objClass, UNSAFE.staticFieldOffset(fieldObj), value);
            } else {
                UNSAFE.putByte(obj, UNSAFE.objectFieldOffset(fieldObj), value);
            }
        }
    }

    public static void setByteArrayElementAt(final Object obj, final String field, final int index, final byte value) {
        final byte[] array = (byte[]) getArrayObject(obj, field, byte.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Char
    public static char getChar(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            return UNSAFE.getChar(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
        }

        final Field fieldObj = getFieldObj(objClass, field);
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getChar(objClass, UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getChar(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static char getCharArrayElementAt(final Object obj, final String field, final int index) {
        final char[] array = (char[]) getArrayObject(obj, field, char.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public static void setChar(final Object obj, final String field, final char value) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            UNSAFE.putChar(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
        } else {
            final Field fieldObj = getFieldObj(objClass, field);
            if (isStatic(fieldObj.getModifiers())) {
                UNSAFE.putChar(objClass, UNSAFE.staticFieldOffset(fieldObj), value);
            } else {
                UNSAFE.putChar(obj, UNSAFE.objectFieldOffset(fieldObj), value);
            }
        }
    }

    public static void setCharArrayElementAt(final Object obj, final String field, final int index, final char value) {
        final char[] array = (char[]) getArrayObject(obj, field, char.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Double
    public static double getDouble(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            return UNSAFE.getDouble(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
        }

        final Field fieldObj = getFieldObj(objClass, field);
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getDouble(objClass, UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getDouble(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static double getDoubleArrayElementAt(final Object obj, final String field, final int index) {
        final double[] array = (double[]) getArrayObject(obj, field, double.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public static void setDouble(final Object obj, final String field, final double value) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            UNSAFE.putDouble(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
        } else {
            final Field fieldObj = getFieldObj(objClass, field);
            if (isStatic(fieldObj.getModifiers())) {
                UNSAFE.putDouble(objClass, UNSAFE.staticFieldOffset(fieldObj), value);
            } else {
                UNSAFE.putDouble(obj, UNSAFE.objectFieldOffset(fieldObj), value);
            }
        }
    }

    public static void setDoubleArrayElementAt(final Object obj, final String field, final int index, final double value) {
        final double[] array = (double[]) getArrayObject(obj, field, double.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Float
    public static float getFloat(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            return UNSAFE.getFloat(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
        }

        final Field fieldObj = getFieldObj(objClass, field);
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getFloat(objClass, UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getFloat(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static float getFloatArrayElementAt(final Object obj, final String field, final int index) {
        final float[] array = (float[]) getArrayObject(obj, field, float.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public static void setFloat(final Object obj, final String field, final float value) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            UNSAFE.putFloat(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
        } else {
            final Field fieldObj = getFieldObj(objClass, field);
            if (isStatic(fieldObj.getModifiers())) {
                UNSAFE.putFloat(objClass, UNSAFE.staticFieldOffset(fieldObj), value);
            } else {
                UNSAFE.putFloat(obj, UNSAFE.objectFieldOffset(fieldObj), value);
            }
        }
    }

    public static void setFloatArrayElementAt(final Object obj, final String field, final int index, final float value) {
        final float[] array = (float[]) getArrayObject(obj, field, float.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Int
    public static int getInt(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            return UNSAFE.getInt(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
        }

        final Field fieldObj = getFieldObj(objClass, field);
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getInt(objClass, UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getInt(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static int getIntArrayElementAt(final Object obj, final String field, final int index) {
        final int[] array = (int[]) getArrayObject(obj, field, int.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public static void setInt(final Object obj, final String field, final int value) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            UNSAFE.putInt(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
        } else {
            final Field fieldObj = getFieldObj(objClass, field);
            if (isStatic(fieldObj.getModifiers())) {
                UNSAFE.putInt(objClass, UNSAFE.staticFieldOffset(fieldObj), value);
            } else {
                UNSAFE.putInt(obj, UNSAFE.objectFieldOffset(fieldObj), value);
            }
        }
    }

    public static void setIntArrayElementAt(final Object obj, final String field, final int index, final int value) {
        final int[] array = (int[]) getArrayObject(obj, field, int.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Long
    public static long getLong(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            return UNSAFE.getLong(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
        }

        final Field fieldObj = getFieldObj(objClass, field);
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getLong(objClass, UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getLong(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static long getLongArrayElementAt(final Object obj, final String field, final int index) {
        final long[] array = (long[]) getArrayObject(obj, field, long.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public static void setLong(final Object obj, final String field, final long value) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            UNSAFE.putLong(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
        } else {
            final Field fieldObj = getFieldObj(objClass, field);
            if (isStatic(fieldObj.getModifiers())) {
                UNSAFE.putLong(objClass, UNSAFE.staticFieldOffset(fieldObj), value);
            } else {
                UNSAFE.putLong(obj, UNSAFE.objectFieldOffset(fieldObj), value);
            }
        }
    }

    public static void setLongArrayElementAt(final Object obj, final String field, final int index, final long value) {
        final long[] array = (long[]) getArrayObject(obj, field, long.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Short
    public static short getShort(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            return UNSAFE.getShort(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
        }

        final Field fieldObj = getFieldObj(objClass, field);
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getShort(objClass, UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getShort(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static short getShortArrayElementAt(final Object obj, final String field, final int index) {
        final short[] array = (short[]) getArrayObject(obj, field, short.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public static void setShort(final Object obj, final String field, final short value) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            UNSAFE.putShort(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
        } else {
            final Field fieldObj = getFieldObj(objClass, field);
            if (isStatic(fieldObj.getModifiers())) {
                UNSAFE.putShort(objClass, UNSAFE.staticFieldOffset(fieldObj), value);
            } else {
                UNSAFE.putShort(obj, UNSAFE.objectFieldOffset(fieldObj), value);
            }
        }
    }

    public static void setShortArrayElementAt(final Object obj, final String field, final int index, final short value) {
        final short[] array = (short[]) getArrayObject(obj, field, short.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Object
    public static Object get(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            return UNSAFE.getObject(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
        }

        final Field fieldObj = getFieldObj(objClass, field);
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getObject(objClass, UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getObject(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static Object getArrayElementAt(final Object obj, final String field, final int index) {
        final Object[] array = (Object[]) getArrayObject(obj, field, Object.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public static void set(final Object obj, final String field, final Object value) {
        final Class objClass = getObjectClass(obj);
        if (objClass.isAnnotationPresent(CacheAware.class)) {
            final FieldStruct fieldStruct = getFieldStruct(objClass, field);
            UNSAFE.putObject(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
        } else {
            final Field fieldObj = getFieldObj(objClass, field);
            if (isStatic(fieldObj.getModifiers())) {
                UNSAFE.putObject(objClass, UNSAFE.staticFieldOffset(fieldObj), value);
            } else {
                UNSAFE.putObject(obj, UNSAFE.objectFieldOffset(fieldObj), value);
            }
        }
    }

    public static void setArrayElementAt(final Object obj, final String field, final int index, final Object value) {
        final Object[] array = (Object[]) getArrayObject(obj, field, Object.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    private static Class getObjectClass(final Object obj) {
        assert obj != null;
        return obj instanceof Class ? (Class) obj : obj.getClass();
    }

    private static FieldStruct getFieldStruct(final Class objClass, final String field) {
        final FieldStruct fieldStruct = ClassStructFactory.get(objClass).getField(field);
        if (fieldStruct == null) {
            throw new IllegalArgumentException("Field [" + field + "] does not exist on Class [" + objClass + "] or its Super Classes!");
        }
        return fieldStruct;
    }

    private static Field getFieldObj(final Class objClass, final String field) {
        final Field objField = ClassStruct.getField(objClass, field);
        if (objField == null) {
            throw new IllegalArgumentException("Field [" + field + "] does not exist on Class [" + objClass + "] or its Super Classes!");
        }
        return objField;
    }

    private static Object getArrayObject(final Object obj, final String field, final Class componentType) {
        final Class objClass = getObjectClass(obj);
        FieldStruct fieldStruct = null;
        Field objField = null;
        final boolean cacheAware = objClass.isAnnotationPresent(CacheAware.class);
        if (cacheAware) {
            fieldStruct = getFieldStruct(objClass, field);
        } else {
            objField = getFieldObj(objClass, field);
        }

        if (!isFieldArrayAndAssignable(fieldStruct, objField, componentType)) {
            throw new IllegalArgumentException("Field [" + field + "] on Class [" + objClass + "] is not an array of type [" + componentType.getSimpleName() + "]!");
        }

        final Object arrayObj;
        if (cacheAware) {
            arrayObj = UNSAFE.getObject(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
        } else {
            if (isStatic(objField.getModifiers())) {
                arrayObj = UNSAFE.getObject(objClass, UNSAFE.staticFieldOffset(objField));
            } else {
                arrayObj = UNSAFE.getObject(obj, UNSAFE.objectFieldOffset(objField));
            }
        }
        if (arrayObj == null) {
            throw new IllegalArgumentException("Field [" + field + "] on Class [" + objClass + "] is NULL");
        }
        return arrayObj;
    }

    private static boolean isFieldArrayAndAssignable(final FieldStruct fieldStruct, final Field objField, final Class componentType) {
        if (fieldStruct != null) {
            return fieldStruct.isArray() && componentType.isAssignableFrom(fieldStruct.type.getComponentType());
        } else if (objField != null) {
            return objField.getType().isArray() && componentType.isAssignableFrom(objField.getType().getComponentType());
        }
        return false;
    }

    private static void checkArrayIndex(final int length, final int index) {
        if (index >= length) {
            throw new IndexOutOfBoundsException("Array index [" + index + "] is greater than array length [" + length + "]!");
        }
    }
}
