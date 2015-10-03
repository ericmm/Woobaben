package woo.ba.ben.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class SimpleArrayMapTest {

    @Test
    public void shouldCreateSimpleArrayMapSuccessfully() {
        SimpleMap simpleMap = new SimpleArrayMap<>();

        assertThat(simpleMap, notNullValue());
        assertThat(simpleMap.size(), is(0));

        SimpleMap simpleMap2 = new SimpleArrayMap<>(5, 0.5f);
        assertThat(simpleMap2, notNullValue());
        assertThat(simpleMap2.size(), is(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenSizeIsNotPositive() {
        int[] invalidSizes = {-1, 0};
        for (int invalidSize : invalidSizes) {
            SimpleMap simpleMap = new SimpleArrayMap<>(invalidSize);
        }
    }

    @Test
    public void shouldThrowExceptionWhenFillFactorIsNotInZeroAndOne() {
        int size = 10;
        float[] invalidFillFactors = {-1f, 0f, 1f, 5f};
        for (float invalidFillFactor : invalidFillFactors) {
            try {
                SimpleMap simpleMap = new SimpleArrayMap<>(size, invalidFillFactor);
                fail();
            } catch (IllegalArgumentException e) {
                assertThat(e.getMessage(), is("FillFactor must be in (0, 1)"));
            }
        }
    }

    @Test
    public void testPerformance() throws InterruptedException {
        int size = 3000000;

        for (int times = 0; times<10; times++) {
            HashMap hashMap = new HashMap(size * 2);
            SimpleMap simpleMap = new SimpleArrayMap<>(size * 2, 0.75f);

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