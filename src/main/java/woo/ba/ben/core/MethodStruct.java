package woo.ba.ben.core;


import java.lang.reflect.Method;
import java.util.Objects;

class MethodStruct {
    final String name;
    final Class returnType;
    final Class[] parameterTypes;
//    public final MethodHandle methodHandle;

    MethodStruct(final Method method) {
        Objects.requireNonNull(method, "The method parameter cannot be null");
        name = method.getName();
        returnType = method.getReturnType();
        parameterTypes = method.getParameterTypes();

        final int modifiers = method.getModifiers();
//        final boolean isMethodPublic = Modifier.isPublic(modifiers);
//
//        if (Modifier.isStatic(modifiers)) {
//            this.methodHandle = MethodHandleUtils.findStatic(method.getDeclaringClass(), name, returnType, parameterTypes);
//        } else {
//            this.methodHandle = MethodHandleUtils.findVirtual(method.getDeclaringClass(), name, returnType, parameterTypes);
//        }
    }
}
