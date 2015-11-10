package woo.ba.ben.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ClassStructTest {
    @Test
    public void shouldCreateClassStructSuccessfully() {
        final ClassStruct parent = new ClassStruct(TestFieldObj.class, null);
        final ClassStruct classStruct = new ClassStruct(TestClassObj.class, parent);
        assertThat(classStruct.getDeclaredField("stringFieldInClassObj").isPrimitive(), is(false));
        assertThat(classStruct.parent, is(parent));
        assertThat(classStruct.getClassName(), is("woo.ba.ben.core.TestClassObj"));
        assertEquals(classStruct.realClass, TestClassObj.class);
    }
}