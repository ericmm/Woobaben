package woo.ba.ben.experimental;


public abstract class AbstractHashBase {
    protected static final Object FREE_KEY = new Object() {
        @Override
        public String toString() {
            return "#FREE#";
        }
    };
    protected static final Object REMOVED_KEY = new Object() {
        @Override
        public String toString() {
            return "#REMOVED#";
        }
    };
    protected static final float DEFAULT_LOAD_FACTOR = 0.75f;
    protected static final int DEFAULT_INITIAL_CAPACITY = 12;

    protected static final int NOT_FOUND_INDEX = -100;

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

    public static long nextPowerOfTwo(long x) {
        if (x == 0) {
            return 1;
        }

        x--;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return (x | x >> 32) + 1;
    }


    protected void checkFillFactorAndSize(final int size, final float fillFactor) {
        if (fillFactor <= 0 || fillFactor >= 1) {
            throw new IllegalArgumentException("FillFactor must be in (0, 1)");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive!");
        }
    }

    protected int getStartIndex(final Object key, final int arraySize) {
        return key.hashCode() & (arraySize - 1);
    }

    protected int getNextIndex(final int index, final int arraySize) {
        return (index + 1) & (arraySize - 1);
    }

    protected int findIndex(final Object[] keysArray, final Object key) {
        int index = getStartIndex(key, keysArray.length);
        Object foundKey;
        for (int i = 0; i < keysArray.length; i++) {
            foundKey = keysArray[index];
            if (foundKey == FREE_KEY || foundKey == REMOVED_KEY) {
                return NOT_FOUND_INDEX;
            } else if (foundKey.equals(key)) {
                return index;
            }
            index = getNextIndex(index, keysArray.length);
        }
        return NOT_FOUND_INDEX;
    }

    protected void removeAt(final Object[] elements, final int foundIndex) {
        if (elements[getNextIndex(foundIndex, elements.length)] == FREE_KEY) {
            elements[foundIndex] = FREE_KEY;
        } else {
            elements[foundIndex] = REMOVED_KEY;
        }
    }
}
