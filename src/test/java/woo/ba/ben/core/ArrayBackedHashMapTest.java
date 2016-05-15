package woo.ba.ben.core;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import woo.ba.ben.experimental.ArrayBackedHashMap;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.random;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ArrayBackedHashMapTest {

    private Map simpleMap;

    @Before
    public void setup() {
        simpleMap = new ArrayBackedHashMap<>();
    }

    @Test
    public void shouldCreateSimpleArrayMapSuccessfully() {
        assertThat(simpleMap, notNullValue());
        assertThat(simpleMap.size(), is(0));

        final Map simpleMap2 = new ArrayBackedHashMap<>(5, 0.5f);
        assertThat(simpleMap2, notNullValue());
        assertThat(simpleMap2.size(), is(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenSizeIsNotPositive() {
        for (final int invalidSize : new int[]{-1, 0}) {
            new ArrayBackedHashMap<>(invalidSize);
        }
    }

    @Test
    public void shouldThrowExceptionWhenFillFactorIsNotInZeroAndOne() {
        final int size = 10;
        final float[] invalidFillFactors = {-1f, 0f, 1f, 5f};
        for (final float invalidFillFactor : invalidFillFactors) {
            try {
                final Map simpleMap = new ArrayBackedHashMap<>(size, invalidFillFactor);
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

        simpleMap = new ArrayBackedHashMap<String, String>(1);
        assertThat(simpleMap.size(), is(0));

        simpleMap.put("1", "2");
        assertThat(simpleMap.size(), is(1));
        assertThat(simpleMap.get("1"), is("2"));

        simpleMap.put("2", "5");
        simpleMap.put("3", "6");
        simpleMap.put("4", "7");
        assertThat(simpleMap.size(), is(4));
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

        final int size = 500_000;
        simpleMap = new ArrayBackedHashMap<>(size * 2);
        String[] keys = new String[size];
        String[] values = new String[size];
        for (int i = 0; i < size; i++) {
            keys[i] = "int"+i;
            values[i] = String.valueOf(random()+i);
        }

        for (int i = 0; i < size; i++) {
            simpleMap.put(keys[i], values[i]);
            simpleMap.put(values[i], keys[i]);
        }

        assertThat(simpleMap.size(), is(size *2));

        for (int i = 0; i < size; i++) {
            simpleMap.remove(keys[i]);
            simpleMap.remove(values[i]);
        }
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

        for (int times = 0; times < 3; times++) {
            try {
                System.gc();
                Thread.sleep(1_000);
            } catch (Exception e){}

            final HashMap hashMap = new HashMap(size * 2);
            final Map simpleMap = new ArrayBackedHashMap<>(size * 2);

            //warm up
            for (int i = 0; i < 100; i++) {
                final String obj = String.valueOf(i);
                simpleMap.put(obj, obj);
                simpleMap.remove(obj);

                hashMap.put(obj, obj);
                hashMap.remove(obj);
            }

            String[] keys = new String[size];
            String[] values = new String[size];
            for (int i = 0; i < size; i++) {
                keys[i] = "int"+i;
                values[i] = String.valueOf(random());
            }


            final long start = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                hashMap.put(keys[i], values[i]);
                hashMap.put(values[i], keys[i]);
            }
            final long end = System.currentTimeMillis();
            System.out.println("HashMap add used :" + (end - start));

            final long start1 = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                simpleMap.put(keys[i], values[i]);
                simpleMap.put(values[i], keys[i]);
            }
            final long end1 = System.currentTimeMillis();
            System.out.println("SimpleMap add used :" + (end1 - start1));

            final long start2 = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                hashMap.remove(keys[i]);
                hashMap.remove(values[i]);
            }
            final long end2 = System.currentTimeMillis();
            System.out.println("HashMap remove used :" + (end2 - start2));

            final long start3 = System.currentTimeMillis();
            for (int i = 0; i < size; i++) {
                simpleMap.remove(keys[i]);
                simpleMap.remove(values[i]);
            }
            final long end3 = System.currentTimeMillis();
            System.out.println("SimpleMap remove used :" + (end3 - start3));
        }
    }
}