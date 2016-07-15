package woo.ba.ben.core;


import java.lang.invoke.MethodHandle;

import static java.lang.invoke.MethodHandles.lookup;
import static java.lang.invoke.MethodType.methodType;

public class MethodHandleUtils {

    private MethodHandleUtils(){}

    public static MethodHandle lookupStatic(final Class clazz, final String methodName, final Class retClazz, final Class... argsClazz) {
        try {
            return lookup().findStatic(clazz, methodName, argsClazz == null ? methodType(retClazz) : methodType(retClazz, argsClazz));
        } catch (final Exception e) {
            throw new RuntimeException("findStatic() failed", e);
        }
    }

    public static MethodHandle lookupVoidStatic(final Class clazz, final String methodName, final Class... argsClazz) {
        return lookupStatic(clazz, methodName, void.class, argsClazz);
    }

    public static MethodHandle lookupVoidStaticWithNoArgs(final Class clazz, final String methodName) {
        return lookupStatic(clazz, methodName, void.class);
    }

    public static MethodHandle lookupVoidStaticWithOneArgs(final Class clazz, final String methodName, final Class firstArgsClass) {
        return lookupStatic(clazz, methodName, void.class, firstArgsClass);
    }


}
