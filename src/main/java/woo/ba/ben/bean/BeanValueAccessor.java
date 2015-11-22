package woo.ba.ben.bean;

import woo.ba.ben.core.SimpleArrayMap;
import woo.ba.ben.core.SimpleMap;

public class BeanValueAccessor {
    private final static SimpleMap<Class, IPropertyAccessor> ACCESSOR_MAP = new SimpleArrayMap<>(9);

    static {
        ACCESSOR_MAP.put(boolean.class, BooleanValueAccessor.getInstance());
        ACCESSOR_MAP.put(byte.class, ByteValueAccessor.getInstance());
        ACCESSOR_MAP.put(char.class, CharValueAccessor.getInstance());
        ACCESSOR_MAP.put(short.class, ShortValueAccessor.getInstance());
        ACCESSOR_MAP.put(int.class, IntValueAccessor.getInstance());
        ACCESSOR_MAP.put(float.class, FloatValueAccessor.getInstance());
        ACCESSOR_MAP.put(double.class, DoubleValueAccessor.getInstance());
        ACCESSOR_MAP.put(long.class, LongValueAccessor.getInstance());
        ACCESSOR_MAP.put(Object.class, TypedObjectValueAccessor.getInstance());
    }

    public IPropertyAccessor getValueAccessor(final Class clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException();
        }

        if(clazz.isPrimitive()) {
            return ACCESSOR_MAP.get(clazz);
        }

        return ACCESSOR_MAP.get(Object.class);
    }

}
