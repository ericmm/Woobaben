package woo.ba.ben.bean;

import org.junit.Before;
import org.junit.Test;
import woo.ba.ben.core.TestClassObj;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class HeapBeanCopierTest {
    private long dateMillis = System.currentTimeMillis();
    private TestClassObj testClassObj;

    @Before
    public void setup() {
        testClassObj = new TestClassObj();
        testClassObj.dateArrayFieldInClassObj = new Date[1];
        testClassObj.dateArrayFieldInClassObj[0] = new Date(dateMillis);

        testClassObj.setStringFieldInClassObj("stringInClassObj");
        testClassObj.setBigInteger(new BigInteger("111"));
        final List<String> stringList = new ArrayList<>();
        stringList.add("abc");
        testClassObj.setStringList(stringList);
        testClassObj.setBooleanFieldInClassObj(true);
        testClassObj.setTestPrimitiveChar('A');
        testClassObj.setIntFieldInClassObj(54321);
    }

    @Test
    public void shouldCopyHeapBean() throws InstantiationException {
        final TestClassObj copiedObj = (TestClassObj) HeapBeanCopier.copyBean(testClassObj);

        assertThat(copiedObj, notNullValue());
        assertThat(copiedObj.dateArrayFieldInClassObj[0], is(new Date(dateMillis)));
    }
}