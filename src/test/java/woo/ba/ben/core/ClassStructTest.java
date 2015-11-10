package woo.ba.ben.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ClassStructTest {
    @Test
    public void shouldCreateClassStructSuccessfully() {
        final ClassStruct classStruct = new ClassStruct(TestClassObj.class);
        assertThat(classStruct.getDeclaredField("stringFieldInClassObj").isPrimitive(), is(false));
        assertThat(classStruct.getParent(), nullValue());
        assertThat(classStruct.name, is("woo.ba.ben.core.TestClassObj"));
        assertEquals(classStruct.realClass, TestClassObj.class);
    }
}