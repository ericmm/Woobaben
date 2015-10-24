package woo.ba.ben;

import com.alibaba.fastjson.JSON;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class AppTest extends TestCase {

    public AppTest(final String testName) {
        super(testName);
    }


    public static Test suite() {
        return new TestSuite(AppTest.class);
    }


    public void testApp() {
        assertTrue(true);

        final Person person = new Person();
        person.setAge(new Short((short)30));
        person.setName("John");
        person.setId(120);

        final String json = JSON.toJSONString(person);
        System.out.println(json);
    }
}
