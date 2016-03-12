package woo.ba.ben.field;


public interface DoublePropertyAccessor extends IPropertyAccessor {
    double get(Object obj, String field);

    double getArrayElementAt(Object obj, String field, int index);

    void set(Object obj, String field, double value);

    void setArrayElementAt(Object obj, String field, int index, double value);
}
