package woo.ba.ben.core;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.nio.ByteOrder;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;


public class UnsafeFactoryTest {
    private UnsafeFactory factory;

    @Test
    public void shouldGetUnsafeInstanceSuccessfully() {
        final Unsafe unsafe = factory.get();

        assertTrue(unsafe != null);
        assertTrue(UnsafeFactory.ADDRESS_SIZE > 0);
        assertTrue(UnsafeFactory.OBJECT_REF_SIZE > 0);
        assertTrue(UnsafeFactory.SYSTEM_BYTE_ORDER == ByteOrder.nativeOrder());
    }

    @Test
    public void shouldNotCreateUnsafeFactory() {
        try {
            Constructor<UnsafeFactory> constructor = UnsafeFactory.class.getDeclaredConstructor();
            factory = constructor.newInstance();
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("UnsafeFactory with modifiers \"private\""));
        }
    }


}