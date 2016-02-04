package woo.ba.ben.bean;

import woo.ba.ben.core.ArrayBackedHashMap;

import java.util.Map;

public class BeanValueAccessor {
    private final static Map<Class, IPropertyAccessor> ACCESSOR_MAP = new ArrayBackedHashMap<>(9);

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

    private BeanValueAccessor() {
    }

    public static IPropertyAccessor getAccessorByClass(final Class clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException();
        }

        if (clazz.isPrimitive()) {
            return ACCESSOR_MAP.get(clazz);
        }

        return ACCESSOR_MAP.get(Object.class);
    }

    public static BooleanPropertyAccessor getBooleanValueAccessor() {
        return BooleanValueAccessor.getInstance();
    }

    public static BytePropertyAccessor getByteValueAccessor() {
        return ByteValueAccessor.getInstance();
    }

    public static CharPropertyAccessor getCharValueAccessor() {
        return CharValueAccessor.getInstance();
    }

    public static ShortPropertyAccessor getShortValueAccessor() {
        return ShortValueAccessor.getInstance();
    }

    public static IntPropertyAccessor getIntValueAccessor() {
        return IntValueAccessor.getInstance();
    }

    public static FloatPropertyAccessor getFloatValueAccessor() {
        return FloatValueAccessor.getInstance();
    }

    public static DoublePropertyAccessor getDoubleValueAccessor() {
        return DoubleValueAccessor.getInstance();
    }

    public static LongPropertyAccessor getLongValueAccessor() {
        return LongValueAccessor.getInstance();
    }

    public static TypedObjectPropertyAccessor getTypedObjectValueAccessor() {
        return TypedObjectValueAccessor.getInstance();
    }

}
