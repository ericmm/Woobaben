package woo.ba.ben.core;

import org.junit.Test;
import sun.misc.Unsafe;

import static org.junit.Assert.assertTrue;


public class UnsafeFactoryTest {
    @Test
    public void shouldGetUnsafeInstanceSuccessfully() {
        final Unsafe unsafe = UnsafeFactory.UNSAFE;

        assertTrue(unsafe != null);
        assertTrue(UnsafeFactory.OBJECT_REF_SIZE > 0);
    }

}