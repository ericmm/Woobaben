package woo.ba.ben.core;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestFieldObj {
    private static final double staticDoubleValue = 20.5d;
    private static int testInt = 50;
    private static short staticShortValue = 10;
    private static long[] staticLongArray = {30l, 40l};

    public BigDecimal bigDecimal = new BigDecimal("12345.67893");

    protected Integer integerValue = 28;
    protected Map<Byte, Boolean> byteBooleanMap;

    BigInteger bigInteger = new BigInteger("123");
    List<String> stringList = new ArrayList<>();

    private String[] strings = {"str1", "str2"};
    private long[] longs = {14, 16};
    private short[][] twoDimensionShorts = new short[2][3];
    private Double[][] twoDimensionDoubles = new Double[4][5];
    private boolean testPrimitiveBoolean = false;
    public byte testPrimitiveByte = 64;
    private float testPrimitiveFloat = 64.1234f;

    public static double getStaticDoubleValue() {
        return staticDoubleValue;
    }

    public static int getTestInt() {
        return testInt;
    }

    public static void setTestInt(final int testInt) {
        TestFieldObj.testInt = testInt;
    }

    public static short getStaticShortValue() {
        return staticShortValue;
    }

    public static void setStaticShortValue(final short staticShortValue) {
        TestFieldObj.staticShortValue = staticShortValue;
    }

    public static long[] getStaticLongArray() {
        return staticLongArray;
    }

    public static void setStaticLongArray(final long[] staticLongArray) {
        TestFieldObj.staticLongArray = staticLongArray;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(final BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public Integer getIntegerValue() {
        return integerValue;
    }

    public void setIntegerValue(final Integer integerValue) {
        this.integerValue = integerValue;
    }

    public Map<Byte, Boolean> getByteBooleanMap() {
        return byteBooleanMap;
    }

    public void setByteBooleanMap(final Map<Byte, Boolean> byteBooleanMap) {
        this.byteBooleanMap = byteBooleanMap;
    }

    public BigInteger getBigInteger() {
        return bigInteger;
    }

    public void setBigInteger(final BigInteger bigInteger) {
        this.bigInteger = bigInteger;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(final List<String> stringList) {
        this.stringList = stringList;
    }

    public String[] getStrings() {
        return strings;
    }

    public void setStrings(final String[] strings) {
        this.strings = strings;
    }

    public long[] getLongs() {
        return longs;
    }

    public void setLongs(final long[] longs) {
        this.longs = longs;
    }

    public short[][] getTwoDimensionShorts() {
        return twoDimensionShorts;
    }

    public void setTwoDimensionShorts(final short[][] twoDimensionShorts) {
        this.twoDimensionShorts = twoDimensionShorts;
    }

    public Double[][] getTwoDimensionDoubles() {
        return twoDimensionDoubles;
    }

    public void setTwoDimensionDoubles(final Double[][] twoDimensionDoubles) {
        this.twoDimensionDoubles = twoDimensionDoubles;
    }

    public boolean isTestPrimitiveBoolean() {
        return testPrimitiveBoolean;
    }

    public void setTestPrimitiveBoolean(final boolean testPrimitiveBoolean) {
        this.testPrimitiveBoolean = testPrimitiveBoolean;
    }

    public byte getTestPrimitiveByte() {
        return testPrimitiveByte;
    }

    public void setTestPrimitiveByte(final byte testPrimitiveByte) {
        this.testPrimitiveByte = testPrimitiveByte;
    }

    public float getTestPrimitiveFloat() {
        return testPrimitiveFloat;
    }

    public void setTestPrimitiveFloat(final float testPrimitiveFloat) {
        this.testPrimitiveFloat = testPrimitiveFloat;
    }
}
