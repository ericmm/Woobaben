package woo.ba.ben.core;

import java.util.Arrays;

/**
 * refactored based on Mikhail Vorontsov's ObjObjMap
 */
public class SimpleArrayMap<K, V> implements SimpleMap<K, V> {
    private static final Object FREE_KEY = new Object();
    private static final Object REMOVED_KEY = new Object();

    private static final float DEFAULT_LOAD_FACTOR = 0.65f;
    private static final int DEFAULT_INITIAL_CAPACITY = 20;
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * Keys and values
     */
    private Object[] data;
    /**
     * Value for the null key (if inserted into a map)
     */
    private Object nullValue = null;
    private boolean hasNull = false;

    /**
     * Fill factor, must be between (0 and 1)
     */
    private final float fillFactor;
    /**
     * We will resize a map once it reaches this size
     */
    private int threshold;
    /**
     * Current map size
     */
    private int size;
    /**
     * Mask to calculate the original position
     */
    private int indexMask;
    /**
     * Mask to wrap the actual array pointer
     */
    private int nextIndexMask;

    public SimpleArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public SimpleArrayMap(final int size) {
        this(size, DEFAULT_LOAD_FACTOR);
    }

    public SimpleArrayMap(final int size, final float fillFactor) {
        if (fillFactor <= 0 || fillFactor >= 1) {
            throw new IllegalArgumentException("FillFactor must be in (0, 1)");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive!");
        }

        final int capacity = arraySize(size, fillFactor);
        indexMask = capacity - 1;
        nextIndexMask = capacity * 2 - 1;
        this.fillFactor = fillFactor;

        data = new Object[capacity * 2];
        Arrays.fill(data, FREE_KEY);

        threshold = (int) (capacity * fillFactor);
    }

    @Override
    public V get(final Object key) {
        if (key == null) {
            return (V) nullValue; //we null it on remove, so safe not to check a flag here
        }

        int index = getStartIndex(key) << 1;
        Object objKey = data[index];

        if (objKey == FREE_KEY) {
            return null; //end of chain already
        }
        if (objKey.equals(key)) { //we check FREE and REMOVED prior to this call
            return (V) data[index + 1];
        }

        while (true) {
            index = (index + 2) & nextIndexMask; //that's next index
            objKey = data[index];
            if (objKey == FREE_KEY) {
                return null;
            }
            if (objKey.equals(key)) {
                return (V) data[index + 1];
            }
        }
    }

    @Override
    public V put(final K key, final V value) {
        if (key == null) {
            return insertNullKey(value);
        }

        int index = getStartIndex(key) << 1;
        Object objKey = data[index];

        if (objKey == FREE_KEY) { //end of chain already
            putValue(key, value, index);
            return null;
        } else if (objKey.equals(key)) { //we check FREE and REMOVED prior to this call
            final Object previousValue = data[index + 1];
            data[index + 1] = value;
            return (V) previousValue;
        }

        int firstRemoved = -1;
        if (objKey == REMOVED_KEY) {
            firstRemoved = index; //we may find a key later
        }

        while (true) {
            index = (index + 2) & nextIndexMask; //that's next index calculation
            objKey = data[index];
            if (objKey == FREE_KEY) {
                if (firstRemoved != -1) {
                    index = firstRemoved;
                }
                putValue(key, value, index);
                return null;
            } else if (objKey.equals(key)) {
                final Object previousValue = data[index + 1];
                data[index + 1] = value;
                return (V) previousValue;
            } else if (objKey == REMOVED_KEY) {
                if (firstRemoved == -1) {
                    firstRemoved = index;
                }
            }
        }
    }

    @Override
    public V remove(final Object key) {
        if (key == null) {
            return removeNullKey();
        }

        int index = getStartIndex(key) << 1;
        Object objKey = data[index];
        if (objKey == FREE_KEY) {
            return null;  //end of chain already
        } else if (objKey.equals(key)) { //we check FREE and REMOVED prior to this call
            --size;
            if (data[(index + 2) & nextIndexMask] == FREE_KEY) {
                data[index] = FREE_KEY;
            } else {
                data[index] = REMOVED_KEY;
            }
            final V previousValue = (V) data[index + 1];
            data[index + 1] = null;
            return previousValue;
        }

        while (true) {
            index = (index + 2) & nextIndexMask; //that's next index calculation
            objKey = data[index];
            if (objKey == FREE_KEY) {
                return null;
            } else if (objKey.equals(key)) {
                --size;
                if (data[(index + 2) & nextIndexMask] == FREE_KEY) {
                    data[index] = FREE_KEY;
                } else {
                    data[index] = REMOVED_KEY;
                }
                final V previousValue = (V) data[index + 1];
                data[index + 1] = null;
                return previousValue;
            }
        }
    }

    @Override
    public int size() {
        return size;
    }

    private void putValue(K key, V value, int index) {
        data[index] = key;
        data[index + 1] = value;
        if (size >= threshold) {
            resize(data.length * 2); //size is set inside
        } else {
            ++size;
        }
    }

    private V insertNullKey(final V value) {
        if (hasNull) {
            final Object previousValue = nullValue;
            nullValue = value;
            return (V) previousValue;
        } else {
            nullValue = value;
            hasNull = true;
            ++size;
            return null;
        }
    }

    private V removeNullKey() {
        if (hasNull) {
            final Object previousValue = nullValue;
            nullValue = null;
            hasNull = false;
            --size;
            return (V) previousValue;
        } else {
            return null;
        }
    }

    private void resize(final int newCapacity) {
        threshold = (int) (newCapacity / 2 * fillFactor);
        indexMask = newCapacity / 2 - 1;
        nextIndexMask = newCapacity - 1;

        final int oldCapacity = data.length;
        final Object[] oldData = data;

        data = new Object[newCapacity];
        Arrays.fill(data, FREE_KEY);

        size = hasNull ? 1 : 0;

        for (int i = 0; i < oldCapacity; i += 2) {
            final Object oldKey = oldData[i];
            if (oldKey != FREE_KEY && oldKey != REMOVED_KEY) {
                put((K) oldKey, (V) oldData[i + 1]);
            }
        }
    }

    private int getStartIndex(final Object key) {
        return key.hashCode() & indexMask;
    }

    private static int arraySize(final int expected, final float f) {
        final long s = Math.max(2, nextPowerOfTwo((long) Math.ceil(expected / f)));
        if (s > MAXIMUM_CAPACITY) {
            throw new IllegalArgumentException("Too large (" + expected + " expected elements with load factor " + f + ")");
        }
        return (int) s;
    }

    private static long nextPowerOfTwo(long x) {
        if (x == 0) return 1;
        x--;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return (x | x >> 32) + 1;
    }
}
