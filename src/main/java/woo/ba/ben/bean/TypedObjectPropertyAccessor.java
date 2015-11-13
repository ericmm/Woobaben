package woo.ba.ben.bean;


public interface TypedObjectPropertyAccessor<T> extends IPropertyAccessor {
    T get(Object bean, String field);
    T getArrayElementAt(Object bean, String field, int index);

    void set(Object bean, String field, T value);
    void setArrayElementAt(Object bean, String field, int index, T value);
}
