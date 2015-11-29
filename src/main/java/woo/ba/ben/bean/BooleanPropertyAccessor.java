package woo.ba.ben.bean;


public interface BooleanPropertyAccessor extends IPropertyAccessor {
    boolean get(Object bean, String field);

    boolean getArrayElementAt(Object bean, String field, int index);

    void set(Object bean, String field, boolean value);

    void setArrayElementAt(Object bean, String field, int index, boolean value);
}
