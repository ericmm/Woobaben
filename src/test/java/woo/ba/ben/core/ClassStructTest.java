package woo.ba.ben.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public class ClassStructTest {
    @Test
    public void shouldCreateClassStructSuccessfully() {
        final ClassStruct classStruct = new ClassStruct(TestClassObj.class);
        assertThat(classStruct.getField("abc"), nullValue());
        assertThat(classStruct.getField("bigInteger"), notNullValue());
        assertThat(classStruct.realClass, equalTo(TestClassObj.class));
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
        assertThat(classStruct1.toString(), is("ClassStruct{realClass=class woo.ba.ben.core.TestFieldObj}"));
    }

    @Test
    public void shouldCopyMemory(){
        final TestClassObj obj1 = new TestClassObj();
        obj1.setIntFieldInClassObj(123456);

        final ClassStruct struct = new ClassStruct(TestClassObj.class);
        final long startOffset = struct.getStartOffset();
        final long blockSize = struct.getInstanceBlockSize();
        final byte[] buffer = new byte[(int) blockSize];

        final FieldStruct fieldStruct = struct.getField("intFieldInClassObj");
        final int fieldStart = (int) (fieldStruct.offset - startOffset);
        UNSAFE.copyMemory(obj1, startOffset, buffer, ARRAY_BYTE_BASE_OFFSET, blockSize);

        final IHeapDataReader nativeOrderDataReader = DataReaderFactory.getNativeOrderHeapDataReader();
        final int readInt = nativeOrderDataReader.readInt(buffer, fieldStart);
        assertThat(readInt, equalTo(123456));

//        final TestClassObj obj2 = new TestClassObj();
//        UNSAFE.copyMemory(buffer, ARRAY_BYTE_BASE_OFFSET, obj2, fieldStart, blockSize);

        final int[] intArray = new int[] {12, 34, 56};
        final Class arrayClass = intArray.getClass();
        final ClassStruct intArrayStruct = new ClassStruct(arrayClass);
        final long intArrayStartOffset = intArrayStruct.getStartOffset();
        final long intArrayBlockSize = intArrayStruct.getArrayBlockSize(intArray.length);
        final byte[] arrayBuffer = new byte[(int) intArrayBlockSize];

        UNSAFE.copyMemory(intArray, intArrayStartOffset, arrayBuffer, ARRAY_BYTE_BASE_OFFSET, intArrayBlockSize);

        final int element1 = nativeOrderDataReader.readInt(arrayBuffer, 1 * UNSAFE.arrayIndexScale(arrayClass));
        assertThat(element1, is(34));
    }
}