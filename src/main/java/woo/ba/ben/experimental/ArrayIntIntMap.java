package woo.ba.ben.experimental;


import java.util.Arrays;
import java.util.Objects;

import static java.util.Arrays.fill;

public class ArrayIntIntMap implements IntIntMap {
    public static final int MAXIMUM_CAPACITY = 1 << 30;
    public static final int NO_VALUE = 0;

    private static final int FREE_KEY = 0;
    private static final int RANDOM_INT = 0x9E3779B9;
    private static final float DEFAULT_FILL_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 12;

    /**
     * Fill factor, must be between (0 and 1)
     */
    private final float fillFactor;

    private int[] data;
    private boolean hasFreeKey;
    private int valueForFreeKey;
    private int threshold;
    private int size;

    private int mask;
    private int nextMask;

    public ArrayIntIntMap(final int size, final float fillFactor) {
        checkSizeAndFillFactor(size, fillFactor);
        this.fillFactor = fillFactor;
        initializeArray(size);
    }

    public ArrayIntIntMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_FILL_FACTOR);
    }

    @Override
    public int get(final int key) {
        if (key == FREE_KEY) {
            return hasFreeKey ? valueForFreeKey : NO_VALUE;
        }

        int index = findIndex(key);
        int foundKey;
        while (true) {
            foundKey = data[index];
            if (foundKey == FREE_KEY) {
                return NO_VALUE;
            } else if (foundKey == key) {
                return data[index + 1];
            }

            index = findNextIndex(index);
        }
    }

    @Override
    public int put(final int key, final int value) {
        if (key == FREE_KEY) {
            return insertValueForFreeKey(value);
        }

        int index = findIndex(key);
        int foundKey;
        while (true) {
            foundKey = data[index];
            if (foundKey == FREE_KEY) {
                return putValue(key, value, index);
            } else if (foundKey == key) {
                return replaceValue(index, value);
            }
            index = findNextIndex(index);
        }
    }

    @Override
    public int remove(final int key) {
        if (key == FREE_KEY) {
            return removeFreeKey();
        }

        final int initialIndex = findIndex(key);
        int index = initialIndex;
        int foundKey;
        while (true) {
            foundKey = data[index];
            if (foundKey == key) {
                final int previousValue = data[index + 1];
                data[index + 1] = NO_VALUE;
                size--;
                shiftEntries(index, initialIndex);
                return previousValue;
            } else if (foundKey == FREE_KEY) {
                return NO_VALUE;
            }
            index = findNextIndex(index);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        fill(data, FREE_KEY);
        hasFreeKey = false;
        valueForFreeKey = NO_VALUE;
        size = 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArrayIntIntMap that = (ArrayIntIntMap) o;
        return Float.compare(that.fillFactor, fillFactor) == 0 &&
                hasFreeKey == that.hasFreeKey &&
                valueForFreeKey == that.valueForFreeKey &&
                Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(fillFactor, hasFreeKey, valueForFreeKey);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }


    //////////////////////////////////
    private int putValue(final int key, final int value, final int index) {
        data[index] = key;
        data[index + 1] = value;

        if (size >= threshold) {
            resize(size * 2);
        } else {
            size++;
        }
        return NO_VALUE;
    }

    private int replaceValue(final int index, final int value) {
        final int previousValue = data[index + 1];
        data[index + 1] = value;
        return previousValue;
    }

    private void shiftEntries(final int index, final int initialIndex) {
        int nextKey;
        int nextIndex;
        int currentIndex = index;

        while (true) {
            nextIndex = findNextIndex(currentIndex);
            nextKey = data[nextIndex];
            if (nextKey == FREE_KEY || findIndex(nextKey) != initialIndex) {
                break;
            }

            // shift key & value
            data[currentIndex] = data[nextIndex];
            data[currentIndex + 1] = data[nextIndex + 1];

            data[nextIndex] = FREE_KEY;
            data[nextIndex + 1] = NO_VALUE;

            currentIndex = nextIndex;
        }
    }

    private void resize(final int newSize) {
        final int[] oldData = data;
        initializeArray(newSize);
        size = hasFreeKey ? 1 : 0;

        for (int i = 0; i < oldData.length; i += 2) {
            if (oldData[i] != FREE_KEY) {
                put(oldData[i], oldData[i + 1]);
            }
        }
    }

    private int removeFreeKey() {
        if (hasFreeKey) {
            final int previousValue = valueForFreeKey;
            valueForFreeKey = NO_VALUE;
            hasFreeKey = false;
            size--;
            return previousValue;
        }
        return NO_VALUE;
    }

    private int insertValueForFreeKey(final int value) {
        if (hasFreeKey) {
            final int previousValue = valueForFreeKey;
            valueForFreeKey = value;
            return previousValue;
        } else {
            valueForFreeKey = value;
            hasFreeKey = true;
            size++;
            return NO_VALUE;
        }
    }

    private int findIndex(final int key) {
        return (randomise(key) & mask) << 1;
    }

    private int findNextIndex(final int index) {
        return (index + 2) & nextMask;
    }

    private void initializeArray(final int size) {
        final int capacity = arraySize(size, fillFactor);
        mask = capacity - 1;
        nextMask = capacity * 2 - 1;

        data = new int[capacity * 2];
        threshold = (int) (capacity * fillFactor);
    }

    private static void checkSizeAndFillFactor(final int size, final float fillFactor) {
        assert fillFactor > 0 && fillFactor < 1 : "FillFactor must be in (0, 1)";
        assert size > 0 : "Size must be positive";
    }

    private static int randomise(final int x) {
        final int i = x * RANDOM_INT;
        return i ^ (i >> 16);
    }

    private static int arraySize(final int expectedSize, final float fillFactor) {
        final long actualSize = Math.max(2, nextPowerOfTwo((long) Math.ceil(expectedSize / fillFactor)));
        assert actualSize <= MAXIMUM_CAPACITY : "Too large (expected elements " + expectedSize + " with load factor " + fillFactor + ")";
        return (int) actualSize;
    }

    private static long nextPowerOfTwo(long v) {
        if (v < 1) {
            return 0;
        }
        v--;
        v |= v >> 1;
        v |= v >> 2;
        v |= v >> 4;
        v |= v >> 8;
        v |= v >> 16;
        return ++v;
    }
}

