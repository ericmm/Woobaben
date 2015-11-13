package woo.ba.ben.bean;


public interface IntPropertyAccessor extends IPropertyAccessor {
    int get(Object bean, String field);
    int getArrayElementAt(Object bean, String field, int index);

    void set(Object bean, String field, int value);
    void setArrayElementAt(Object bean, String field, int index, int value);
}
