package woo.ba.ben.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ClassStructTest {
    @Test
    public void shouldCreateClassStructSuccessfully() {
        final ClassStruct parent = new ClassStruct(TestFieldObj.class, null);
        final ClassStruct classStruct = new ClassStruct(TestClassObj.class, parent);
        assertThat(classStruct.getDeclaredField("stringFieldInClassObj").isPrimitive(), is(false));
        assertThat(classStruct.getField("abc"), nullValue());
        assertThat(classStruct.getField("bigInteger"), notNullValue());
        assertThat(classStruct.parent, is(parent));
        assertThat(classStruct.getClassName(), is("woo.ba.ben.core.TestClassObj"));
        assertEquals(classStruct.realClass, TestClassObj.class);
    }

    @Test
    public void shouldEqualWhenRealClassIsSame() {
        final ClassStruct classStruct1 = new ClassStruct(TestFieldObj.class, null);
        final ClassStruct classStruct2 = new ClassStruct(TestFieldObj.class, null);

        assertTrue(classStruct1.equals(classStruct2));
    }

    @Test
    public void shouldGenerateHashcode() {
        final ClassStruct classStruct1 = new ClassStruct(TestFieldObj.class, null);
        assertThat(classStruct1.hashCode(), notNullValue());
    }

    @Test
    public void shouldGenerateToString() {
        final ClassStruct classStruct1 = new ClassStruct(TestFieldObj.class, null);
        assertThat(classStruct1.toString(), is("ClassStruct{realClass=class woo.ba.ben.core.TestFieldObj, parent=null}"));
    }

    @Test
    public void testPerformance(){

    }
}