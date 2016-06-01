package woo.ba.ben;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.UnsupportedEncodingException;

import static woo.ba.ben.core.UTF8Utils.encode;


public class AppTest extends TestCase {

    public static final int INT = 50_000_000;

    public AppTest(final String testName) {
        super(testName);
    }


    public static Test suite() {
        return new TestSuite(AppTest.class);
    }


    public void testApp() throws UnsupportedEncodingException {
        String a = "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd"
                + "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd"
                + "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd"
                + "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd"
                + "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd"
                + "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd";

        char[] charArr = a.toCharArray();
        testConvertString(a);
        testConvertMy(charArr);
    }

    private void testConvertString(String str) throws UnsupportedEncodingException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < INT; i++) {
            str.getBytes("UTF-8");
        }
        long end = System.currentTimeMillis();
        System.out.println("it took " + (end - start) + " to run String.getBytes()");
    }


    private void testConvertMy(char[] charArr) throws UnsupportedEncodingException {
        byte[] output = new byte[114];
        long start = System.currentTimeMillis();
        for (int i = 0; i < INT; i++) {
            encode(charArr, 0, charArr.length, output, 0);
        }
        long end = System.currentTimeMillis();
        System.out.println("it took " + (end - start) + " to run charArr");
    }
}
