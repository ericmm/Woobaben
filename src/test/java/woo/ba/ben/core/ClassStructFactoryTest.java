package woo.ba.ben.core;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;


public class ClassStructFactoryTest {
    public static final int LOOP_COUNTS = 50_000_000;

    @Test
    public void shouldCreateClassStruct() {
        final ClassStruct testFieldObjStruct = ClassStructFactory.get(TestFieldObj.class);
        assertThat(testFieldObjStruct, notNullValue());

        final ClassStruct testClassObjStruct = ClassStructFactory.get(TestClassObj.class);
        assertThat(testClassObjStruct, notNullValue());

        assertThat(testClassObjStruct.getField("testPrimitiveByte").isPrimitive(), is(true));
    }

    @Test
    public void shouldGetClassStruct() {
        final ClassStruct testFieldObjStruct = new ClassStruct(TestFieldObj.class);
        assertThat(testFieldObjStruct, notNullValue());

        final ClassStruct testClassObjStruct = ClassStructFactory.get(TestClassObj.class);
        assertThat(testClassObjStruct, notNullValue());

        assertThat(testClassObjStruct.getField("testPrimitiveByte").isPrimitive(), is(true));
    }

    @Test
    public void shouldGetClassStructForAnyNotNullClass() {
        assertThat(ClassStructFactory.get(Object.class), notNullValue());
    }

    @Test
    public void shouldGetNullWhenInputIsNull() {
        assertThat(ClassStructFactory.get(null), nullValue());
    }

    @Test
    public void shouldGetBlock() throws NoSuchFieldException, IllegalAccessException {
        final Unsafe unsafe = UnsafeFactory.UNSAFE;

        final ClassStruct classObjStruct = ClassStructFactory.get(TestClassObj.class);
        final ClassStruct emptyObjStruct = ClassStructFactory.get(TestEmptyObj.class);
        final ClassStruct fieldObjStruct = ClassStructFactory.get(TestFieldObj.class);

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


        FieldStruct fieldStruct1 = classObjStruct.getField("testPrimitiveByte");
        FieldStruct fieldStruct2 = emptyObjStruct.getField("testPrimitiveByte");
        FieldStruct fieldStruct3 = fieldObjStruct.getField("testPrimitiveByte");

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

    @Test
    public void shouldCopyBean() {
        final Unsafe unsafe = UnsafeFactory.UNSAFE;

        final ClassStruct classObjStruct = ClassStructFactory.get(TestClassObj.class);
        final TestClassObj testClassObj = new TestClassObj();
        testClassObj.setIntFieldInClassObj(100);
        testClassObj.setTestPrimitiveByte((byte) 1);

        final long fieldOffset = classObjStruct.getField("intFieldInClassObj").offset;
        final TestClassObj copiedTestClassObj = new TestClassObj();

        final int value = unsafe.getInt(testClassObj, fieldOffset);
        assertThat(value, is(100));

        unsafe.putInt(copiedTestClassObj, fieldOffset, value);
        final int gotValue = unsafe.getInt(copiedTestClassObj, fieldOffset);
        assertThat(copiedTestClassObj.getIntFieldInClassObj(), is(100));
        assertThat(gotValue, is(100));

        final FieldStruct[] instanceFields = classObjStruct.getInstanceFields();
        final long minInstanceOffset = instanceFields[0].offset;
        final long maxInstanceOffset = instanceFields[instanceFields.length - 1].offset;
        final long instanceFieldsSize = maxInstanceOffset - minInstanceOffset;
        final byte[] dataArray = new byte[(int) instanceFieldsSize];
        testClassObj.setIntFieldInClassObj(10);
        final int BYTES_OFFSET = unsafe.arrayBaseOffset(byte[].class);
        unsafe.copyMemory(testClassObj, minInstanceOffset, dataArray, BYTES_OFFSET, instanceFieldsSize);
        final int newOffset = (int) (fieldOffset - minInstanceOffset);

        for (int i = 0; i < dataArray.length; i++) {
            System.out.println("data[" + i + "]=" + dataArray[i]);
        }
        System.out.println("newOffset=" + newOffset + ", type is int, size is 4");

        System.out.print(unsafe.ARRAY_OBJECT_INDEX_SCALE + "," + unsafe.ARRAY_INT_INDEX_SCALE);
//        unsafe.putByte(copiedTestClassObj, minInstanceOffset, );

//        unsafe.copyMemory(testClassObj, minInstanceOffset, copiedTestClassObj, minInstanceOffset, 4);
//        unsafe.copyMemory(dataArray, BYTES_OFFSET, copiedTestClassObj, minInstanceOffset, instanceFieldsSize);
//        assertThat(copiedTestClassObj.getIntFieldInClassObj(), is(10));


//        unsafe.copyMemory(testClassObj, fieldOffset, copiedTestClassObj, fieldOffset, unsafe.ARRAY_INT_INDEX_SCALE);
//
//        assertThat(copiedTestClassObj.getIntFieldInClassObj(), is(200));

//        unsafe.copyMemory(testClassObj, minInstanceOffset, copiedTestClassObj, minInstanceOffset,  instanceFieldsSize);

//        assertThat(copiedTestClassObj.getIntFieldInClassObj(), is(100));
//        assertThat(copiedTestClassObj.getTestPrimitiveByte(), is((byte)1));

//        final long allocatedMemoryAddress = unsafe.allocateMemory(instanceFieldsSize);
//        unsafe.copyMemory();

//        unsafe.freeMemory(allocatedMemoryAddress);


    }

//    private Object[] holder = new Object[1];
//
//    private long getAddress(final Object obj) {
//        final Unsafe unsafe = UnsafeFactory.get();
//        holder[0] = obj;
//    }
}