package woo.ba.ben.bean;


public interface IPropertyAccessor {
    Object getObject(Object bean, String field);
    Object getStaticObject(Class beanClass, String field);
    Object getObjectArrayElement(Object bean, String field, int index);
    void set(Object bean, String field, Object value);
    void setStatic(Class beanClass, String field, Object value);
    void setArrayElement(Object bean, String field, int index, Object value);

    boolean getBoolean(Object bean, String field);
    boolean getStaticBoolean(Class beanClass, String field);
    boolean getBooleanArrayElement(Object bean, String field, int index);
    void setBoolean(Object bean, String field, boolean value);
    void setStatic(Class beanClass, String field, boolean value);
    void setArrayElement(Object bean, String field, int index, boolean value);

    byte getByte(Object bean, String field);
    byte getStaticByte(Class beanClass, String field);
    byte getByteArrayElement(Object bean, String field, int index);
    void setByte(Object bean, String field, byte value);
    void setStatic(Class beanClass, String field, byte value);
    void setArrayElement(Object bean, String field, int index, byte value);

    short getShort(Object bean, String field);
    short getStaticShort(Class beanClass, String field);
    short getShortArrayElement(Object bean, String field, int index);
    void setShort(Object bean, String field, short value);
    void setStatic(Class beanClass, String field, short value);
    void setArrayElement(Object bean, String field, int index, short value);

    char getChar(Object bean, String field);
    char getStaticChar(Class beanClass, String field);
    char getCharArrayElement(Object bean, String field, int index);
    void setChar(Object bean, String field, char value);
    void setStatic(Class beanClass, String field, char value);
    void setArrayElement(Object bean, String field, int index, char value);

    int getInt(Object bean, String field);
    int getStaticInt(Class beanClass, String field);
    int getIntArrayElement(Object bean, String field, int index);
    void set(Object bean, String field, int value);
    void setStatic(Class beanClass, String field, int value);
    void setArrayElement(Object bean, String field, int index, int value);

    long getLong(Object bean, String field);
    long getStaticLong(Class beanClass, String field);
    long getLongArrayElement(Object bean, String field, int index);
    void setLong(Object bean, String field, long value);
    void setStatic(Class beanClass, String field, long value);
    void setArrayElement(Object bean, String field, int index, long value);

    float getFloat(Object bean, String field);
    float getStaticFloat(Class beanClass, String field);
    float getFloatArrayElement(Object bean, String field, int index);
    void setFloat(Object bean, String field, float value);
    void setStatic(Class beanClass, String field, float value);
    void setArrayElement(Object bean, String field, int index, float value);

    double getDouble(Object bean, String field);
    double getStaticDouble(Class beanClass, String field);
    double getDoubleArrayElement(Object bean, String field, int index);
    void setDouble(Object bean, String field, double value);
    void setStatic(Class beanClass, String field, double value);
    void setArrayElement(Object bean, String field, int index, double value);
}
