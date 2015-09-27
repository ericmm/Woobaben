package woo.ba.ben.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class SimpleArrayMapTest {

    @Test
    public void shouldCreateSimpleArrayMapSuccessfully(){
        SimpleMap simpleMap = new SimpleArrayMap<>();

        assertThat(simpleMap, notNullValue());
        assertThat(simpleMap.size(), is(0));

        SimpleMap simpleMap2 = new SimpleArrayMap<>(5, 0.5f);
        assertThat(simpleMap2, notNullValue());
        assertThat(simpleMap2.size(), is(0));
    }
    
    @Test(expected = IllegalArgumentException.class )
    public void shouldThrowExceptionWhenSizeIsNotPositive(){
        int[] invalidSizes = {-1, 0};
        for (int invalidSize: invalidSizes) {
            SimpleMap simpleMap = new SimpleArrayMap<>(invalidSize);
        }
    }

    @Test
    public void shouldThrowExceptionWhenFillFactorIsNotInZeroAndOne(){
        int size = 10;
        float[] invalidFillFactors = {-1f, 0f, 1f, 5f};
        for (float invalidFillFactor: invalidFillFactors) {
            try {
                SimpleMap simpleMap = new SimpleArrayMap<>(size, invalidFillFactor);
                fail();
            }catch (IllegalArgumentException e) {
                assertThat(e.getMessage(), is("FillFactor must be in (0, 1)"));
            }
        }
    }
}