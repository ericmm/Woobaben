package woo.ba.ben.core;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class FieldStructTest {

    @Test
    public void shouldCreateFieldStruct() throws NoSuchFieldException {
        final Field testInt = TestFieldObj.class.getDeclaredField("testInt");
        final Field integerValue = TestFieldObj.class.getDeclaredField("integerValue");
        final Field strings = TestFieldObj.class.getDeclaredField("strings");
        final Field twoDimensionShorts = TestFieldObj.class.getDeclaredField("twoDimensionShorts");

        final FieldStruct fieldStruct = new FieldStruct(testInt);

        assertThat(fieldStruct.name, is("testInt"));
        assertTrue(fieldStruct.type == int.class);
        assertTrue(fieldStruct.offset > 0);

        assertFalse(fieldStruct.isArray());
        assertTrue(fieldStruct.getArrayType() == null);
        assertTrue(fieldStruct.isPrimitive());

        final FieldStruct integerFieldStruct = new FieldStruct(integerValue);
        assertFalse(integerFieldStruct.isPrimitive());

        final FieldStruct stringsFieldStruct = new FieldStruct(strings);
        assertTrue(stringsFieldStruct.isArray());

        final FieldStruct twoDimensionShortsFieldStruct = new FieldStruct(twoDimensionShorts);
        assertTrue(twoDimensionShortsFieldStruct.isArray());
    }

    @Test
    public void shouldEqualWhenRealFieldIsSame() throws NoSuchFieldException {
        final Field testInt = TestFieldObj.class.getDeclaredField("testInt");
        final FieldStruct fieldStruct1 = new FieldStruct(testInt);
        final FieldStruct fieldStruct2 = new FieldStruct(testInt);

        assertTrue(fieldStruct1.equals(fieldStruct2));
    }

    @Test
    public void shouldGenerateHashcode() throws NoSuchFieldException{
        final Field testInt = TestFieldObj.class.getDeclaredField("testInt");
        final FieldStruct fieldStruct1 = new FieldStruct(testInt);
        assertThat(fieldStruct1.hashCode(), notNullValue());
    }
}