package woo.ba.ben.bean;


public class UnsafePropertyAccessor implements IPropertyAccessor {

    @Override
    public Object get(Object bean, String field) {
        throw new RuntimeException("implement me");
    }
}
