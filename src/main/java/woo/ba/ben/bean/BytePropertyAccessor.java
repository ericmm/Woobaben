package woo.ba.ben.bean;


public interface BytePropertyAccessor extends IPropertyAccessor {
    byte get(Object bean, String field);

    byte getArrayElementAt(Object bean, String field, int index);

    void set(Object bean, String field, byte value);

    void setArrayElementAt(Object bean, String field, int index, byte value);
}
