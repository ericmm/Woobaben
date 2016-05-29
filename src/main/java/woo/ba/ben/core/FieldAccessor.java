package woo.ba.ben.core;


import static woo.ba.ben.core.ClassStruct.getObjectClass;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public class FieldAccessor {
    private FieldAccessor() {
    }

    //Boolean
    public static boolean getBoolean(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getBoolean(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static boolean getInstanceBoolean(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getBoolean(obj, fieldStruct.offset);
    }

    public static boolean getStaticBoolean(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getBoolean(objClass, fieldStruct.offset);
    }

    public static void setBoolean(final Object obj, final FieldStruct fieldStruct, final boolean value) {
        UNSAFE.putBoolean(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static void setInstanceBoolean(final Object obj, final FieldStruct fieldStruct, final boolean value) {
        UNSAFE.putBoolean(obj, fieldStruct.offset, value);
    }

    public static void setStaticBoolean(final Class objClass, final FieldStruct fieldStruct, final boolean value) {
        UNSAFE.putBoolean(objClass, fieldStruct.offset, value);
    }

    public static boolean getBooleanArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((boolean[]) getObject(obj, fieldStruct))[index];
    }

    public static boolean getInstanceBooleanArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((boolean[]) getInstanceObject(obj, fieldStruct))[index];
    }

    public static boolean getStaticBooleanArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((boolean[]) getStaticObject(objClass, fieldStruct))[index];
    }

    public static void setBooleanArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final boolean value) {
        ((boolean[]) getObject(obj, fieldStruct))[index] = value;
    }

    public static void setInstanceBooleanArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final boolean value) {
        ((boolean[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    public static void setStaticBooleanArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final boolean value) {
        ((boolean[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }


    //Byte
    public static byte getByte(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getByte(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static byte getInstanceByte(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getByte(obj, fieldStruct.offset);
    }

    public static byte getStaticByte(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getByte(objClass, fieldStruct.offset);
    }

    public static void setByte(final Object obj, final FieldStruct fieldStruct, final byte value) {
        UNSAFE.putByte(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static void setInstanceByte(final Object obj, final FieldStruct fieldStruct, final byte value) {
        UNSAFE.putByte(obj, fieldStruct.offset, value);
    }

    public static void setStaticByte(final Class objClass, final FieldStruct fieldStruct, final byte value) {
        UNSAFE.putByte(objClass, fieldStruct.offset, value);
    }

    public static byte getByteArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((byte[]) getObject(obj, fieldStruct))[index];
    }

    public static byte getInstanceByteArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((byte[]) getInstanceObject(obj, fieldStruct))[index];
    }

    public static byte getStaticByteArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((byte[]) getStaticObject(objClass, fieldStruct))[index];
    }

    public static void setByteArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final byte value) {
        ((byte[]) getObject(obj, fieldStruct))[index] = value;
    }

    public static void setInstanceByteArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final byte value) {
        ((byte[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    public static void setStaticByteArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final byte value) {
        ((byte[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }

    //Char
    public static char getChar(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getChar(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static char getInstanceChar(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getChar(obj, fieldStruct.offset);
    }

    public static char getStaticChar(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getChar(objClass, fieldStruct.offset);
    }

    public static void setChar(final Object obj, final FieldStruct fieldStruct, final char value) {
        UNSAFE.putChar(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static void setInstanceChar(final Object obj, final FieldStruct fieldStruct, final char value) {
        UNSAFE.putChar(obj, fieldStruct.offset, value);
    }

    public static void setStaticChar(final Class objClass, final FieldStruct fieldStruct, final char value) {
        UNSAFE.putChar(objClass, fieldStruct.offset, value);
    }

    public static char getCharArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((char[]) getObject(obj, fieldStruct))[index];
    }

    public static char getInstanceCharArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((char[]) getInstanceObject(obj, fieldStruct))[index];
    }

    public static char getStaticCharArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((char[]) getStaticObject(objClass, fieldStruct))[index];
    }

    public static void setCharArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final char value) {
        ((char[]) getObject(obj, fieldStruct))[index] = value;
    }

    public static void setInstanceCharArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final char value) {
        ((char[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    public static void setStaticCharArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final char value) {
        ((char[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }

    //Double
    public static double getDouble(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getDouble(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static double getInstanceDouble(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getDouble(obj, fieldStruct.offset);
    }

    public static double getStaticDouble(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getDouble(objClass, fieldStruct.offset);
    }

    public static void setDouble(final Object obj, final FieldStruct fieldStruct, final double value) {
        UNSAFE.putDouble(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static void setInstanceDouble(final Object obj, final FieldStruct fieldStruct, final double value) {
        UNSAFE.putDouble(obj, fieldStruct.offset, value);
    }

    public static void setStaticDouble(final Class objClass, final FieldStruct fieldStruct, final double value) {
        UNSAFE.putDouble(objClass, fieldStruct.offset, value);
    }

    public static double getDoubleArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((double[]) getObject(obj, fieldStruct))[index];
    }

    public static double getInstanceDoubleArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((double[]) getInstanceObject(obj, fieldStruct))[index];
    }

    public static double getStaticDoubleArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((double[]) getStaticObject(objClass, fieldStruct))[index];
    }

    public static void setDoubleArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final double value) {
        ((double[]) getObject(obj, fieldStruct))[index] = value;
    }

    public static void setInstanceDoubleArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final double value) {
        ((double[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    public static void setStaticDoubleArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final double value) {
        ((double[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }

    //Float
    public static float getFloat(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getFloat(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static float getInstanceFloat(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getFloat(obj, fieldStruct.offset);
    }

    public static float getStaticFloat(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getFloat(objClass, fieldStruct.offset);
    }

    public static void setFloat(final Object obj, final FieldStruct fieldStruct, final float value) {
        UNSAFE.putFloat(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static void setInstanceFloat(final Object obj, final FieldStruct fieldStruct, final float value) {
        UNSAFE.putFloat(obj, fieldStruct.offset, value);
    }

    public static void setStaticFloat(final Class objClass, final FieldStruct fieldStruct, final float value) {
        UNSAFE.putFloat(objClass, fieldStruct.offset, value);
    }

    public static float getFloatArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((float[]) getObject(obj, fieldStruct))[index];
    }

    public static float getInstanceFloatArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((float[]) getInstanceObject(obj, fieldStruct))[index];
    }

    public static float getStaticFloatArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((float[]) getStaticObject(objClass, fieldStruct))[index];
    }

    public static void setFloatArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final float value) {
        ((float[]) getObject(obj, fieldStruct))[index] = value;
    }

    public static void setInstanceFloatArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final float value) {
        ((float[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    public static void setStaticFloatArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final float value) {
        ((float[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }

    //Int
    public static int getInt(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getInt(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static int getInstanceInt(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getInt(obj, fieldStruct.offset);
    }

    public static int getStaticInt(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getInt(objClass, fieldStruct.offset);
    }

    public static void setInt(final Object obj, final FieldStruct fieldStruct, final int value) {
        UNSAFE.putInt(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static void setInstanceInt(final Object obj, final FieldStruct fieldStruct, final int value) {
        UNSAFE.putInt(obj, fieldStruct.offset, value);
    }

    public static void setStaticInt(final Class objClass, final FieldStruct fieldStruct, final int value) {
        UNSAFE.putInt(objClass, fieldStruct.offset, value);
    }

    public static int getIntArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((int[]) getObject(obj, fieldStruct))[index];
    }

    public static int getInstanceIntArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((int[]) getInstanceObject(obj, fieldStruct))[index];
    }

    public static int getStaticIntArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((int[]) getStaticObject(objClass, fieldStruct))[index];
    }

    public static void setIntArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final int value) {
        ((int[]) getObject(obj, fieldStruct))[index] = value;
    }

    public static void setInstanceIntArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final int value) {
        ((int[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    public static void setStaticIntArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final int value) {
        ((int[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }

    //Long
    public static long getLong(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getLong(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static long getInstanceLong(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getLong(obj, fieldStruct.offset);
    }

    public static long getStaticLong(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getLong(objClass, fieldStruct.offset);
    }

    public static void setLong(final Object obj, final FieldStruct fieldStruct, final long value) {
        UNSAFE.putLong(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static void setInstanceLong(final Object obj, final FieldStruct fieldStruct, final long value) {
        UNSAFE.putLong(obj, fieldStruct.offset, value);
    }

    public static void setStaticLong(final Class objClass, final FieldStruct fieldStruct, final long value) {
        UNSAFE.putLong(objClass, fieldStruct.offset, value);
    }

    public static long getLongArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((long[]) getObject(obj, fieldStruct))[index];
    }

    public static long getInstanceLongArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((long[]) getInstanceObject(obj, fieldStruct))[index];
    }

    public static long getStaticLongArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((long[]) getStaticObject(objClass, fieldStruct))[index];
    }

    public static void setLongArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final long value) {
        ((long[]) getObject(obj, fieldStruct))[index] = value;
    }

    public static void setInstanceLongArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final long value) {
        ((long[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    public static void setStaticLongArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final long value) {
        ((long[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }

    //Short
    public static short getShort(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getShort(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static short getInstanceShort(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getShort(obj, fieldStruct.offset);
    }

    public static short getStaticShort(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getShort(objClass, fieldStruct.offset);
    }

    public static void setShort(final Object obj, final FieldStruct fieldStruct, final short value) {
        UNSAFE.putShort(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static void setInstanceShort(final Object obj, final FieldStruct fieldStruct, final short value) {
        UNSAFE.putShort(obj, fieldStruct.offset, value);
    }

    public static void setStaticShort(final Class objClass, final FieldStruct fieldStruct, final short value) {
        UNSAFE.putShort(objClass, fieldStruct.offset, value);
    }

    public static short getShortArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((short[]) getObject(obj, fieldStruct))[index];
    }

    public static short getInstanceShortArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((short[]) getInstanceObject(obj, fieldStruct))[index];
    }

    public static short getStaticShortArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((short[]) getStaticObject(objClass, fieldStruct))[index];
    }

    public static void setShortArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final short value) {
        ((short[]) getObject(obj, fieldStruct))[index] = value;
    }

    public static void setInstanceShortArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final short value) {
        ((short[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    public static void setStaticShortArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final short value) {
        ((short[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }

    //Object
    public static Object getObject(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getObject(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    public static Object getInstanceObject(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getObject(obj, fieldStruct.offset);
    }

    public static Object getStaticObject(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getObject(objClass, fieldStruct.offset);
    }

    public static void setObject(final Object obj, final FieldStruct fieldStruct, final Object value) {
        UNSAFE.putObject(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    public static void setInstanceObject(final Object obj, final FieldStruct fieldStruct, final Object value) {
        UNSAFE.putObject(obj, fieldStruct.offset, value);
    }

    public static void setStaticObject(final Class objClass, final FieldStruct fieldStruct, final Object value) {
        UNSAFE.putObject(objClass, fieldStruct.offset, value);
    }

    public static Object getObjectArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((Object[]) getObject(obj, fieldStruct))[index];
    }

    public static Object getInstanceObjectArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((Object[]) getInstanceObject(obj, fieldStruct))[index];
    }

    public static Object getStaticObjectArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((Object[]) getStaticObject(objClass, fieldStruct))[index];
    }

    public static void setObjectArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final Object value) {
        ((Object[]) getObject(obj, fieldStruct))[index] = value;
    }

    public static void setInstanceObjectArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final Object value) {
        ((Object[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    public static void setStaticObjectArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final Object value) {
        ((Object[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }
}
