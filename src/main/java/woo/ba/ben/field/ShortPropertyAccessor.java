package woo.ba.ben.field;


public interface ShortPropertyAccessor extends IPropertyAccessor {
    short get(Object obj, String field);

    short getArrayElementAt(Object obj, String field, int index);

    void set(Object obj, String field, short value);

    void setArrayElementAt(Object obj, String field, int index, short value);
}
