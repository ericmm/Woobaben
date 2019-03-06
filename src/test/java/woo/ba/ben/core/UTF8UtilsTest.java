package woo.ba.ben.core;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CoderResult;
import java.text.NumberFormat;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static woo.ba.ben.core.IHeapDataHandler.arrayEquals;
import static woo.ba.ben.core.UTF8Utils.decode;
import static woo.ba.ben.core.UTF8Utils.decode2;
import static woo.ba.ben.core.UTF8Utils.encode;


public class UTF8UtilsTest {
    public static final int LOOP_CNT = 50_000_000;

    private long time1, time2, time3, time4, time5;

    private static int log2int(int bits) {
        if (bits == 0) {
            return 0; // or throw exception
        }
        return 31 - Integer.numberOfLeadingZeros(bits);
    }

    private static int log2long(long bits) {
        if (bits == 0) {
            return 0; // or throw exception
        }
        return 63 - Long.numberOfLeadingZeros(bits);
    }

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
        System.out.println("========> Custom encoding is " + calcPercentage(time1, time2) + " faster than JDK. \n");

        final char[] chars = decodeString(bytes);
        final char[] chars1 = decodeMy(bytes);
        final char[] chars2 = decodeMy2(bytes);
        assertArrayEquals(chars, chars1);
        assertArrayEquals(chars1, chars2);
        System.out.println("========> Custom decoding is " + calcPercentage(time3, time4) + " faster than JDK. ");
        System.out.println("========> Custom decoding 2 is " + calcPercentage(time3, time5) + " faster than JDK. ");
        System.out.println("========> Custom decoding 2 is " + calcPercentage(time4, time5) + " faster than decoding 1. ");
    }

    @Test
    public void shouldEncodeCorrectly() {
        Charset utf8 = Charset.forName("UTF-8");
        for (int i = Character.MIN_CODE_POINT; i < Character.MAX_CODE_POINT; i++) {
            char[] chars = Character.toChars(i);
            byte[] array1 = new String(chars).getBytes(utf8);
            System.out.println("code point=" + i + ", array1.length=" + array1.length + ", chars.length=" + chars.length);
            byte[] array2 = new byte[array1.length];

            CoderResult coderResult = UTF8Utils.encode(chars, 0, chars.length, array2, 0, array2.length);
            if (i >= Character.MIN_SURROGATE && i < Character.MAX_SURROGATE + 1) {
                assertThat(coderResult.isUnmappable(), is(true));
            } else {
                assertThat(coderResult, is(CoderResult.UNDERFLOW));
                assertArrayEquals(array1, array2);
            }
        }
    }

    private String calcPercentage(long d1, long d2) {
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(0);
        return percentFormat.format(1.0 * (d1 - d2) / d1);
    }

    private char[] decodeMy(byte[] bytes) throws CharacterCodingException {
        char[] chars = new char[66];
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_CNT; i++) {
            decode(bytes, 0, bytes.length, chars, 0, chars.length);
        }
        long end = System.currentTimeMillis();
        time4 = end - start;
        System.out.println("it took " + time4 + " to run custom decode()");

        return chars;
    }

    private char[] decodeMy2(byte[] bytes) throws CharacterCodingException {
        char[] chars = new char[66];
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_CNT; i++) {
            decode2(bytes, 0, bytes.length, chars, 0, chars.length);
        }
        long end = System.currentTimeMillis();
        time5 = end - start;
        System.out.println("it took " + time5 + " to run custom decode2()");

        return chars;
    }

    private char[] decodeString(byte[] bytes) throws UnsupportedEncodingException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_CNT; i++) {
            new String(bytes, "UTF-8");
        }
        long end = System.currentTimeMillis();
        time3 = end - start;
        System.out.println("it took " + time3 + " to run new String(bytes, UTF-8)");

        final String str = new String(bytes, "UTF-8");
        return UTF8Utils.getCharArrayDirectly(str);
    }

    private byte[] testConvertString(String str) throws UnsupportedEncodingException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_CNT; i++) {
            str.getBytes("UTF-8");
        }
        long end = System.currentTimeMillis();
        time1 = end - start;
        System.out.println("it took " + time1 + " to run String.getBytes(UTF-8)");

        return str.getBytes("UTF-8");
    }

    private byte[] testConvert(char[] charArr) throws UnsupportedEncodingException {
        byte[] output = new byte[114];
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP_CNT; i++) {
            encode(charArr, 0, charArr.length, output, 0, output.length);
        }
        long end = System.currentTimeMillis();
        time2 = end - start;
        System.out.println("it took " + time2 + " to run custom encode()");

        return output;
    }

    @Test
    public void shouldEqualLog2() {

        int sum = 0, x = 0;
        long time = System.nanoTime();
        do sum += log2int(x); while (++x != 0);
        time = System.nanoTime() - time;
        System.out.println("time1=" + time / 1000000L / 1000.0 + "s -> " + sum);

        sum = 0;
        x = 0;
        time = System.nanoTime();
        do sum += log2long(x); while (++x != 0);
        time = System.nanoTime() - time;
        System.out.println("time2=" + time / 1000000L / 1000.0 + "s -> " + sum);
    }
}