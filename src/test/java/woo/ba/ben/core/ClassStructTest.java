package woo.ba.ben.core;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;
import static woo.ba.ben.core.ClassStruct.*;
import static woo.ba.ben.core.UnsafeFactory.UNSAFE;

public class ClassStructTest {
    @Test
    public void shouldCreateClassStructSuccessfully() {
        final ClassStruct classStruct = classStruct(TestClassObj.class);
        assertThat(classStruct.getField("abc"), nullValue());
        assertThat(classStruct.getField("bigInteger"), notNullValue());
        assertThat(classStruct.realClass, equalTo(TestClassObj.class));
    }

    @Test
    public void shouldEqualWhenRealClassIsSame() {
        final ClassStruct classStruct1 = classStruct(TestFieldObj.class);
        final ClassStruct classStruct2 = classStruct(TestFieldObj.class);

        assertTrue(classStruct1.equals(classStruct2));
    }

    @Test
    public void shouldGenerateHashcode() {
        final ClassStruct classStruct1 = classStruct(TestFieldObj.class);
        assertThat(classStruct1.hashCode(), notNullValue());
    }

    @Test
    public void shouldGenerateToString() {
        final ClassStruct classStruct1 = classStruct(TestFieldObj.class);
        assertThat(classStruct1.toString(), is("ClassStruct{realClass=class woo.ba.ben.core.TestFieldObj}"));
    }

    @Ignore
    @Test
    public void shouldCopyMemory(){
        final TestClassObj obj1 = new TestClassObj();
        obj1.setIntFieldInClassObj(123456);

//        final ClassStruct struct = classStruct(TestClassObj.class);
//        final long startOffset = struct.startOffset;
//        final long blockSize = struct.instanceBlockSize;
//        final byte[] buffer = new byte[(int) blockSize];
//
//        final FieldStruct fieldStruct = struct.getField("intFieldInClassObj");
//        final int fieldStart = (int) (fieldStruct.offset - startOffset);
//        UNSAFE.copyMemory(obj1, startOffset, buffer, ARRAY_BYTE_BASE_OFFSET, blockSize);
//
        final IHeapDataHandler nativeOrderDataReader = DataHandlerFactory.nativeOrderHeapDataHandler();
//        final int readInt = nativeOrderDataReader.readInt(buffer, fieldStart);
//        assertThat(readInt, equalTo(123456));

//        final TestClassObj obj2 = new TestClassObj();
//        UNSAFE.copyMemory(buffer, ARRAY_BYTE_BASE_OFFSET, obj2, fieldStart, blockSize);

        final int[] intArray = new int[] {12, 34, 56};
        final Class arrayClass = intArray.getClass();
        final long intArrayStartOffset = getArrayStartOffset(int[].class);
        final long intArrayBlockSize = getArrayBlockSize(int[].class, intArray.length);
        final byte[] arrayBuffer = new byte[(int) intArrayBlockSize];

        UNSAFE.copyMemory(intArray, intArrayStartOffset, arrayBuffer, ARRAY_BYTE_BASE_OFFSET, intArrayBlockSize);

        final int element1 = nativeOrderDataReader.readInt(arrayBuffer, 1 * UNSAFE.arrayIndexScale(arrayClass));
        assertThat(element1, is(34));
    }
}