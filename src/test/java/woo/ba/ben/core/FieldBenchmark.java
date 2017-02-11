package woo.ba.ben.core;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 1)
@Warmup(iterations = 5, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 5, time = 3, timeUnit = SECONDS)
public class FieldBenchmark {
    private String value = "foo";
    private int primitiveValue = 42;

    enum Access {
        INSTANCE;
        private String value = "bar";
    }

    private Field reflective, reflectiveAccessible, reflectivePrimitive, reflectiveAccessiblePrimitive, reflectiveAccessiblePrivate;
    private MethodHandle methodHandle, methodHandleUnreflected, methodHandlePrimitive, methodHandleUnreflectedPrimitive, methodHandleUnreflectedPrivate;

    private static final MethodHandle METHOD_HANDLE_INLINE, METHOD_HANDLE_UNREFLECTED_INLINE, METHOD_HANDLE_PRIMITIVE_INLINE, METHOD_HANDLE_UNREFLECTED_PRIMITIVE, METHOD_HANDLE_UNREFLECTED_PRIVATE;

    static {
        try {
            METHOD_HANDLE_INLINE = MethodHandleUtils.getter(FieldBenchmark.class, "value", String.class);
            METHOD_HANDLE_UNREFLECTED_INLINE = MethodHandles.lookup().unreflectGetter(FieldBenchmark.class.getDeclaredField("value"));
            METHOD_HANDLE_PRIMITIVE_INLINE = MethodHandleUtils.getter(FieldBenchmark.class, "primitiveValue", int.class);
            METHOD_HANDLE_UNREFLECTED_PRIMITIVE = MethodHandles.lookup().unreflectGetter(FieldBenchmark.class.getDeclaredField("primitiveValue"));
            Field reflectiveAccessiblePrivate = Access.class.getDeclaredField("value");
            reflectiveAccessiblePrivate.setAccessible(true);
            METHOD_HANDLE_UNREFLECTED_PRIVATE = MethodHandles.lookup().unreflectGetter(reflectiveAccessiblePrivate);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Setup
    public void setup() throws Exception {
        reflective = FieldBenchmark.class.getDeclaredField("value");

        reflectiveAccessible = FieldBenchmark.class.getDeclaredField("value");
        reflectiveAccessible.setAccessible(true);

        reflectivePrimitive = FieldBenchmark.class.getDeclaredField("primitiveValue");

        reflectiveAccessiblePrimitive = FieldBenchmark.class.getDeclaredField("primitiveValue");
        reflectiveAccessiblePrimitive.setAccessible(true);

        methodHandle = MethodHandleUtils.getter(FieldBenchmark.class, "value", String.class);

        methodHandleUnreflected = MethodHandles.lookup().unreflectGetter(reflective);

        methodHandlePrimitive = MethodHandles.lookup().findGetter(FieldBenchmark.class, "primitiveValue", int.class);

        methodHandleUnreflectedPrimitive = MethodHandles.lookup().unreflectGetter(reflectivePrimitive);

        reflectiveAccessiblePrivate = Access.class.getDeclaredField("value");
        reflectiveAccessiblePrivate.setAccessible(true);

        methodHandleUnreflectedPrivate = MethodHandles.lookup().unreflectGetter(reflectiveAccessiblePrivate);
    }

    @Benchmark
    public Object normal() {
        return value;
    }

    @Benchmark
    public Object reflection() throws InvocationTargetException, IllegalAccessException {
        return reflective.get(this);
    }

    @Benchmark
    public Object reflectionAccessible() throws InvocationTargetException, IllegalAccessException {
        return reflectiveAccessible.get(this);
    }

    @Benchmark
    public Object handle() throws Throwable {
        return methodHandle.invoke(this);
    }

    @Benchmark
    public Object handleExact() throws Throwable {
        return (String) methodHandle.invokeExact(this);
    }

    @Benchmark
    public Object handleUnreflected() throws Throwable {
        return methodHandleUnreflected.invoke(this);
    }

    @Benchmark
    public Object handleUnreflectedExact() throws Throwable {
        return (String) methodHandleUnreflected.invokeExact(this);
    }

    @Benchmark
    public int primitive() {
        return primitiveValue;
    }

    @Benchmark
    public int reflectionPrimitive() throws InvocationTargetException, IllegalAccessException {
        return (int) reflectivePrimitive.get(this);
    }

    @Benchmark
    public int reflectionAccessiblePrimitive() throws InvocationTargetException, IllegalAccessException {
        return (int) reflectiveAccessiblePrimitive.get(this);
    }

    @Benchmark
    public int reflectionSpecializedPrimitive() throws InvocationTargetException, IllegalAccessException {
        return reflectivePrimitive.getInt(this);
    }

    @Benchmark
    public int reflectionAccessibleSpecializedPrimitive() throws InvocationTargetException, IllegalAccessException {
        return reflectiveAccessiblePrimitive.getInt(this);
    }

    @Benchmark
    public int handlePrimitive() throws Throwable {
        return (int) methodHandlePrimitive.invoke(this);
    }

    @Benchmark
    public int handleExactPrimitive() throws Throwable {
        return (int) methodHandlePrimitive.invokeExact(this);
    }

    @Benchmark
    public int handleUnreflectedPrimitive() throws Throwable {
        return (int) methodHandleUnreflectedPrimitive.invoke(this);
    }

    @Benchmark
    public int handleUnreflectedExactPrimitive() throws Throwable {
        return (int) methodHandleUnreflectedPrimitive.invokeExact(this);
    }

    @Benchmark
    public String privateNormal() {
        return Access.INSTANCE.value; // accessor method
    }

    @Benchmark
    public Object reflectionAccessiblePrivate() throws Exception {
        return reflectiveAccessiblePrivate.get(Access.INSTANCE);
    }

    @Benchmark
    public String handleUnreflectedPrivate() throws Throwable {
        return (String) methodHandleUnreflectedPrivate.invokeExact((Access) Access.INSTANCE);
    }

    @Benchmark
    public Object handleInline() throws Throwable {
        return METHOD_HANDLE_INLINE.invoke(this);
    }

    @Benchmark
    public Object handleExactInline() throws Throwable {
        return (String) METHOD_HANDLE_INLINE.invokeExact(this);
    }

    @Benchmark
    public Object handleUnreflectedInline() throws Throwable {
        return METHOD_HANDLE_UNREFLECTED_INLINE.invoke(this);
    }

    @Benchmark
    public Object handleUnreflectedExactInline() throws Throwable {
        return (String) METHOD_HANDLE_UNREFLECTED_INLINE.invokeExact(this);
    }

    @Benchmark
    public int handlePrimitiveInline() throws Throwable {
        return (int) METHOD_HANDLE_PRIMITIVE_INLINE.invoke(this);
    }

    @Benchmark
    public int handleExactPrimitiveInline() throws Throwable {
        return (int) METHOD_HANDLE_PRIMITIVE_INLINE.invokeExact(this);
    }

    @Benchmark
    public int handleUnreflectedPrimitiveInline() throws Throwable {
        return (int) METHOD_HANDLE_UNREFLECTED_PRIMITIVE.invoke(this);
    }

    @Benchmark
    public int handleUnreflectedExactPrimitiveInline() throws Throwable {
        return (int) METHOD_HANDLE_UNREFLECTED_PRIMITIVE.invokeExact(this);
    }

    @Benchmark
    public String handleUnreflectedPrivateInline() throws Throwable {
        return (String) METHOD_HANDLE_UNREFLECTED_PRIVATE.invokeExact((Access) Access.INSTANCE);
    }
}
