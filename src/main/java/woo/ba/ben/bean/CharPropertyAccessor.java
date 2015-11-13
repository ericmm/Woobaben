package woo.ba.ben.bean;


public interface CharPropertyAccessor extends IPropertyAccessor {
    char get(Object bean, String field);
    char getArrayElementAt(Object bean, String field, int index);

    void set(Object bean, String field, char value);
    void setArrayElementAt(Object bean, String field, int index, char value);
}
