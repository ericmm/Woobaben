package woo.ba.ben.bean;


public interface FloatPropertyAccessor extends IPropertyAccessor {
    double get(Object bean, String field);
    double getArrayElementAt(Object bean, String field, int index);

    void set(Object bean, String field, double value);
    void setArrayElementAt(Object bean, String field, int index, double value);
}
