package woo.ba.ben.field;


public interface BooleanPropertyAccessor extends IPropertyAccessor {
    boolean get(Object obj, String field);

    boolean getArrayElementAt(Object obj, String field, int index);

    void set(Object obj, String field, boolean value);

    void setArrayElementAt(Object obj, String field, int index, boolean value);
}
