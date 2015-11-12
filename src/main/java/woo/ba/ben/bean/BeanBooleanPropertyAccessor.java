package woo.ba.ben.bean;


public class BeanBooleanPropertyAccessor implements BooleanPropertyAccessor{

    @Override
    public boolean get(Object bean, String field) {
        return false;
    }

    @Override
    public boolean getStatic(Class bean, String field) {
        return false;
    }

    @Override
    public boolean getArrayElementAt(Object bean, String field, int index) {
        return false;
    }

    @Override
    public void set(Object bean, String field, boolean value) {

    }

    @Override
    public void setStatic(Class beanClass, String field, boolean value) {

    }

    @Override
    public void setArrayElementAt(Object bean, String field, int index, boolean value) {

    }
}
