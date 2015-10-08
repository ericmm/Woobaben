package woo.ba.ben.bean;


public interface IPropertyAccessor {
    <T> T get(Object bean, String field, Class<T> fieldType);
    <T> T getStatic(Class bean, String field, Class<T> fieldType);
    <T> T getArrayElementAt(Object bean, String field, Class<T> fieldType, int index);

    void set(Object bean, String field, Object value);
    void setStatic(Class beanClass, String field, Object value);
    void setArrayElementAt(Object bean, String field, int index, Object value);

    void set(Object bean, String field, boolean value);
    void setStatic(Class beanClass, String field, boolean value);
    void setArrayElementAt(Object bean, String field, int index, boolean value);

    void set(Object bean, String field, byte value);
    void setStatic(Class beanClass, String field, byte value);
    void setArrayElementAt(Object bean, String field, int index, byte value);

    void set(Object bean, String field, short value);
    void setStatic(Class beanClass, String field, short value);
    void setArrayElementAt(Object bean, String field, int index, short value);

    void set(Object bean, String field, char value);
    void setStatic(Class beanClass, String field, char value);
    void setArrayElementAt(Object bean, String field, int index, char value);

    void set(Object bean, String field, int value);
    void setStatic(Class beanClass, String field, int value);
    void setArrayElementAt(Object bean, String field, int index, int value);

    void set(Object bean, String field, long value);
    void setStatic(Class beanClass, String field, long value);
    void setArrayElementAt(Object bean, String field, int index, long value);

    void set(Object bean, String field, float value);
    void setStatic(Class beanClass, String field, float value);
    void setArrayElementAt(Object bean, String field, int index, float value);

    void set(Object bean, String field, double value);
    void setStatic(Class beanClass, String field, double value);
    void setArrayElementAt(Object bean, String field, int index, double value);
}
