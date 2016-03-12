package woo.ba.ben.field;


public interface LongPropertyAccessor extends IPropertyAccessor {
    long get(Object obj, String field);

    long getArrayElementAt(Object obj, String field, int index);

    void set(Object obj, String field, long value);

    void setArrayElementAt(Object obj, String field, int index, long value);
}
