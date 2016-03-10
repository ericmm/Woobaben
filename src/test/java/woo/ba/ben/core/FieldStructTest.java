package woo.ba.ben.core;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import static org.hamcrest.CoreMatchers.notNullValue;
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

    @Test
    public void shouldGenerateToString() throws NoSuchFieldException {
        final Field testInt = TestFieldObj.class.getDeclaredField("testInt");
        final FieldStruct fieldStruct1 = new FieldStruct(testInt);
        assertThat(fieldStruct1.toString(), is("FieldStruct{name='testInt', type=int, offset=120, realField=private static int woo.ba.ben.core.TestFieldObj.testInt}"));
    }

    @Test
    public void shouldFailToCreateFieldStructWhenFieldIsNull() {
        try {
            new FieldStruct(null);
            fail();
        } catch (final NullPointerException e) {
            assertThat(e.getMessage(), is("The 1 parameter is null!"));
        }
    }
}