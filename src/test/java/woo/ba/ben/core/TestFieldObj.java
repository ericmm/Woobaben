package woo.ba.ben.core;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class TestFieldObj {
    private static int testInt = 50;
    protected Integer integerValue = 28;
    public BigDecimal bigDecimal = new BigDecimal("12345.67893");
    BigInteger bigInteger = new BigInteger("123");

    private String[] strings = {"str1", "str2"};
    private long[] longs = {14, 16};
    private short[][] twoDimensionShorts = new short[2][3];
    private Double[][] twoDimensionDoubles = new Double[4][5];

    List<String> stringList;

    protected Map<Byte, Boolean> byteBooleanMap;

    private static short staticShortValue = 10;
    private static final double staticDoubleValue = 20.5d;
    private static long[] staticLongArray = {30l, 40l};
}
