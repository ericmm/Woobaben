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
@Warmup(iterations = 10, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 2, time = 10, timeUnit = SECONDS)
public class LookupBenchmark {

    private String name = "method";

    private MethodHandles.Lookup lookup;

    private MethodType methodType;

    private Class<?> returnType = void.class, declaringType = LookupBenchmark.class;

    void method() {
        /* empty */
    }

    @Setup
    public void setUp() throws Exception {
        lookup = MethodHandles.lookup();
        methodType = MethodType.methodType(void.class);
    }

    @Benchmark
    public Method reflection() throws Exception {
        return declaringType.getDeclaredMethod(name);
    }

    @Benchmark
    public MethodHandle handle() throws Exception {
        return MethodHandles.lookup().findVirtual(declaringType, name, MethodType.methodType(returnType));
    }

    @Benchmark
    public MethodHandle handlePreLookedUp() throws Exception {
        return lookup.findVirtual(declaringType, name, methodType);
    }

    @Benchmark
    public MethodHandle hiddenPreLookUp() throws Exception {
        return MethodHandleUtils.findVirtual(declaringType, name, methodType);
    }
}