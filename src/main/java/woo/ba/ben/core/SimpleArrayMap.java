package woo.ba.ben.core;

import java.util.*;

/**
 * refactored and improved based on Mikhail Vorontsov's ObjObjMap
 */
public class SimpleArrayMap<K, V> implements Map<K, V> {
    private static final Object FREE_KEY = new Object();
    private static final Object REMOVED_KEY = new Object();

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 12;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    /**
     * Fill factor, must be between (0 and 1)
     */
    private final float fillFactor;
    /**
     * Keys and values
     */
    private Object[] data;
    /**
     * value for the null key
     */
    private Object nullValue = null;
    private boolean hasNull = false;

    private int threshold;
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

        this.fillFactor = fillFactor;

        initDataBlock(size);
    }

    private static int getStartIndex(final Object key, final int indexMask) {
        return (key.hashCode() & indexMask) << 1;
    }

    private static int getNextIndex(final int index, final int nextIndexMask) {
        return (index + 2) & nextIndexMask;
    }

    private static int arraySize(final int expectedSize, final float fillFactor) {
        final long dataSize = Math.max(2, nextPowerOfTwo((long) Math.ceil(expectedSize / fillFactor)));
        checkSize(expectedSize, fillFactor, dataSize);
        return (int) dataSize;
    }

    private static void checkSize(final int expectedSize, final float fillFactor, final long dataSize) {
        if (dataSize > MAXIMUM_CAPACITY) {
            throw new IllegalArgumentException("Too large (" + expectedSize + " expected elements with load factor " + fillFactor + ")");
        }
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

    @Override
    public V get(final Object key) {
        if (key == null) {
            return (V) nullValue; //we null it on remove, so safe not to check a flag here
        }

        int index = getStartIndex(key, indexMask);
        Object objKey = data[index];

        if (objKey == FREE_KEY) {
            return null; //end of chain already
        }
        if (objKey.equals(key)) { //we check FREE and REMOVED prior to this call
            return (V) data[index + 1];
        }

        while (true) {
            index = getNextIndex(index, nextIndexMask); //that's next index
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

        int index = getStartIndex(key, indexMask);
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
            index = getNextIndex(index, nextIndexMask); //that's next index calculation
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

        int index = getStartIndex(key, indexMask);
        Object objKey = data[index];
        if (objKey == FREE_KEY) {
            return null;  //end of chain already
        } else if (objKey.equals(key)) { //we check FREE and REMOVED prior to this call
            --size;
            if (data[getNextIndex(index, nextIndexMask)] == FREE_KEY) {
                data[index] = FREE_KEY;
            } else {
                data[index] = REMOVED_KEY;
            }
            final V previousValue = (V) data[index + 1];
            data[index + 1] = null;
            return previousValue;
        }

        while (true) {
            index = getNextIndex(index, nextIndexMask); //that's next index calculation
            objKey = data[index];
            if (objKey == FREE_KEY) {
                return null;
            } else if (objKey.equals(key)) {
                --size;
                if (data[getNextIndex(index, nextIndexMask)] == FREE_KEY) {
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
    public void putAll(final Map<? extends K, ? extends V> m) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(data, FREE_KEY);
        hasNull = false;
        nullValue = null;
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(final Object key) {
        if (key == null) {
            return hasNull ? true : false;
        }

        Object objKey;
        for (int i = 0; i < data.length; i += 2) {
            objKey = data[i];
            if (objKey == FREE_KEY || objKey == REMOVED_KEY) {
                continue;
            }

            if (key.equals(objKey)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(final Object value) {
        if (hasNull && Objects.equals(value, nullValue)) {
            return true;
        }

        Object objKey;
        for (int i = 0; i < data.length; i += 2) {
            objKey = data[i];
            if (objKey == FREE_KEY || objKey == REMOVED_KEY) {
                continue;
            }

            if (Objects.equals(value, data[i + 1])) {
                return true;
            }
        }
        return false;
    }

    /////////////////////////////////////////
    private void putValue(final K key, final V value, final int index) {
        data[index] = key;
        data[index + 1] = value;

        if (size >= threshold) {
            resize();
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

    private void initDataBlock(final int size) {
        final int capacity = arraySize(size, fillFactor);
        indexMask = capacity - 1;
        nextIndexMask = capacity * 2 - 1;

        data = new Object[capacity * 2];
        Arrays.fill(data, FREE_KEY);

        threshold = (int) (capacity * fillFactor);
    }

    private void resize() {
        final Object[] oldData = data;
        initDataBlock(size * 2);

        size = hasNull ? 1 : 0;

        Object oldKey;
        for (int i = 0; i < oldData.length; i += 2) {
            oldKey = oldData[i];
            if (oldKey != FREE_KEY && oldKey != REMOVED_KEY) {
                put((K) oldKey, (V) oldData[i + 1]);
                size++;
            }
        }
    }
}
