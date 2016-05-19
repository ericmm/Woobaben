package woo.ba.ben.core;


import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public class FieldAccessor {
    private FieldAccessor() {
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

    private static Object getArrayObject(final Object obj, final String field, final Class componentType) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        if (!fieldStruct.isArray() && componentType.isAssignableFrom(fieldStruct.type.getComponentType())) {
            throw new IllegalArgumentException("Field [" + field + "] on Class [" + objClass + "] is not an array of type [" + componentType.getSimpleName() + "]!");
        }
        final Object arrayObj = UNSAFE.getObject(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
        if (arrayObj == null) {
            throw new IllegalArgumentException("Field [" + field + "] on Class [" + objClass + "] is NULL");
        }
        return arrayObj;
    }

    private static void checkArrayIndex(final int length, final int index) {
        if (index >= length) {
            throw new IndexOutOfBoundsException("Array index [" + index + "] is greater than array length [" + length + "]!");
        }
    }

    //Boolean
    public boolean getBoolean(final Object obj, final String field) throws NoSuchFieldException {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getBoolean(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
    }

    public boolean getBooleanArrayElementAt(final Object obj, final String field, final int index) {
        final boolean[] array = (boolean[]) getArrayObject(obj, field, boolean.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public void setBoolean(final Object obj, final String field, final boolean value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putBoolean(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
    }

    public void setBooleanArrayElementAt(final Object obj, final String field, final int index, final boolean value) {
        final boolean[] array = (boolean[]) getArrayObject(obj, field, boolean.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Byte
    public byte getByte(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getByte(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
    }

    public byte getByteArrayElementAt(final Object obj, final String field, final int index) {
        final byte[] array = (byte[]) getArrayObject(obj, field, byte.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public void setByte(final Object obj, final String field, final byte value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putByte(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
    }

    public void setByteArrayElementAt(final Object obj, final String field, final int index, final byte value) {
        final byte[] array = (byte[]) getArrayObject(obj, field, byte.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Char
    public char getChar(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getChar(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
    }

    public char getCharArrayElementAt(final Object obj, final String field, final int index) {
        final char[] array = (char[]) getArrayObject(obj, field, char.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public void setChar(final Object obj, final String field, final char value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putChar(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
    }

    public void setCharArrayElementAt(final Object obj, final String field, final int index, final char value) {
        final char[] array = (char[]) getArrayObject(obj, field, char.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Double
    public double getDouble(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getDouble(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
    }

    public double getDoubleArrayElementAt(final Object obj, final String field, final int index) {
        final double[] array = (double[]) getArrayObject(obj, field, double.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public void setDouble(final Object obj, final String field, final double value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putDouble(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
    }

    public void setDoubleArrayElementAt(final Object obj, final String field, final int index, final double value) {
        final double[] array = (double[]) getArrayObject(obj, field, double.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Float
    public float getFloat(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getFloat(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
    }

    public float getFloatArrayElementAt(final Object obj, final String field, final int index) {
        final float[] array = (float[]) getArrayObject(obj, field, float.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public void setFloat(final Object obj, final String field, final float value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putFloat(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
    }

    public void setFloatArrayElementAt(final Object obj, final String field, final int index, final float value) {
        final float[] array = (float[]) getArrayObject(obj, field, float.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Int
    public int getInt(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getInt(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
    }

    public int getIntArrayElementAt(final Object obj, final String field, final int index) {
        final int[] array = (int[]) getArrayObject(obj, field, int.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public void setInt(final Object obj, final String field, final int value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putInt(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
    }

    public void setIntArrayElementAt(final Object obj, final String field, final int index, final int value) {
        final int[] array = (int[]) getArrayObject(obj, field, int.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Long
    public long getLong(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getLong(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
    }

    public long getLongArrayElementAt(final Object obj, final String field, final int index) {
        final long[] array = (long[]) getArrayObject(obj, field, long.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public void setLong(final Object obj, final String field, final long value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putLong(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
    }

    public void setLongArrayElementAt(final Object obj, final String field, final int index, final long value) {
        final long[] array = (long[]) getArrayObject(obj, field, long.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Short
    public short getShort(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getShort(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
    }

    public short getShortArrayElementAt(final Object obj, final String field, final int index) {
        final short[] array = (short[]) getArrayObject(obj, field, short.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public void setShort(final Object obj, final String field, final short value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putShort(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
    }

    public void setShortArrayElementAt(final Object obj, final String field, final int index, final short value) {
        final short[] array = (short[]) getArrayObject(obj, field, short.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }

    //Object
    public Object get(final Object obj, final String field) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        return UNSAFE.getObject(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset);
    }

    public Object getArrayElementAt(final Object obj, final String field, final int index) {
        final Object[] array = (Object[]) getArrayObject(obj, field, Object.class);
        checkArrayIndex(array.length, index);
        return array[index];
    }

    public void set(final Object obj, final String field, final Object value) {
        final Class objClass = getObjectClass(obj);
        final FieldStruct fieldStruct = getFieldStruct(objClass, field);
        UNSAFE.putObject(fieldStruct.isStatic() ? objClass : obj, fieldStruct.offset, value);
    }

    public void setArrayElementAt(final Object obj, final String field, final int index, final Object value) {
        final Object[] array = (Object[]) getArrayObject(obj, field, Object.class);
        checkArrayIndex(array.length, index);
        array[index] = value;
    }
}
