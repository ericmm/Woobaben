package woo.ba.ben;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static woo.ba.ben.core.UTF8Utils.encode;


public class AppTest {

    public static final int LOOP_CNT = 50_000_000;

    @Test
    public void testApp() throws UnsupportedEncodingException {
        String a = "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd"
                + "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd"
                + "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd"
                + "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd"
                + "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd"
                + "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd";

        char[] charArr = a.toCharArray();
        testConvertString(a);
        testConvert(charArr);
    }

    private void testConvertString(String str) throws UnsupportedEncodingException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_CNT; i++) {
            str.getBytes("UTF-8");
        }
        long end = System.currentTimeMillis();
        System.out.println("it took " + (end - start) + " to run String.getBytes()");
    }


    private void testConvert(char[] charArr) throws UnsupportedEncodingException {
        byte[] output = new byte[114];
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_CNT; i++) {
            encode(charArr, 0, charArr.length, output, 0, output.length);
        }
        long end = System.currentTimeMillis();
        System.out.println("it took " + (end - start) + " to run charArr");
    }



}
