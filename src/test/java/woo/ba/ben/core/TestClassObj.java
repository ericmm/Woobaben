package woo.ba.ben.core;


import java.util.Date;

public class TestClassObj extends TestEmptyObj {
    private String stringFieldInClassObj;
    protected int intFieldInClassObj;
    boolean booleanFieldInClassObj;

    public Date[] dateArrayFieldInClassObj;

    private char testPrimitiveChar = 'a';
    private float testPrimitiveFloat = 164.1f;
    private long testPrimitiveLong = 164L;

    private static final String TEST_STATIC_FIELD = "TEST_STATIC_FIELD";

    public static float testStaticPrimitiveFloat = 5432.12f;

    public String getStringFieldInClassObj() {
        return stringFieldInClassObj;
    }

    public void setStringFieldInClassObj(final String stringFieldInClassObj) {
        this.stringFieldInClassObj = stringFieldInClassObj;
    }

    public int getIntFieldInClassObj() {
        return intFieldInClassObj;
    }

    public void setIntFieldInClassObj(final int intFieldInClassObj) {
        this.intFieldInClassObj = intFieldInClassObj;
    }

    public boolean isBooleanFieldInClassObj() {
        return booleanFieldInClassObj;
    }

    public void setBooleanFieldInClassObj(final boolean booleanFieldInClassObj) {
        this.booleanFieldInClassObj = booleanFieldInClassObj;
    }

    public Date[] getDateArrayFieldInClassObj() {
        return dateArrayFieldInClassObj;
    }

    public void setDateArrayFieldInClassObj(final Date[] dateArrayFieldInClassObj) {
        this.dateArrayFieldInClassObj = dateArrayFieldInClassObj;
    }

    public char getTestPrimitiveChar() {
        return testPrimitiveChar;
    }

    public void setTestPrimitiveChar(final char testPrimitiveChar) {
        this.testPrimitiveChar = testPrimitiveChar;
    }

    @Override
    public float getTestPrimitiveFloat() {
        return testPrimitiveFloat;
    }

    @Override
    public void setTestPrimitiveFloat(final float testPrimitiveFloat) {
        this.testPrimitiveFloat = testPrimitiveFloat;
    }

    public long getTestPrimitiveLong() {
        return testPrimitiveLong;
    }

    public void setTestPrimitiveLong(final long testPrimitiveLong) {
        this.testPrimitiveLong = testPrimitiveLong;
    }

    public static float getTestStaticPrimitiveFloat() {
        return testStaticPrimitiveFloat;
    }

    public static void setTestStaticPrimitiveFloat(final float testStaticPrimitiveFloat) {
        TestClassObj.testStaticPrimitiveFloat = testStaticPrimitiveFloat;
    }
}
