package woo.ba.ben.core;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class FieldStructTest {

    @Test
    public void shouldCreateFieldStruct() throws NoSuchFieldException {
        final Field testInt = TestFieldObj.class.getDeclaredField("testInt");
        final Field integerValue = TestFieldObj.class.getDeclaredField("integerValue");
        final Field strings = TestFieldObj.class.getDeclaredField("strings");
        final Field twoDimensionShorts = TestFieldObj.class.getDeclaredField("twoDimensionShorts");
        final Field stringList = TestFieldObj.class.getDeclaredField("stringList");
        final Field byteBooleanMap = TestFieldObj.class.getDeclaredField("byteBooleanMap");

        final FieldStruct fieldStruct = new FieldStruct(testInt);

        assertThat(fieldStruct.name, is("testInt"));
        assertThat(fieldStruct.realField, is(testInt));
        assertTrue(fieldStruct.type == int.class);
        assertTrue(fieldStruct.offset > 0);

        assertFalse(fieldStruct.isArray());
        assertTrue(fieldStruct.getFlatType() == int.class);
        assertTrue(fieldStruct.getArrayType() == null);
        assertThat(fieldStruct.getParameterizedTypes(), nullValue());
        assertTrue(fieldStruct.isPrimitive());
        assertTrue(fieldStruct.isStatic());
        assertFalse(fieldStruct.hasParameterizedType());

        final FieldStruct integerFieldStruct = new FieldStruct(integerValue);
        assertFalse(integerFieldStruct.isPrimitive());
        assertFalse(integerFieldStruct.isStatic());

        final FieldStruct stringsFieldStruct = new FieldStruct(strings);
        assertTrue(stringsFieldStruct.isArray());
        assertTrue(stringsFieldStruct.getFlatType() == String.class);
        assertThat(stringsFieldStruct.getFirstParameterizedType(), nullValue());

        final FieldStruct twoDimensionShortsFieldStruct = new FieldStruct(twoDimensionShorts);
        assertTrue(twoDimensionShortsFieldStruct.isArray());

        final FieldStruct stringListFieldStruct = new FieldStruct(stringList);
        assertThat(stringListFieldStruct.getParameterizedTypes(), is(new Type[]{String.class}));
        assertThat(stringListFieldStruct.getFirstParameterizedType(), is((Type) String.class));

        final FieldStruct byteBooleanMapFieldStruct = new FieldStruct(byteBooleanMap);
        assertThat(byteBooleanMapFieldStruct.getParameterizedTypes(), is(new Type[]{Byte.class, Boolean.class}));
        assertThat(byteBooleanMapFieldStruct.getFirstParameterizedType(), is((Type) Byte.class));
    }

    @Test
    public void shouldFailToCreateFieldStructWhenFieldIsNull() {
        try {
            new FieldStruct(null);
            fail();
        } catch (final IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Argument is null"));
        }
    }
}