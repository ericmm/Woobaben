package woo.ba.ben.core;


import static woo.ba.ben.core.ClassStruct.getObjectClass;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

class FieldAccessor {
    private FieldAccessor() {
    }

    //Boolean
    static boolean getBoolean(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getBoolean(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    static boolean getInstanceBoolean(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getBoolean(obj, fieldStruct.offset);
    }

    static boolean getStaticBoolean(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getBoolean(objClass, fieldStruct.offset);
    }

    static void setBoolean(final Object obj, final FieldStruct fieldStruct, final boolean value) {
        UNSAFE.putBoolean(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    static void setInstanceBoolean(final Object obj, final FieldStruct fieldStruct, final boolean value) {
        UNSAFE.putBoolean(obj, fieldStruct.offset, value);
    }

    static void setStaticBoolean(final Class objClass, final FieldStruct fieldStruct, final boolean value) {
        UNSAFE.putBoolean(objClass, fieldStruct.offset, value);
    }

    static boolean getBooleanArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((boolean[]) getObject(obj, fieldStruct))[index];
    }

    static boolean getInstanceBooleanArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((boolean[]) getInstanceObject(obj, fieldStruct))[index];
    }

    static boolean getStaticBooleanArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((boolean[]) getStaticObject(objClass, fieldStruct))[index];
    }

    static void setBooleanArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final boolean value) {
        ((boolean[]) getObject(obj, fieldStruct))[index] = value;
    }

    static void setInstanceBooleanArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final boolean value) {
        ((boolean[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    static void setStaticBooleanArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final boolean value) {
        ((boolean[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }


    //Byte
    static byte getByte(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getByte(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    static byte getInstanceByte(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getByte(obj, fieldStruct.offset);
    }

    static byte getStaticByte(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getByte(objClass, fieldStruct.offset);
    }

    static void setByte(final Object obj, final FieldStruct fieldStruct, final byte value) {
        UNSAFE.putByte(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    static void setInstanceByte(final Object obj, final FieldStruct fieldStruct, final byte value) {
        UNSAFE.putByte(obj, fieldStruct.offset, value);
    }

    static void setStaticByte(final Class objClass, final FieldStruct fieldStruct, final byte value) {
        UNSAFE.putByte(objClass, fieldStruct.offset, value);
    }

    static byte getByteArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((byte[]) getObject(obj, fieldStruct))[index];
    }

    static byte getInstanceByteArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((byte[]) getInstanceObject(obj, fieldStruct))[index];
    }

    static byte getStaticByteArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((byte[]) getStaticObject(objClass, fieldStruct))[index];
    }

    static void setByteArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final byte value) {
        ((byte[]) getObject(obj, fieldStruct))[index] = value;
    }

    static void setInstanceByteArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final byte value) {
        ((byte[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    static void setStaticByteArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final byte value) {
        ((byte[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }

    //Char
    static char getChar(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getChar(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    static char getInstanceChar(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getChar(obj, fieldStruct.offset);
    }

    static char getStaticChar(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getChar(objClass, fieldStruct.offset);
    }

    static void setChar(final Object obj, final FieldStruct fieldStruct, final char value) {
        UNSAFE.putChar(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    static void setInstanceChar(final Object obj, final FieldStruct fieldStruct, final char value) {
        UNSAFE.putChar(obj, fieldStruct.offset, value);
    }

    static void setStaticChar(final Class objClass, final FieldStruct fieldStruct, final char value) {
        UNSAFE.putChar(objClass, fieldStruct.offset, value);
    }

    static char getCharArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((char[]) getObject(obj, fieldStruct))[index];
    }

    static char getInstanceCharArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((char[]) getInstanceObject(obj, fieldStruct))[index];
    }

    static char getStaticCharArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((char[]) getStaticObject(objClass, fieldStruct))[index];
    }

    static void setCharArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final char value) {
        ((char[]) getObject(obj, fieldStruct))[index] = value;
    }

    static void setInstanceCharArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final char value) {
        ((char[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    static void setStaticCharArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final char value) {
        ((char[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }

    //Double
    static double getDouble(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getDouble(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    static double getInstanceDouble(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getDouble(obj, fieldStruct.offset);
    }

    static double getStaticDouble(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getDouble(objClass, fieldStruct.offset);
    }

    static void setDouble(final Object obj, final FieldStruct fieldStruct, final double value) {
        UNSAFE.putDouble(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    static void setInstanceDouble(final Object obj, final FieldStruct fieldStruct, final double value) {
        UNSAFE.putDouble(obj, fieldStruct.offset, value);
    }

    static void setStaticDouble(final Class objClass, final FieldStruct fieldStruct, final double value) {
        UNSAFE.putDouble(objClass, fieldStruct.offset, value);
    }

    static double getDoubleArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((double[]) getObject(obj, fieldStruct))[index];
    }

    static double getInstanceDoubleArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((double[]) getInstanceObject(obj, fieldStruct))[index];
    }

    static double getStaticDoubleArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((double[]) getStaticObject(objClass, fieldStruct))[index];
    }

    static void setDoubleArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final double value) {
        ((double[]) getObject(obj, fieldStruct))[index] = value;
    }

    static void setInstanceDoubleArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final double value) {
        ((double[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    static void setStaticDoubleArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final double value) {
        ((double[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }

    //Float
    static float getFloat(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getFloat(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    static float getInstanceFloat(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getFloat(obj, fieldStruct.offset);
    }

    static float getStaticFloat(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getFloat(objClass, fieldStruct.offset);
    }

    static void setFloat(final Object obj, final FieldStruct fieldStruct, final float value) {
        UNSAFE.putFloat(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    static void setInstanceFloat(final Object obj, final FieldStruct fieldStruct, final float value) {
        UNSAFE.putFloat(obj, fieldStruct.offset, value);
    }

    static void setStaticFloat(final Class objClass, final FieldStruct fieldStruct, final float value) {
        UNSAFE.putFloat(objClass, fieldStruct.offset, value);
    }

    static float getFloatArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((float[]) getObject(obj, fieldStruct))[index];
    }

    static float getInstanceFloatArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((float[]) getInstanceObject(obj, fieldStruct))[index];
    }

    static float getStaticFloatArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((float[]) getStaticObject(objClass, fieldStruct))[index];
    }

    static void setFloatArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final float value) {
        ((float[]) getObject(obj, fieldStruct))[index] = value;
    }

    static void setInstanceFloatArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final float value) {
        ((float[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    static void setStaticFloatArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final float value) {
        ((float[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }

    //Int
    static int getInt(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getInt(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    static int getInstanceInt(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getInt(obj, fieldStruct.offset);
    }

    static int getStaticInt(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getInt(objClass, fieldStruct.offset);
    }

    static void setInt(final Object obj, final FieldStruct fieldStruct, final int value) {
        UNSAFE.putInt(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    static void setInstanceInt(final Object obj, final FieldStruct fieldStruct, final int value) {
        UNSAFE.putInt(obj, fieldStruct.offset, value);
    }

    static void setStaticInt(final Class objClass, final FieldStruct fieldStruct, final int value) {
        UNSAFE.putInt(objClass, fieldStruct.offset, value);
    }

    static int getIntArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((int[]) getObject(obj, fieldStruct))[index];
    }

    static int getInstanceIntArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((int[]) getInstanceObject(obj, fieldStruct))[index];
    }

    static int getStaticIntArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((int[]) getStaticObject(objClass, fieldStruct))[index];
    }

    static void setIntArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final int value) {
        ((int[]) getObject(obj, fieldStruct))[index] = value;
    }

    static void setInstanceIntArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final int value) {
        ((int[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    static void setStaticIntArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final int value) {
        ((int[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }

    //Long
    static long getLong(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getLong(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    static long getInstanceLong(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getLong(obj, fieldStruct.offset);
    }

    static long getStaticLong(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getLong(objClass, fieldStruct.offset);
    }

    static void setLong(final Object obj, final FieldStruct fieldStruct, final long value) {
        UNSAFE.putLong(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    static void setInstanceLong(final Object obj, final FieldStruct fieldStruct, final long value) {
        UNSAFE.putLong(obj, fieldStruct.offset, value);
    }

    static void setStaticLong(final Class objClass, final FieldStruct fieldStruct, final long value) {
        UNSAFE.putLong(objClass, fieldStruct.offset, value);
    }

    static long getLongArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((long[]) getObject(obj, fieldStruct))[index];
    }

    static long getInstanceLongArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((long[]) getInstanceObject(obj, fieldStruct))[index];
    }

    static long getStaticLongArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((long[]) getStaticObject(objClass, fieldStruct))[index];
    }

    static void setLongArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final long value) {
        ((long[]) getObject(obj, fieldStruct))[index] = value;
    }

    static void setInstanceLongArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final long value) {
        ((long[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    static void setStaticLongArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final long value) {
        ((long[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }

    //Short
    static short getShort(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getShort(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    static short getInstanceShort(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getShort(obj, fieldStruct.offset);
    }

    static short getStaticShort(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getShort(objClass, fieldStruct.offset);
    }

    static void setShort(final Object obj, final FieldStruct fieldStruct, final short value) {
        UNSAFE.putShort(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    static void setInstanceShort(final Object obj, final FieldStruct fieldStruct, final short value) {
        UNSAFE.putShort(obj, fieldStruct.offset, value);
    }

    static void setStaticShort(final Class objClass, final FieldStruct fieldStruct, final short value) {
        UNSAFE.putShort(objClass, fieldStruct.offset, value);
    }

    static short getShortArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((short[]) getObject(obj, fieldStruct))[index];
    }

    static short getInstanceShortArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((short[]) getInstanceObject(obj, fieldStruct))[index];
    }

    static short getStaticShortArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((short[]) getStaticObject(objClass, fieldStruct))[index];
    }

    static void setShortArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final short value) {
        ((short[]) getObject(obj, fieldStruct))[index] = value;
    }

    static void setInstanceShortArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final short value) {
        ((short[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    static void setStaticShortArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final short value) {
        ((short[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }

    //Object
    static Object getObject(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getObject(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset);
    }

    static Object getInstanceObject(final Object obj, final FieldStruct fieldStruct) {
        return UNSAFE.getObject(obj, fieldStruct.offset);
    }

    static Object getStaticObject(final Class objClass, final FieldStruct fieldStruct) {
        return UNSAFE.getObject(objClass, fieldStruct.offset);
    }

    static void setObject(final Object obj, final FieldStruct fieldStruct, final Object value) {
        UNSAFE.putObject(fieldStruct.isStatic() ? getObjectClass(obj) : obj, fieldStruct.offset, value);
    }

    static void setInstanceObject(final Object obj, final FieldStruct fieldStruct, final Object value) {
        UNSAFE.putObject(obj, fieldStruct.offset, value);
    }

    static void setStaticObject(final Class objClass, final FieldStruct fieldStruct, final Object value) {
        UNSAFE.putObject(objClass, fieldStruct.offset, value);
    }

    static Object getObjectArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((Object[]) getObject(obj, fieldStruct))[index];
    }

    static Object getInstanceObjectArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index) {
        return ((Object[]) getInstanceObject(obj, fieldStruct))[index];
    }

    static Object getStaticObjectArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index) {
        return ((Object[]) getStaticObject(objClass, fieldStruct))[index];
    }

    static void setObjectArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final Object value) {
        ((Object[]) getObject(obj, fieldStruct))[index] = value;
    }

    static void setInstanceObjectArrayElementAt(final Object obj, final FieldStruct fieldStruct, final int index, final Object value) {
        ((Object[]) getInstanceObject(obj, fieldStruct))[index] = value;
    }

    static void setStaticObjectArrayElementAt(final Class objClass, final FieldStruct fieldStruct, final int index, final Object value) {
        ((Object[]) getStaticObject(objClass, fieldStruct))[index] = value;
    }
}
