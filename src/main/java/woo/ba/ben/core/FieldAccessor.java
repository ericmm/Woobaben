package woo.ba.ben.core;


import java.lang.reflect.Field;

import static java.lang.reflect.Modifier.isStatic;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public class FieldAccessor {
    private FieldAccessor() {
    }

    //Boolean
    public static boolean getBoolean(final Object obj, final String field) throws NoSuchFieldException {
        return getBoolean(obj, getFieldObj(getObjectClass(obj), field));
    }

    public static boolean getBoolean(final Object obj, final Field fieldObj) {
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getBoolean(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getBoolean(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static boolean getBooleanCached(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getBoolean(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static void setBoolean(final Object obj, final String field, final boolean value) throws NoSuchFieldException {
        setBoolean(obj, getFieldObj(getObjectClass(obj), field), value);
    }

    public static void setBoolean(final Object obj, final Field fieldObj, final boolean value) {
        if (isStatic(fieldObj.getModifiers())) {
            UNSAFE.putBoolean(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj), value);
        } else {
            UNSAFE.putBoolean(obj, UNSAFE.objectFieldOffset(fieldObj), value);
        }
    }

    public static void setBooleanCached(final Object obj, final FieldStruct fieldStruct, final boolean value) {
        UNSAFE.putBoolean(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static boolean getBooleanArrayElementAt(final Object obj, final String field, final int index) throws NoSuchFieldException {
        return ((boolean[]) getObject(obj, field))[index];
    }

    public static boolean getBooleanArrayElementAt(final Object obj, final Field fieldObj, final int index) {
        return ((boolean[]) getObject(obj, fieldObj))[index];
    }

    public static boolean getBooleanArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((boolean[]) getObjectCached(obj, fieldStruct))[index];
    }

    public static void setBooleanArrayElementAt(final Object obj, final String field, final int index, final boolean value) throws NoSuchFieldException {
        ((boolean[]) getObject(obj, field))[index] = value;
    }

    public static void setBooleanArrayElementAt(final Object obj, final Field fieldObj, final int index, final boolean value) {
        ((boolean[]) getObject(obj, fieldObj))[index] = value;
    }

    public static void setBooleanArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final boolean value) {
        ((boolean[]) getObjectCached(obj, fieldStruct))[index] = value;
    }


    //Byte
    public static byte getByte(final Object obj, final String field) throws NoSuchFieldException {
        return getByte(obj, getFieldObj(getObjectClass(obj), field));
    }

    public static byte getByte(final Object obj, final Field fieldObj) {
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getByte(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getByte(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static byte getByteCached(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getByte(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static void setByte(final Object obj, final String field, final byte value) throws NoSuchFieldException {
        setByte(obj, getFieldObj(getObjectClass(obj), field), value);
    }

    public static void setByte(final Object obj, final Field fieldObj, final byte value) {
        if (isStatic(fieldObj.getModifiers())) {
            UNSAFE.putByte(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj), value);
        } else {
            UNSAFE.putByte(obj, UNSAFE.objectFieldOffset(fieldObj), value);
        }
    }

    public static void setByteCached(final Object obj, final FieldStruct fieldStruct, final byte value) {
        UNSAFE.putByte(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static byte getByteArrayElementAt(final Object obj, final String field, final int index) throws NoSuchFieldException {
        return ((byte[]) getObject(obj, field))[index];
    }

    public static byte getByteArrayElementAt(final Object obj, final Field fieldObj, final int index) {
        return ((byte[]) getObject(obj, fieldObj))[index];
    }

    public static byte getByteArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((byte[]) getObjectCached(obj, fieldStruct))[index];
    }

    public static void setByteArrayElementAt(final Object obj, final String field, final int index, final byte value) throws NoSuchFieldException {
        ((byte[]) getObject(obj, field))[index] = value;
    }

    public static void setByteArrayElementAt(final Object obj, final Field fieldObj, final int index, final byte value) {
        ((byte[]) getObject(obj, fieldObj))[index] = value;
    }

    public static void setByteArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final byte value) {
        ((byte[]) getObjectCached(obj, fieldStruct))[index] = value;
    }

    //Char
    public static char getChar(final Object obj, final String field) throws NoSuchFieldException {
        return getChar(obj, getFieldObj(getObjectClass(obj), field));
    }

    public static char getChar(final Object obj, final Field fieldObj) {
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getChar(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getChar(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static char getCharCached(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getChar(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static void setChar(final Object obj, final String field, final char value) throws NoSuchFieldException {
        setChar(obj, getFieldObj(getObjectClass(obj), field), value);
    }

    public static void setChar(final Object obj, final Field fieldObj, final char value) {
        if (isStatic(fieldObj.getModifiers())) {
            UNSAFE.putChar(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj), value);
        } else {
            UNSAFE.putChar(obj, UNSAFE.objectFieldOffset(fieldObj), value);
        }
    }

    public static void setCharCached(final Object obj, final FieldStruct fieldStruct, final char value) {
        UNSAFE.putChar(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static char getCharArrayElementAt(final Object obj, final String field, final int index) throws NoSuchFieldException {
        return ((char[]) getObject(obj, field))[index];
    }

    public static char getCharArrayElementAt(final Object obj, final Field fieldObj, final int index) {
        return ((char[]) getObject(obj, fieldObj))[index];
    }

    public static char getCharArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((char[]) getObjectCached(obj, fieldStruct))[index];
    }

    public static void setCharArrayElementAt(final Object obj, final String field, final int index, final char value) throws NoSuchFieldException {
        ((char[]) getObject(obj, field))[index] = value;
    }

    public static void setCharArrayElementAt(final Object obj, final Field fieldObj, final int index, final char value) {
        ((char[]) getObject(obj, fieldObj))[index] = value;
    }

    public static void setCharArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final char value) {
        ((char[]) getObjectCached(obj, fieldStruct))[index] = value;
    }

    //Double
    public static double getDouble(final Object obj, final String field) throws NoSuchFieldException {
        return getDouble(obj, getFieldObj(getObjectClass(obj), field));
    }

    public static double getDouble(final Object obj, final Field fieldObj) {
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getDouble(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getDouble(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static double getDoubleCached(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getDouble(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static void setDouble(final Object obj, final String field, final double value) throws NoSuchFieldException {
        setDouble(obj, getFieldObj(getObjectClass(obj), field), value);
    }

    public static void setDouble(final Object obj, final Field fieldObj, final double value) {
        if (isStatic(fieldObj.getModifiers())) {
            UNSAFE.putDouble(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj), value);
        } else {
            UNSAFE.putDouble(obj, UNSAFE.objectFieldOffset(fieldObj), value);
        }
    }

    public static void setDoubleCached(final Object obj, final FieldStruct fieldStruct, final double value) {
        UNSAFE.putDouble(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static double getDoubleArrayElementAt(final Object obj, final String field, final int index) throws NoSuchFieldException {
        return ((double[]) getObject(obj, field))[index];
    }

    public static double getDoubleArrayElementAt(final Object obj, final Field fieldObj, final int index) {
        return ((double[]) getObject(obj, fieldObj))[index];
    }

    public static double getDoubleArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((double[]) getObjectCached(obj, fieldStruct))[index];
    }

    public static void setDoubleArrayElementAt(final Object obj, final String field, final int index, final double value) throws NoSuchFieldException {
        ((double[]) getObject(obj, field))[index] = value;
    }

    public static void setDoubleArrayElementAt(final Object obj, final Field fieldObj, final int index, final double value) {
        ((double[]) getObject(obj, fieldObj))[index] = value;
    }

    public static void setDoubleArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final double value) {
        ((double[]) getObjectCached(obj, fieldStruct))[index] = value;
    }

    //Float
    public static float getFloat(final Object obj, final String field) throws NoSuchFieldException {
        return getFloat(obj, getFieldObj(getObjectClass(obj), field));
    }

    public static float getFloat(final Object obj, final Field fieldObj) {
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getFloat(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getFloat(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static float getFloatCached(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getFloat(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static void setFloat(final Object obj, final String field, final float value) throws NoSuchFieldException {
        setFloat(obj, getFieldObj(getObjectClass(obj), field), value);
    }

    public static void setFloat(final Object obj, final Field fieldObj, final float value) {
        if (isStatic(fieldObj.getModifiers())) {
            UNSAFE.putFloat(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj), value);
        } else {
            UNSAFE.putFloat(obj, UNSAFE.objectFieldOffset(fieldObj), value);
        }
    }

    public static void setFloatCached(final Object obj, final FieldStruct fieldStruct, final float value) {
        UNSAFE.putFloat(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static float getFloatArrayElementAt(final Object obj, final String field, final int index) throws NoSuchFieldException {
        return ((float[]) getObject(obj, field))[index];
    }

    public static float getFloatArrayElementAt(final Object obj, final Field fieldObj, final int index) {
        return ((float[]) getObject(obj, fieldObj))[index];
    }

    public static float getFloatArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((float[]) getObjectCached(obj, fieldStruct))[index];
    }

    public static void setFloatArrayElementAt(final Object obj, final String field, final int index, final float value) throws NoSuchFieldException {
        ((float[]) getObject(obj, field))[index] = value;
    }

    public static void setFloatArrayElementAt(final Object obj, final Field fieldObj, final int index, final float value) {
        ((float[]) getObject(obj, fieldObj))[index] = value;
    }

    public static void setFloatArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final float value) {
        ((float[]) getObjectCached(obj, fieldStruct))[index] = value;
    }

    //Int
    public static int getInt(final Object obj, final String field) throws NoSuchFieldException {
        return getInt(obj, getFieldObj(getObjectClass(obj), field));
    }

    public static int getInt(final Object obj, final Field fieldObj) {
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getInt(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getInt(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static int getIntCached(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getInt(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static void setInt(final Object obj, final String field, final int value) throws NoSuchFieldException {
        setInt(obj, getFieldObj(getObjectClass(obj), field), value);
    }

    public static void setInt(final Object obj, final Field fieldObj, final int value) {
        if (isStatic(fieldObj.getModifiers())) {
            UNSAFE.putInt(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj), value);
        } else {
            UNSAFE.putInt(obj, UNSAFE.objectFieldOffset(fieldObj), value);
        }
    }

    public static void setIntCached(final Object obj, final FieldStruct fieldStruct, final int value) {
        UNSAFE.putInt(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static int getIntArrayElementAt(final Object obj, final String field, final int index) throws NoSuchFieldException {
        return ((int[]) getObject(obj, field))[index];
    }

    public static int getIntArrayElementAt(final Object obj, final Field fieldObj, final int index) {
        return ((int[]) getObject(obj, fieldObj))[index];
    }

    public static int getIntArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((int[]) getObjectCached(obj, fieldStruct))[index];
    }

    public static void setIntArrayElementAt(final Object obj, final String field, final int index, final int value) throws NoSuchFieldException {
        ((int[]) getObject(obj, field))[index] = value;
    }

    public static void setIntArrayElementAt(final Object obj, final Field fieldObj, final int index, final int value) {
        ((int[]) getObject(obj, fieldObj))[index] = value;
    }

    public static void setIntArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final int value) {
        ((int[]) getObjectCached(obj, fieldStruct))[index] = value;
    }

    //Long
    public static long getLong(final Object obj, final String field) throws NoSuchFieldException {
        return getLong(obj, getFieldObj(getObjectClass(obj), field));
    }

    public static long getLong(final Object obj, final Field fieldObj) {
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getLong(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getLong(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static long getLongCached(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getLong(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static void setLong(final Object obj, final String field, final long value) throws NoSuchFieldException {
        setLong(obj, getFieldObj(getObjectClass(obj), field), value);
    }

    public static void setLong(final Object obj, final Field fieldObj, final long value) {
        if (isStatic(fieldObj.getModifiers())) {
            UNSAFE.putLong(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj), value);
        } else {
            UNSAFE.putLong(obj, UNSAFE.objectFieldOffset(fieldObj), value);
        }
    }

    public static void setLongCached(final Object obj, final FieldStruct fieldStruct, final long value) {
        UNSAFE.putLong(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static long getLongArrayElementAt(final Object obj, final String field, final int index) throws NoSuchFieldException {
        return ((long[]) getObject(obj, field))[index];
    }

    public static long getLongArrayElementAt(final Object obj, final Field fieldObj, final int index) {
        return ((long[]) getObject(obj, fieldObj))[index];
    }

    public static long getLongArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((long[]) getObjectCached(obj, fieldStruct))[index];
    }

    public static void setLongArrayElementAt(final Object obj, final String field, final int index, final long value) throws NoSuchFieldException {
        ((long[]) getObject(obj, field))[index] = value;
    }

    public static void setLongArrayElementAt(final Object obj, final Field fieldObj, final int index, final long value) {
        ((long[]) getObject(obj, fieldObj))[index] = value;
    }

    public static void setLongArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final long value) {
        ((long[]) getObjectCached(obj, fieldStruct))[index] = value;
    }

    //Short
    public static short getShort(final Object obj, final String field) throws NoSuchFieldException {
        return getShort(obj, getFieldObj(getObjectClass(obj), field));
    }

    public static short getShort(final Object obj, final Field fieldObj) {
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getShort(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getShort(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static short getShortCached(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getShort(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static void setShort(final Object obj, final String field, final short value) throws NoSuchFieldException {
        setShort(obj, getFieldObj(getObjectClass(obj), field), value);
    }

    public static void setShort(final Object obj, final Field fieldObj, final short value) {
        if (isStatic(fieldObj.getModifiers())) {
            UNSAFE.putShort(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj), value);
        } else {
            UNSAFE.putShort(obj, UNSAFE.objectFieldOffset(fieldObj), value);
        }
    }

    public static void setShortCached(final Object obj, final FieldStruct fieldStruct, final short value) {
        UNSAFE.putShort(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static short getShortArrayElementAt(final Object obj, final String field, final int index) throws NoSuchFieldException {
        return ((short[]) getObject(obj, field))[index];
    }

    public static short getShortArrayElementAt(final Object obj, final Field fieldObj, final int index) {
        return ((short[]) getObject(obj, fieldObj))[index];
    }

    public static short getShortArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((short[]) getObjectCached(obj, fieldStruct))[index];
    }

    public static void setShortArrayElementAt(final Object obj, final String field, final int index, final short value) throws NoSuchFieldException {
        ((short[]) getObject(obj, field))[index] = value;
    }

    public static void setShortArrayElementAt(final Object obj, final Field fieldObj, final int index, final short value) {
        ((short[]) getObject(obj, fieldObj))[index] = value;
    }

    public static void setShortArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final short value) {
        ((short[]) getObjectCached(obj, fieldStruct))[index] = value;
    }

    //Object
    public static Object getObject(final Object obj, final String field) throws NoSuchFieldException {
        return getObject(obj, getFieldObj(getObjectClass(obj), field));
    }

    public static Object getObject(final Object obj, final Field fieldObj) {
        if (isStatic(fieldObj.getModifiers())) {
            return UNSAFE.getObject(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj));
        } else {
            return UNSAFE.getObject(obj, UNSAFE.objectFieldOffset(fieldObj));
        }
    }

    public static Object getObjectCached(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getObject(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static void setObject(final Object obj, final String field, final Object value) throws NoSuchFieldException {
        setObject(obj, getFieldObj(getObjectClass(obj), field), value);
    }

    public static void setObject(final Object obj, final Field fieldObj, final Object value) {
        if (isStatic(fieldObj.getModifiers())) {
            UNSAFE.putObject(getObjectClass(obj), UNSAFE.staticFieldOffset(fieldObj), value);
        } else {
            UNSAFE.putObject(obj, UNSAFE.objectFieldOffset(fieldObj), value);
        }
    }

    public static void setObjectCached(final Object obj, final FieldStruct fieldStruct, final Object value) {
        UNSAFE.putObject(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static Object getObjectArrayElementAt(final Object obj, final String field, final int index) throws NoSuchFieldException {
        return ((Object[]) getObject(obj, field))[index];
    }

    public static Object getObjectArrayElementAt(final Object obj, final Field fieldObj, final int index) {
        return ((Object[]) getObject(obj, fieldObj))[index];
    }

    public static Object getObjectArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((Object[]) getObjectCached(obj, fieldStruct))[index];
    }

    public static void setObjectArrayElementAt(final Object obj, final String field, final int index, final Object value) throws NoSuchFieldException {
        ((Object[]) getObject(obj, field))[index] = value;
    }

    public static void setObjectArrayElementAt(final Object obj, final Field fieldObj, final int index, final Object value) {
        ((Object[]) getObject(obj, fieldObj))[index] = value;
    }

    public static void setObjectArrayCachedElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final Object value) {
        ((Object[]) getObjectCached(obj, fieldStruct))[index] = value;
    }


    private static Class getObjectClass(final Object obj) {
        return obj instanceof Class ? (Class) obj : obj.getClass();
    }

    private static Field getFieldObj(final Class objClass, final String field) throws NoSuchFieldException {
        return ClassStruct.getField(objClass, field);
    }
}
