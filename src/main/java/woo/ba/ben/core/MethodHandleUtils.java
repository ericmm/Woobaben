package woo.ba.ben.core;


import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

class MethodHandleUtils {
    private static final Lookup TRUSTED_LOOKUP;

    static {
        try {
            final Field trustedLookupField = Lookup.class.getDeclaredField("IMPL_LOOKUP");
            trustedLookupField.setAccessible(true);
            TRUSTED_LOOKUP = (Lookup) trustedLookupField.get(lookup());
        } catch (Throwable e) {
            throw new RuntimeException("Unable to get 'IMPL_LOOKUP' field from Lookup", e);
        }
    }

    private MethodHandleUtils() {
    }

    static MethodHandle findStatic(final Class clazz, final String methodName, final Class retClazz, final Class... argsClazz) throws NoSuchMethodException, IllegalAccessException {
        return TRUSTED_LOOKUP.findStatic(clazz, methodName, argsClazz == null ? methodType(retClazz) : methodType(retClazz, argsClazz));
    }

    static MethodHandle findVirtual(final Class clazz, final String methodName, final MethodType methodType) throws NoSuchMethodException, IllegalAccessException {
        return TRUSTED_LOOKUP.findVirtual(clazz, methodName, methodType);
    }

    static MethodHandle findVirtual(final Class clazz, final String methodName, final Class retClazz, final Class... argsClazz) throws NoSuchMethodException, IllegalAccessException {
        return TRUSTED_LOOKUP.findVirtual(clazz, methodName, argsClazz == null ? methodType(retClazz) : methodType(retClazz, argsClazz));
    }

    static MethodHandle voidStatic(final Class clazz, final String methodName, final Class... argsClazz) throws NoSuchMethodException, IllegalAccessException {
        return findStatic(clazz, methodName, void.class, argsClazz);
    }

    static MethodHandle voidStatic(final Class clazz, final String methodName) throws NoSuchMethodException, IllegalAccessException {
        return findStatic(clazz, methodName, void.class);
    }

    static MethodHandle getter(final Class clazz, final String propertyName, final Class propertyType) throws NoSuchFieldException, IllegalAccessException {
        return TRUSTED_LOOKUP.findGetter(clazz, propertyName, propertyType);
    }

    static MethodHandle setter(final Class clazz, final String propertyName, final Class propertyType) throws NoSuchFieldException, IllegalAccessException {
        return TRUSTED_LOOKUP.findSetter(clazz, propertyName, propertyType);
    }

    static MethodHandle staticGetter(final Class clazz, final String propertyName, final Class propertyType) throws NoSuchFieldException, IllegalAccessException {
        return TRUSTED_LOOKUP.findStaticGetter(clazz, propertyName, propertyType);
    }

    static MethodHandle staticSetter(final Class clazz, final String propertyName, final Class propertyType) throws NoSuchFieldException, IllegalAccessException {
        return TRUSTED_LOOKUP.findStaticSetter(clazz, propertyName, propertyType);
    }
}
