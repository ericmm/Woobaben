package woo.ba.ben.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


public class ClassCacheTest {
    private ClassCache cache = ClassCache.getInstance();

    @Test
    public void shouldPutClassIntoCache() {
        cache.put(TestClassObj.class);

        assertThat(cache.size(), is(2));

        final ClassStruct testClassObjStruct = cache.get(TestClassObj.class);
        final ClassStruct testFieldObjStruct = cache.get(TestFieldObj.class);

        assertThat(testClassObjStruct, notNullValue());
        assertThat(testFieldObjStruct, notNullValue());

        assertThat(testClassObjStruct.getField("testInt").isPrimitive(), is(true));
    }

    @Test
    public void shouldGetInstance() {
        cache = ClassCache.getInstance();

        assertThat(cache, notNullValue());
    }

    @Test
    public void shouldRemoveClassFromCache() {
        cache.put(TestClassObj.class);
        assertThat(cache.size(), is(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenNull() {
        cache.put(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenObjectClass() {
        cache.put(Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenAnnotationClass() {
        cache.put(Test.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenInterface() {
        cache.put(Runnable.class);
    }
}