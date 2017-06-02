package woo.ba.ben.core;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static woo.ba.ben.core.ClassStruct.classStruct;


public class ClassStructFactoryTest {
    public static final int LOOP_COUNTS = 50_000_000;

    @Test
    public void shouldCreateClassStruct() {
        final ClassStruct testFieldObjStruct = classStruct(TestFieldObj.class);
        assertThat(testFieldObjStruct, notNullValue());

        final ClassStruct testClassObjStruct = classStruct(TestClassObj.class);
        assertThat(testClassObjStruct, notNullValue());

        assertThat(testClassObjStruct.getField("testPrimitiveByte").isPrimitive(), is(true));
    }

    @Test
    public void shouldGetClassStruct() {
        final ClassStruct testFieldObjStruct = classStruct(TestFieldObj.class);
        assertThat(testFieldObjStruct, notNullValue());

        final ClassStruct testClassObjStruct = classStruct(TestClassObj.class);
        assertThat(testClassObjStruct, notNullValue());

        assertThat(testClassObjStruct.getField("testPrimitiveByte").isPrimitive(), is(true));
    }

    @Test
    public void shouldGetClassStructForAnyNotNullClass() {
        assertThat(classStruct(Object.class), notNullValue());
    }

    @Test
    public void shouldTestPerformance() throws NoSuchFieldException, IllegalAccessException {
        final Unsafe unsafe = UnsafeFactory.UNSAFE;

        final ClassStruct classObjStruct = classStruct(TestClassObj.class);
        final ClassStruct emptyObjStruct = classStruct(TestEmptyObj.class);
        final ClassStruct fieldObjStruct = classStruct(TestFieldObj.class);

        final TestFieldObj testFieldObj = new TestFieldObj();
        testFieldObj.setTestPrimitiveByte((byte) 1);

        final TestEmptyObj testEmptyObj = new TestEmptyObj();
        testEmptyObj.setTestPrimitiveByte((byte) 2);

        long start, end;
        byte value;

        final TestClassObj testClassObj = new TestClassObj();
        testClassObj.setTestPrimitiveByte((byte) 3);
        start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNTS; i++) {
            value = testFieldObj.getTestPrimitiveByte();
            value = testEmptyObj.getTestPrimitiveByte();
            value = testClassObj.getTestPrimitiveByte();

            testFieldObj.setTestPrimitiveByte((byte) 4);
            testEmptyObj.setTestPrimitiveByte((byte) 5);
            testClassObj.setTestPrimitiveByte((byte) 6);
        }
        end = System.currentTimeMillis();
        System.out.println("getter/setter takes " + (end - start));


        final FieldStruct testPrimitiveByte = classObjStruct.getField("testPrimitiveByte");
        final long offset = testPrimitiveByte.offset;
        start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNTS; i++) {
            value = unsafe.getByte(testFieldObj, offset);
            value = unsafe.getByte(testEmptyObj, offset);
            value = unsafe.getByte(testClassObj, offset);

            unsafe.putByte(testFieldObj, offset, (byte) 4);
            unsafe.putByte(testEmptyObj, offset, (byte) 5);
            unsafe.putByte(testClassObj, offset, (byte) 6);
        }
        end = System.currentTimeMillis();
        System.out.println("unsafe takes " + (end - start));


        start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNTS; i++) {
            value = testFieldObj.testPrimitiveByte;
            value = testEmptyObj.testPrimitiveByte;
            value = testClassObj.testPrimitiveByte;

            testFieldObj.testPrimitiveByte = (byte) 4;
            testEmptyObj.testPrimitiveByte = (byte) 5;
            testClassObj.testPrimitiveByte = (byte) 6;
        }
        end = System.currentTimeMillis();
        System.out.println("direct takes " + (end - start));


        final FieldStruct fieldStruct1 = classObjStruct.getField("testPrimitiveByte");
        final FieldStruct fieldStruct2 = emptyObjStruct.getField("testPrimitiveByte");
        final FieldStruct fieldStruct3 = fieldObjStruct.getField("testPrimitiveByte");

        start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNTS; i++) {
            //FIXME: two objects have same attributes
            value = FieldAccessor.getInstanceByte(testFieldObj, fieldStruct1);
            value = FieldAccessor.getInstanceByte(testEmptyObj, fieldStruct2);
            value = FieldAccessor.getInstanceByte(testClassObj, fieldStruct3);

            FieldAccessor.setInstanceByte(testFieldObj, fieldStruct1, (byte) 4);
            FieldAccessor.setInstanceByte(testEmptyObj, fieldStruct2, (byte) 5);
            FieldAccessor.setInstanceByte(testClassObj, fieldStruct3, (byte) 6);
        }
        end = System.currentTimeMillis();
        System.out.println("field accessor (fieldStruct) takes " + (end - start));


        final Field field1 = TestFieldObj.class.getField("testPrimitiveByte");
        final Field field2 = TestEmptyObj.class.getField("testPrimitiveByte");
        final Field field3 = TestClassObj.class.getField("testPrimitiveByte");

        start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNTS; i++) {
            value = field1.getByte(testFieldObj);
            value = field2.getByte(testEmptyObj);
            value = field3.getByte(testClassObj);

            field1.setByte(testFieldObj, (byte) 4);
            field2.setByte(testEmptyObj, (byte) 5);
            field3.setByte(testClassObj, (byte) 6);
        }
        end = System.currentTimeMillis();
        System.out.println("reflection takes " + (end - start));
    }
}