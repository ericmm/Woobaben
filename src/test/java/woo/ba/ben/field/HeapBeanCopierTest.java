package woo.ba.ben.field;

import org.junit.Before;
import org.junit.Test;
import woo.ba.ben.core.TestClassObj;
import woo.ba.ben.core.TestFieldObj;
import woo.ba.ben.obj.HeapObjectCopier;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class HeapBeanCopierTest {
    private TestClassObj testClassObj;
    private TestFieldObj testFieldObj;

    @Before
    public void setup() {
        testClassObj = new TestClassObj();
        testClassObj.dateArrayFieldInClassObj = new Date[1];

        testClassObj.setStringFieldInClassObj("stringInClassObj");
        testClassObj.setBigInteger(new BigInteger("111"));
        final List<String> stringList = new ArrayList<>();
        stringList.add("abc");
        testClassObj.setStringList(stringList);
        testClassObj.setBooleanFieldInClassObj(true);
        testClassObj.setTestPrimitiveChar('A');
        testClassObj.setIntFieldInClassObj(54321);

        testFieldObj = new TestFieldObj();


    }

    @Test
    public void shouldCopyHeapBean() throws InstantiationException {
        final TestFieldObj copiedTestFieldObj = HeapObjectCopier.deepCopy(testFieldObj);
        final TestClassObj copiedTestClassObj = HeapObjectCopier.deepCopy(testClassObj);

        assertThat(copiedTestFieldObj, notNullValue());
        assertThat(copiedTestFieldObj.bigDecimal, is(new BigDecimal("12345.67893")));

        final List<String> copiedStringList = copiedTestClassObj.getStringList();
        assertThat(copiedStringList.size(), is(1));
        assertThat(copiedStringList.get(0), is("abc"));

        final Class integerClass = HeapObjectCopier.deepCopy(Integer.class);
        assertTrue(integerClass.equals(Integer.class));

        final int[] intArray = new int[2];
        intArray[0] = 1;
        intArray[1] = 2;

        final int[] copiedIntArray = HeapObjectCopier.deepCopy(intArray);
        assertArrayEquals(intArray, copiedIntArray);

        assertTrue(HeapObjectCopier.deepCopy(null) == null);
        assertTrue(HeapObjectCopier.deepCopy(1) == 1);
    }
}