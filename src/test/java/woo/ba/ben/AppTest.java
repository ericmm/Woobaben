package woo.ba.ben;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CoderResult;


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

        System.out.println(a);
        char[] charArr = a.toCharArray();
//
//        int c;
//        for (int i = 0; i < charArr.length; i++) {
//            c = charArr[i];
//            System.out.println(c);
//        }
//
//        byte[] dest = new byte[30];
//
//        assertThat(encode(charArr, 0, charArr.length, dest, 0), is(CoderResult.UNDERFLOW));
//        String e = new String(dest, Charset.forName("UTF-8"));
//        System.out.println(e);
//
//        final byte[] bytes = encode(charArr, 0, charArr.length);
//        String converted = new String(bytes, Charset.forName("UTF-8"));
//        System.out.println(converted);
//
//        assertTrue(true);

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

    CoderResult encode(char[] source, int offset, int count, byte[] dest, int destOffset) {
        if (offset < 0 || count < 0 || destOffset < 0) {
            throw new IllegalArgumentException();
        }

        final int sourceLength = source.length - offset;
        int sourceRemaining = count > sourceLength ? sourceLength : count;
        int remainingCapacity = dest.length - destOffset;
        if (remainingCapacity < sourceRemaining) {
            return CoderResult.OVERFLOW;
        }

//        // handle ascii encoded strings in an optimised loop
//        int minByte = Math.min(count - offset, dest.length - destOffset);
//        while (destOffset < minByte && source[offset] < 0x80) {
//            dest[destOffset++] = (byte) source[offset++];
//        }

        int charValueInInt;
        for (int i = offset; i < count; i++) {
            charValueInInt = source[i];

            if (charValueInInt < 0x80) {
                //check remaining capacity
                remainingCapacity--;
                sourceRemaining--;
                if (remainingCapacity < sourceRemaining) {
                    return CoderResult.OVERFLOW;
                }

                // 1 bit
                dest[destOffset++] = (byte) charValueInInt;
            } else if (charValueInInt < 0x800) {
                //check remaining capacity
                remainingCapacity -= 2;
                sourceRemaining--;
                if (remainingCapacity < sourceRemaining) {
                    return CoderResult.OVERFLOW;
                }

                // 2 bits
                dest[destOffset++] = (byte) (0xC0 | charValueInInt >> 6);
                dest[destOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else if (charValueInInt < 0x10000) {
                //check remaining capacity
                remainingCapacity -= 3;
                sourceRemaining--;
                if (remainingCapacity < sourceRemaining) {
                    return CoderResult.OVERFLOW;
                }

                // 3 bits
                dest[destOffset++] = (byte) (0xE0 | charValueInInt >> 12);
                dest[destOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                dest[destOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else if (charValueInInt < 0x200000) {
                //check remaining capacity
                remainingCapacity -= 4;
                sourceRemaining--;
                if (remainingCapacity < sourceRemaining) {
                    return CoderResult.OVERFLOW;
                }

                // 4 bits
                dest[destOffset++] = (byte) (0xF0 | charValueInInt >> 18);
                dest[destOffset++] = (byte) (0x80 | charValueInInt >> 12 & 0x3F);
                dest[destOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
                dest[destOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
            } else {
                // error, clean-up byte array;
                return CoderResult.OVERFLOW;
            }
        }
        return CoderResult.UNDERFLOW;
    }

//    byte[] encode(char[] source, int offset, int count) {
//        if (offset < 0 || count < 0 || count > source.length - offset) {
//            throw new IndexOutOfBoundsException();
//        }
//
//        int targetCapacity = 0;
//        int charValueInInt;
//        for (int i = offset; i < count; i++) {
//            charValueInInt = source[i];
//            if (charValueInInt < 0x80) {
//                targetCapacity++;
//            } else if (charValueInInt < 0x800) {
//                targetCapacity += 2;
//            } else if (charValueInInt < 0x10000) {
//                targetCapacity += 3;
//            } else if (charValueInInt < 0x200000) {
//                targetCapacity += 4;
//            } else {
//                throw new IllegalArgumentException("Containing unrecognised character:" + source[i]);
//            }
//        }
//        byte[] dest = new byte[targetCapacity];
//        int destOffset = 0;
//        for (int i = offset; i < count; i++) {
//            charValueInInt = source[i];
//
//            if (charValueInInt < 0x80) {
//                // 1 bit
//                dest[destOffset++] = (byte) charValueInInt;
//            } else if (charValueInInt < 0x800) {
//                // 2 bits
//                dest[destOffset++] = (byte) (0xC0 | charValueInInt >> 6);
//                dest[destOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
//            } else if (charValueInInt < 0x10000) {
//                // 3 bits
//                dest[destOffset++] = (byte) (0xE0 | charValueInInt >> 12);
//                dest[destOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
//                dest[destOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
//            } else if (charValueInInt < 0x200000) {
//                // 4 bits
//                dest[destOffset++] = (byte) (0xF0 | charValueInInt >> 18);
//                dest[destOffset++] = (byte) (0x80 | charValueInInt >> 12 & 0x3F);
//                dest[destOffset++] = (byte) (0x80 | charValueInInt >> 6 & 0x3F);
//                dest[destOffset++] = (byte) (0x80 | charValueInInt & 0x3F);
//            } else {
//                // error
//                throw new IllegalArgumentException("Containing unrecognised character:" + source[i]);
//            }
//        }
//        return dest;
//    }
}
