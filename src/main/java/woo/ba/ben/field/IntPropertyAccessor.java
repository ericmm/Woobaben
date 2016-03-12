package woo.ba.ben.field;


public interface IntPropertyAccessor extends IPropertyAccessor {
    int get(Object obj, String field);

    int getArrayElementAt(Object obj, String field, int index);

    void set(Object obj, String field, int value);

    void setArrayElementAt(Object obj, String field, int index, int value);
}
