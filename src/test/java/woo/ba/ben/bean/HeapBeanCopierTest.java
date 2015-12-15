package woo.ba.ben.bean;

import org.junit.Before;
import org.junit.Test;
import woo.ba.ben.core.TestClassObj;
import woo.ba.ben.core.TestFieldObj;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
        final TestFieldObj copiedTestFieldObj = HeapBeanCopier.copyBean(testFieldObj);
        final TestClassObj copiedTestClassObj = HeapBeanCopier.copyBean(testClassObj);

        assertThat(copiedTestFieldObj, notNullValue());
        assertThat(copiedTestFieldObj.bigDecimal, is(new BigDecimal("12345.67893")));

        final List<String> copiedStringList = copiedTestClassObj.getStringList();
        assertThat(copiedStringList.size(), is(1));
        assertThat(copiedStringList.get(0), is("abc"));

        final Class integerClass = HeapBeanCopier.copyBean(Integer.class);
        assertTrue(integerClass.equals(Integer.class));
    }
}