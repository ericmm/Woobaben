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
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 1)
@Warmup(iterations = 5, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 5, time = 3, timeUnit = SECONDS)
public class InvocationBenchmark {

    private String s1 = "foo", s2 = "bar", s3 = "qux", s4 = "baz";

    private String method(String a, String b, String c, String d) {
        return a + b + c + d;
    }

    private int i1 = 1, i2 = 2, i3 = 3, i4 = 4;

    private int methodPrimitive(int a, int b, int c, int d) {
        return a + b + c + d;
    }

    enum Access {

        INSTANCE;

        private String method(String a, String b, String c, String d) {
            return a + b + c + d;
        }
    }

    private Method method, methodAccessible, methodPrimitive, methodAccessiblePrimitive, methodAccessiblePrivate;

    private MethodHandle methodHandle, methodHandleUnreflected, methodHandlePrimitive, methodHandleUnreflectedPrimitive, methodHandleUnreflectedPrivate;

    private static final MethodHandle METHOD_HANDLE_INLINE, METHOD_HANDLE_UNREFLECTED_INLINE, METHOD_HANDLE_PRIMITIVE_INLINE, METHOD_HANDLE_UNREFLECTED_PRIMITIVE_INLINE, METHOD_HANDLE_UNREFLECTED_PRIVATE_INLINE;

    static {
        try {
            Method methodAccessible = InvocationBenchmark.class.getDeclaredMethod("method", String.class, String.class, String.class, String.class);
            methodAccessible.setAccessible(true);
            METHOD_HANDLE_INLINE = MethodHandleUtils.findVirtual(InvocationBenchmark.class, "method",
                    MethodType.methodType(String.class, String.class, String.class, String.class, String.class));
            METHOD_HANDLE_UNREFLECTED_INLINE = MethodHandles.lookup().unreflect(methodAccessible);
            Method methodAccessiblePrimitive = InvocationBenchmark.class.getDeclaredMethod("methodPrimitive", int.class, int.class, int.class, int.class);
            methodAccessiblePrimitive.setAccessible(true);
            METHOD_HANDLE_PRIMITIVE_INLINE = MethodHandleUtils.findVirtual(InvocationBenchmark.class, "methodPrimitive",
                    MethodType.methodType(int.class, int.class, int.class, int.class, int.class));
            METHOD_HANDLE_UNREFLECTED_PRIMITIVE_INLINE = MethodHandles.lookup().unreflect(methodAccessiblePrimitive);
            Method methodAccessiblePrivate = Access.class.getDeclaredMethod("method", String.class, String.class, String.class, String.class);
            methodAccessiblePrivate.setAccessible(true);
            METHOD_HANDLE_UNREFLECTED_PRIVATE_INLINE = MethodHandles.lookup().unreflect(methodAccessiblePrivate);
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Setup
    public void setUp() throws Exception {
        method = InvocationBenchmark.class.getDeclaredMethod("method", String.class, String.class, String.class, String.class);
        methodAccessible = InvocationBenchmark.class.getDeclaredMethod("method", String.class, String.class, String.class, String.class);
        methodAccessible.setAccessible(true);
        methodHandle = MethodHandleUtils.findVirtual(InvocationBenchmark.class, "method",
                MethodType.methodType(String.class, String.class, String.class, String.class, String.class));
        methodHandleUnreflected = MethodHandles.lookup().unreflect(methodAccessible);
        methodPrimitive = InvocationBenchmark.class.getDeclaredMethod("methodPrimitive", int.class, int.class, int.class, int.class);
        methodAccessiblePrimitive = InvocationBenchmark.class.getDeclaredMethod("methodPrimitive", int.class, int.class, int.class, int.class);
        methodAccessiblePrimitive.setAccessible(true);
        methodHandlePrimitive = MethodHandleUtils.findVirtual(InvocationBenchmark.class, "methodPrimitive",
                MethodType.methodType(int.class, int.class, int.class, int.class, int.class));
        methodHandleUnreflectedPrimitive = MethodHandles.lookup().unreflect(methodAccessiblePrimitive);
        methodAccessiblePrivate = Access.class.getDeclaredMethod("method", String.class, String.class, String.class, String.class);
        methodAccessiblePrivate.setAccessible(true);
        methodHandleUnreflectedPrivate = MethodHandles.lookup().unreflect(methodAccessiblePrivate);
    }

    @Benchmark
    public Object normal() throws Exception {
        return method(s1, s2, s3, s4);
    }

    @Benchmark
    public Object reflection() throws Exception {
        return method.invoke(this, s1, s2, s3, s4);
    }

    @Benchmark
    public Object reflectionAccessible() throws Exception {
        return methodAccessible.invoke(this, s1, s2, s3, s4);
    }

    @Benchmark
    public Object handle() throws Throwable {
        return methodHandle.invoke(this, s1, s2, s3, s4);
    }

    @Benchmark
    public Object handleExact() throws Throwable {
        return (String) methodHandle.invokeExact(this, s1, s2, s3, s4);
    }

    @Benchmark
    public Object handleUnreflectedExact() throws Throwable {
        return (String) methodHandleUnreflected.invokeExact(this, s1, s2, s3, s4);
    }

    @Benchmark
    public int primitive() {
        return methodPrimitive(i1, i2, i3, i4);
    }

    @Benchmark
    public int reflectionPrimitive() throws Throwable {
        return (int) methodPrimitive.invoke(this, i1, i2, i3, i4);
    }

    @Benchmark
    public int reflectionAccessiblePrimitive() throws Throwable {
        return (int) methodAccessiblePrimitive.invoke(this, i1, i2, i3, i4);
    }

    @Benchmark
    public int handlePrimitive() throws Throwable {
        return (int) methodHandlePrimitive.invoke(this, i1, i2, i3, i4);
    }

    @Benchmark
    public int handlePrimitiveBoxed() throws Throwable {
        return (Integer) methodHandlePrimitive.invoke(this, Integer.valueOf(i1), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4));
    }

    @Benchmark
    public int handlePrimitiveExact() throws Throwable {
        return (int) methodHandlePrimitive.invokeExact(this, i1, i2, i3, i4);
    }

    @Benchmark
    public Object handleUnreflectedPrimitiveExact() throws Throwable {
        return (int) methodHandleUnreflectedPrimitive.invokeExact(this, i1, i2, i3, i4);
    }

    @Benchmark
    public Object privateNormal() throws Exception {
        return Access.INSTANCE.method(s1, s2, s3, s4); // accessor method indirection
    }

    @Benchmark
    public Object reflectionAccessiblePrivate() throws Exception {
        return methodAccessiblePrivate.invoke(Access.INSTANCE, s1, s2, s3, s4);
    }

    @Benchmark
    public Object handleUnreflectedExactPrivate() throws Throwable {
        return (String) methodHandleUnreflectedPrivate.invokeExact(Access.INSTANCE, s1, s2, s3, s4);
    }

    @Benchmark
    public Object handleInline() throws Throwable {
        return METHOD_HANDLE_INLINE.invoke(this, s1, s2, s3, s4);
    }

    @Benchmark
    public Object handleExactInline() throws Throwable {
        return (String) METHOD_HANDLE_INLINE.invokeExact(this, s1, s2, s3, s4);
    }

    @Benchmark
    public Object handleUnreflectedExactInline() throws Throwable {
        return (String) METHOD_HANDLE_UNREFLECTED_INLINE.invokeExact(this, s1, s2, s3, s4);
    }

    @Benchmark
    public int handlePrimitiveInline() throws Throwable {
        return (int) METHOD_HANDLE_PRIMITIVE_INLINE.invoke(this, i1, i2, i3, i4);
    }

    @Benchmark
    public int handlePrimitiveBoxedInline() throws Throwable {
        return (Integer) METHOD_HANDLE_PRIMITIVE_INLINE.invoke(this, Integer.valueOf(i1), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4));
    }

    @Benchmark
    public int handlePrimitiveExactInline() throws Throwable {
        return (int) METHOD_HANDLE_UNREFLECTED_PRIMITIVE_INLINE.invokeExact(this, i1, i2, i3, i4);
    }

    @Benchmark
    public int handleUnreflectedPrimitiveExactInline() throws Throwable {
        return (int) METHOD_HANDLE_UNREFLECTED_PRIMITIVE_INLINE.invokeExact(this, i1, i2, i3, i4);
    }

    @Benchmark
    public Object handleUnreflectedExactPrivateInline() throws Throwable {
        return (String) METHOD_HANDLE_UNREFLECTED_PRIVATE_INLINE.invokeExact(Access.INSTANCE, s1, s2, s3, s4);
    }
}