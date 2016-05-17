package woo.ba.ben.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ClassStructTest {
    @Test
    public void shouldCreateClassStructSuccessfully() {
        final ClassStruct parent = new ClassStruct(TestFieldObj.class);
        final ClassStruct classStruct = new ClassStruct(TestClassObj.class);
        assertThat(classStruct.getInstanceField("abc"), nullValue());
        assertThat(classStruct.getInstanceField("bigInteger"), notNullValue());
        assertThat(classStruct.className, is("woo.ba.ben.core.TestClassObj"));
    }

    @Test
    public void shouldEqualWhenRealClassIsSame() {
        final ClassStruct classStruct1 = new ClassStruct(TestFieldObj.class);
        final ClassStruct classStruct2 = new ClassStruct(TestFieldObj.class);

        assertTrue(classStruct1.equals(classStruct2));
    }

    @Test
    public void shouldGenerateHashcode() {
        final ClassStruct classStruct1 = new ClassStruct(TestFieldObj.class);
        assertThat(classStruct1.hashCode(), notNullValue());
    }

    @Test
    public void shouldGenerateToString() {
        final ClassStruct classStruct1 = new ClassStruct(TestFieldObj.class);
        assertThat(classStruct1.toString(), is("ClassStruct{className=woo.ba.ben.core.TestFieldObj}"));
    }

    @Test
    public void testPerformance(){

    }
}