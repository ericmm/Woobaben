package woo.ba.ben.core;


import static woo.ba.ben.util.Util.nextPowerOfTwo;

public abstract class AbstractHashBase {
    protected static final Object FREE_KEY = new Object() {
        @Override
        public String toString(){
            return "#FREE#";
        }
    };
    protected static final Object REMOVED_KEY = new Object(){
        @Override
        public String toString(){
            return "#REMOVED#";
        }
    };
    protected static final float DEFAULT_LOAD_FACTOR = 0.75f;
    protected static final int DEFAULT_INITIAL_CAPACITY = 12;

    private static final int MAXIMUM_CAPACITY = 1 << 30;

    protected static int arraySize(final int expectedSize, final float fillFactor) {
        final long dataSize = Math.max(2, nextPowerOfTwo((long) Math.ceil(expectedSize / fillFactor)));
        checkSize(expectedSize, fillFactor, dataSize);
        return (int) dataSize;
    }

    protected static void checkSize(final int expectedSize, final float fillFactor, final long dataSize) {
        if (dataSize > MAXIMUM_CAPACITY) {
            throw new RuntimeException("Too large (" + expectedSize + " expected elements with load factor "
                    + fillFactor + "), maximum capacity is " + MAXIMUM_CAPACITY + ".");
        }
    }

    protected void checkFillFactorAndSize(int size, float fillFactor) {
        if (fillFactor <= 0 || fillFactor >= 1) {
            throw new IllegalArgumentException("FillFactor must be in (0, 1)");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive!");
        }
    }

    protected int getStartIndex(final Object key, final int arraySize) {
        return key.hashCode() & (arraySize -1);
    }

    protected int getNextIndex(final int index, final int arraySize) {
        return (index + 1) & (arraySize - 1);
    }
}
