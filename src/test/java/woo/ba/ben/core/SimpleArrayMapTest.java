package woo.ba.ben.core;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class SimpleArrayMapTest {

    private Map simpleMap;

    @Before
    public void setup() {
        simpleMap = new SimpleArrayMap<>();
    }

    @Test
    public void shouldCreateSimpleArrayMapSuccessfully() {
        assertThat(simpleMap, notNullValue());
        assertThat(simpleMap.size(), is(0));

        final Map simpleMap2 = new SimpleArrayMap<>(5, 0.5f);
        assertThat(simpleMap2, notNullValue());
        assertThat(simpleMap2.size(), is(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenSizeIsNotPositive() {
        for (final int invalidSize : new int[] {-1, 0}) {
            new SimpleArrayMap<>(invalidSize);
        }
    }

    @Test
    public void shouldThrowExceptionWhenFillFactorIsNotInZeroAndOne() {
        final int size = 10;
        final float[] invalidFillFactors = {-1f, 0f, 1f, 5f};
        for (final float invalidFillFactor : invalidFillFactors) {
            try {
                final Map simpleMap = new SimpleArrayMap<>(size, invalidFillFactor);
                fail();
            } catch (final IllegalArgumentException e) {
                assertThat(e.getMessage(), is("FillFactor must be in (0, 1)"));
            }
        }
    }

    @Test
    public void shouldPutAndGetValueSuccessfully() {
        simpleMap.put("nullValue", null);
        assertThat(simpleMap.get("nullValue"), nullValue());

        simpleMap.put("notNullValue", new Object());
        assertThat(simpleMap.get("notNullValue"), notNullValue());

        assertThat(simpleMap.get("notInMap"), nullValue());
    }

    @Test
    public void shouldRemoveValueSuccessfully() {
        assertThat(simpleMap.size(), is(0));
        simpleMap.put("key", "value");
        assertThat(simpleMap.size(), is(1));

        assertThat(simpleMap.remove("key"), is("value"));
        assertThat(simpleMap.size(), is(0));

        assertThat(simpleMap.remove("notInMap"), nullValue());
        assertThat(simpleMap.size(), is(0));
    }

    @Test
    public void shouldClearSuccessfully() {
        assertThat(simpleMap.size(), is(0));
        simpleMap.put("key", "value");
        assertThat(simpleMap.size(), is(1));

        simpleMap.clear();
        assertThat(simpleMap.size(), is(0));
    }

    @Test
    public void shouldReturnIsEmptySuccessfully() {
        assertThat(simpleMap.size(), is(0));
        assertTrue(simpleMap.isEmpty());
        simpleMap.put("key", "value");
        assertThat(simpleMap.size(), is(1));
        assertFalse(simpleMap.isEmpty());

        simpleMap.clear();
        assertThat(simpleMap.size(), is(0));
        assertTrue(simpleMap.isEmpty());
    }

    @Test
    public void shouldGetOrDefaultSuccessfully() {
        final String defaultValue = (String) simpleMap.getOrDefault("notInMap", "defaultValue");
        assertThat(defaultValue, is("defaultValue"));

        simpleMap.put("inMap", "someValue");
        final String value = (String) simpleMap.getOrDefault("inMap", "defaultValue2");
        assertThat(value, is("someValue"));
    }

    @Test
    public void shouldPutIfAbsentSuccessfully() {
        assertThat(simpleMap.put("nullInMap", null), nullValue());

        assertThat(simpleMap.get("nullInMap"), nullValue());

        assertThat(simpleMap.putIfAbsent("nullInMap", "someValue"), nullValue());
        assertThat(simpleMap.get("nullInMap"), is("someValue"));

        assertThat(simpleMap.putIfAbsent("anotherKeyInMap", "test"), nullValue());
        assertThat(simpleMap.get("anotherKeyInMap"), is("test"));
    }


    @Test
    @Ignore
    public void testPerformance() throws InterruptedException {
        final int size = 5_000_000;

        for (int times = 0; times < 5; times++) {
            final HashMap hashMap = new HashMap(size * 2);
            final Map simpleMap = new SimpleArrayMap<>(size * 2, 0.75f);

            //warm up
            for (int i = 0; i < 100; i++) {
                final String obj = String.valueOf(i);
                simpleMap.put(obj, obj);
                simpleMap.remove(obj);

                hashMap.put(obj, obj);
                hashMap.remove(obj);
            }

            final long start = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                hashMap.put("int", String.valueOf(i));
                hashMap.put(String.valueOf(i), "int");
            }
            final long end = System.currentTimeMillis();
            System.out.println("HashMap add used :" + (end - start));

            System.gc();


            final long start1 = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                simpleMap.put("int", String.valueOf(i));
                simpleMap.put(String.valueOf(i), "int");
            }
            final long end1 = System.currentTimeMillis();
            System.out.println("SimpleMap add used :" + (end1 - start1));

            System.gc();

            final long start2 = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                hashMap.remove("int");
                hashMap.remove(String.valueOf(i));
            }
            final long end2 = System.currentTimeMillis();
            System.out.println("HashMap remove used :" + (end2 - start2));

            System.gc();

            final long start3 = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                simpleMap.remove("int");
                simpleMap.remove(String.valueOf(i));
            }
            final long end3 = System.currentTimeMillis();
            System.out.println("SimpleMap remove used :" + (end3 - start3));
        }
    }
}