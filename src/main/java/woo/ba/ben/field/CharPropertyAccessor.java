package woo.ba.ben.field;


public interface CharPropertyAccessor extends IPropertyAccessor {
    char get(Object obj, String field);

    char getArrayElementAt(Object obj, String field, int index);

    void set(Object obj, String field, char value);

    void setArrayElementAt(Object obj, String field, int index, char value);
}
