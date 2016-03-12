package woo.ba.ben.field;


public interface FloatPropertyAccessor extends IPropertyAccessor {
    float get(Object obj, String field);

    float getArrayElementAt(Object obj, String field, int index);

    void set(Object obj, String field, float value);

    void setArrayElementAt(Object obj, String field, int index, float value);
}
