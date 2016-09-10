package woo.ba.ben.core;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static woo.ba.ben.core.IHeapDataHandler.arrayEquals;
import static woo.ba.ben.core.UTF8Utils.decode;
import static woo.ba.ben.core.UTF8Utils.encode;


public class UTF8UtilsTest {
    public static final int LOOP_CNT = 50_000_000;

    @Test
    public void shouldEncodeAndDecodeCorrectly() throws UnsupportedEncodingException, CharacterCodingException {
        String a = "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd"
                + "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd"
                + "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd"
                + "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd"
                + "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd"
                + "\u006f\u0073\u0063\u0068\u0069\u006e\u0061\u5f00\u6e90\u4e2d\u56fd";

        char[] charArr = a.toCharArray();
        final byte[] bytes = testConvertString(a);
        final byte[] bytes1 = testConvert(charArr);
        assertTrue(arrayEquals(bytes, bytes1));

        final char[] chars = decodeString(bytes);
        final char[] chars1 = decodeMy(bytes);

        assertArrayEquals(chars, chars1);
    }

    private char[] decodeMy(byte[] bytes) throws CharacterCodingException {
        char[] chars = new char[66];
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_CNT; i++) {
            decode(bytes, 0, bytes.length, chars, 0, chars.length);
        }
        long end = System.currentTimeMillis();
        System.out.println("it took " + (end - start) + " to run custom decode()");

        return chars;
    }

    private char[] decodeString(byte[] bytes) throws UnsupportedEncodingException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_CNT; i++) {
            new String(bytes, "UTF-8");
        }
        long end = System.currentTimeMillis();
        System.out.println("it took " + (end - start) + " to run new String(bytes, UTF-8)");

        final String str = new String(bytes, "UTF-8");
        return UTF8Utils.getCharArrayDirectly(str);
    }

    private byte[] testConvertString(String str) throws UnsupportedEncodingException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_CNT; i++) {
            str.getBytes("UTF-8");
        }
        long end = System.currentTimeMillis();
        System.out.println("it took " + (end - start) + " to run String.getBytes(UTF-8)");

        return str.getBytes("UTF-8");
    }


    private byte[] testConvert(char[] charArr) throws UnsupportedEncodingException {
        byte[] output = new byte[114];
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_CNT; i++) {
            encode(charArr, 0, charArr.length, output, 0, output.length);
        }
        long end = System.currentTimeMillis();
        System.out.println("it took " + (end - start) + " to run custom encode()");

        return output;
    }
}