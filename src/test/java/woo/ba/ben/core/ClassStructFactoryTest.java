package woo.ba.ben.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


public class ClassStructFactoryTest {
    private ClassStructFactory factory = ClassStructFactory.getInstance();

    @Test
    public void shouldCreateClassStruct() {
        final ClassStruct testFieldObjStruct = factory.get(TestFieldObj.class);
        assertThat(testFieldObjStruct, notNullValue());

        final ClassStruct testClassObjStruct = factory.get(TestClassObj.class);
        assertThat(testClassObjStruct, notNullValue());

        assertThat(testClassObjStruct.getField("testInt").isPrimitive(), is(true));
    }

    @Test
    public void shouldGetClassStruct() {
        final ClassStruct testFieldObjStruct = new ClassStruct(TestFieldObj.class, null);
        assertThat(testFieldObjStruct, notNullValue());

        final ClassStruct testClassObjStruct = factory.get(TestClassObj.class);
        assertThat(testClassObjStruct, notNullValue());

        assertThat(testClassObjStruct.getField("testInt").isPrimitive(), is(true));
    }

    @Test
    public void shouldGetInstance() {
        factory = ClassStructFactory.getInstance();

        assertThat(factory, notNullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenNull() {
        factory.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenObjectClass() {
        factory.get(Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenAnnotationClass() {
        factory.get(Test.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionWhenInterface() {
        factory.get(Runnable.class);
    }
}