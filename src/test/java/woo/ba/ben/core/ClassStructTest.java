package woo.ba.ben.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;

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
        TestClassObj obj1 = new TestClassObj();
        obj1.setIntFieldInClassObj(123456);

        ClassStruct struct = new ClassStruct(TestClassObj.class);
        final long startOffset = struct.getInstanceStartOffset();
        final long blockSize = struct.getInstanceBlockSize();
        byte[] obj2 = new byte[(int) blockSize];

        final FieldStruct fieldStruct = struct.getField("intFieldInClassObj");
        int fieldStart = (int) (fieldStruct.offset - startOffset);
        UnsafeFactory.UNSAFE.copyMemory(obj1, startOffset, obj2, ARRAY_BYTE_BASE_OFFSET, blockSize);

        final int readInt = DataReaderFactory.getNativeOrderDataReader().readInt(obj2, fieldStart);

        assertThat(readInt, equalTo(123456));
    }
}