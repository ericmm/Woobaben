package woo.ba.ben.bean;


public interface DoublePropertyAccessor extends IPropertyAccessor {
    float get(Object bean, String field);
    float getArrayElementAt(Object bean, String field, int index);

    void set(Object bean, String field, float value);
    void setArrayElementAt(Object bean, String field, int index, float value);
}
