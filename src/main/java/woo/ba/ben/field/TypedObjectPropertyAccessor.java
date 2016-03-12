package woo.ba.ben.field;


public interface TypedObjectPropertyAccessor<T> extends IPropertyAccessor {
    T get(Object obj, String field);

    T getArrayElementAt(Object obj, String field, int index);

    void set(Object obj, String field, T value);

    void setArrayElementAt(Object obj, String field, int index, T value);
}
