package woo.ba.ben.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FieldStructTest {

    private Unsafe unsafe = UnsafeFactory.get();

    public static class TestFieldObj {
        private static int testInt = 50;
        protected Integer integerValue = 28;
        public BigDecimal bigDecimal = new BigDecimal("12345.67893");
        BigInteger bigInteger = new BigInteger("123");

        private String[] strings = {"str1", "str2"};
        private long[] longs = {14, 16};
        private short[][] twoDimensionShorts = new short[2][3];
        private Double[][] twoDimensionDoubles = new Double[4][5];

        List<String> stringList;

        protected Map<Byte, Boolean> byteBooleanMap;

        private static short staticShortValue = 10;
        private static final double staticDoubleValue = 20.5d;
        private static long[] staticLongArray = {30l, 40l};
    }

    @Test
    public void shouldCreateFieldStruct() throws NoSuchFieldException {
        final Field testInt = TestFieldObj.class.getDeclaredField("testInt");
        final Field integerValue = TestFieldObj.class.getDeclaredField("integerValue");
        final Field strings = TestFieldObj.class.getDeclaredField("strings");
        final Field twoDimensionShorts = TestFieldObj.class.getDeclaredField("twoDimensionShorts");
        final Field stringList = TestFieldObj.class.getDeclaredField("stringList");
        final Field byteBooleanMap = TestFieldObj.class.getDeclaredField("byteBooleanMap");

        final FieldStruct fieldStruct = new FieldStruct(testInt, unsafe);

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

        final FieldStruct integerFieldStruct = new FieldStruct(integerValue, unsafe);
        assertFalse(integerFieldStruct.isPrimitive());
        assertFalse(integerFieldStruct.isStatic());

        final FieldStruct stringsFieldStruct = new FieldStruct(strings, unsafe);
        assertTrue(stringsFieldStruct.isArray());
        assertTrue(stringsFieldStruct.getFlatType() == String.class);
        assertThat(stringsFieldStruct.getFirstParameterizedType(), nullValue());

        final FieldStruct twoDimensionShortsFieldStruct = new FieldStruct(twoDimensionShorts, unsafe);
        assertTrue(twoDimensionShortsFieldStruct.isArray());

        final FieldStruct stringListFieldStruct = new FieldStruct(stringList, unsafe);
        assertThat(stringListFieldStruct.getParameterizedTypes(), is(new Type[]{String.class}));
        assertThat(stringListFieldStruct.getFirstParameterizedType(), is((Type) String.class));

        final FieldStruct byteBooleanMapFieldStruct = new FieldStruct(byteBooleanMap, unsafe);
        assertThat(byteBooleanMapFieldStruct.getParameterizedTypes(), is(new Type[]{Byte.class, Boolean.class}));
        assertThat(byteBooleanMapFieldStruct.getFirstParameterizedType(), is((Type) Byte.class));
    }

    @Test
    public void shouldFailToCreateFieldStructWhenFieldIsNull() {
        try {
            new FieldStruct(null, unsafe);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Argument is null"));
        }
    }
}