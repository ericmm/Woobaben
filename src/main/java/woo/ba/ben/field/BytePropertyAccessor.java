package woo.ba.ben.field;


public interface BytePropertyAccessor extends IPropertyAccessor {
    byte get(Object obj, String field);

    byte getArrayElementAt(Object obj, String field, int index);

    void set(Object obj, String field, byte value);

    void setArrayElementAt(Object obj, String field, int index, byte value);
}
