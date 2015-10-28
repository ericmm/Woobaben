package woo.ba.ben.core;

import org.junit.Test;
import sun.misc.Unsafe;

import java.nio.ByteOrder;

import static org.junit.Assert.assertTrue;


public class UnsafeFactoryTest {
    @Test
    public void shouldGetUnsafeInstanceSuccessfully() {
        final Unsafe unsafe = UnsafeFactory.get();

        assertTrue(unsafe != null);
        assertTrue(UnsafeFactory.ADDRESS_SIZE > 0);
        assertTrue(UnsafeFactory.OBJECT_REF_SIZE > 0);
        assertTrue(UnsafeFactory.SYSTEM_BYTE_ORDER == ByteOrder.nativeOrder());
    }

}