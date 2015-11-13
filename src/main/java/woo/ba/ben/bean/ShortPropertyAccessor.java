package woo.ba.ben.bean;


public interface ShortPropertyAccessor extends IPropertyAccessor {
    short get(Object bean, String field);
    short getArrayElementAt(Object bean, String field, int index);

    void set(Object bean, String field, short value);
    void setArrayElementAt(Object bean, String field, int index, short value);
}
