package woo.ba.ben.bean;


public interface LongPropertyAccessor extends IPropertyAccessor {
    long get(Object bean, String field);
    long getStatic(Class bean, String field);
    long getArrayElementAt(Object bean, String field, int index);

    void set(Object bean, String field, long value);
    void setStatic(Class beanClass, String field, long value);
    void setArrayElementAt(Object bean, String field, int index, long value);
}
