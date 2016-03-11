package woo.ba.ben.core;

import java.util.*;

import static woo.ba.ben.util.Util.nextPowerOfTwo;

/**
 * inspired by Mikhail Vorontsov's ObjObjMap
 */
public class ArrayBackedHashMap<K, V> implements Map<K, V> {
    private static final Object FREE_KEY = new Object();
    private static final Object REMOVED_KEY = new Object();

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_INITIAL_CAPACITY = 12;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    /**
     * Fill factor, must be between (0 and 1)
     */
    private final float fillFactor;

    private Object[] keys;
    private Object[] values;

    /**
     * value for the null key
     */
    private Object nullValue = null;
    private boolean hasNull = false;

    private int threshold;
    private int size;

    public ArrayBackedHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public ArrayBackedHashMap(final int size) {
        this(size, DEFAULT_LOAD_FACTOR);
    }

    public ArrayBackedHashMap(final int size, final float fillFactor) {
        if (fillFactor <= 0 || fillFactor >= 1) {
            throw new IllegalArgumentException("FillFactor must be in (0, 1)");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive!");
        }

        this.fillFactor = fillFactor;

        initDataBlock(size);
    }

    private static int arraySize(final int expectedSize, final float fillFactor) {
        final long dataSize = Math.max(2, nextPowerOfTwo((long) Math.ceil(expectedSize / fillFactor)));
        checkSize(expectedSize, fillFactor, dataSize);
        return (int) dataSize;
    }

    private static void checkSize(final int expectedSize, final float fillFactor, final long dataSize) {
        if (dataSize > MAXIMUM_CAPACITY) {
            throw new RuntimeException("Too large (" + expectedSize + " expected elements with load factor "
                    + fillFactor + "), maximum capacity is " + MAXIMUM_CAPACITY + ".");
        }
    }

    @Override
    public V get(final Object key) {
        if (key == null) {
            return (V) nullValue;
        }

        int index = getStartIndex(key);
        Object objKey = keys[index];
        for (int i = 0; i < keys.length; i++) {
            if (objKey == FREE_KEY) {
                return null;
            } else if (objKey.equals(key)) {
                return (V) values[index];
            }
            index = getNextIndex(index);
            objKey = keys[index];
        }
        return null;
    }

    @Override
    public V put(final K key, final V value) {
        if (key == null) {
            return insertNullKey(value);
        }

        int firstRemoved = -1;
        int index = getStartIndex(key);
        Object objKey = keys[index];
        for (int i = 0; i < keys.length; i++) {
            if (objKey == FREE_KEY) { //end of chain
                if (firstRemoved != -1) {
                    index = firstRemoved;
                }
                return putValue(key, value, index);
            } else if (objKey.equals(key)) {
                return replaceValue(index, value);
            } else if (objKey == REMOVED_KEY && firstRemoved == -1) {
                firstRemoved = index; //we may find a key later
            }
            index = getNextIndex(index);
            objKey = keys[index];
        }
        throw new RuntimeException("Cannot find a place to put, this should never happen!");
    }

    @Override
    public V remove(final Object key) {
        if (key == null) {
            return removeNullKey();
        }

        int index = getStartIndex(key);
        Object objKey = keys[index];
        for (int i = 0; i < keys.length; i++) {
            if (objKey == FREE_KEY) {
                return null;
            } else if (objKey.equals(key)) {
                --size;
                if (keys[getNextIndex(index)] == FREE_KEY) {
                    keys[index] = FREE_KEY;
                } else {
                    keys[index] = REMOVED_KEY;
                }
                return replaceValue(index, null);
            }
            index = getNextIndex(index);
            objKey = keys[index];
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(keys, FREE_KEY);
        Arrays.fill(values, FREE_KEY);
        hasNull = false;
        nullValue = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(final Object key) {
        if (key == null) {
            return hasNull;
        }

        Object objKey;
        for (int i = 0; i < keys.length; i++) {
            objKey = keys[i];
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
        for (int i = 0; i < keys.length; i++) {
            objKey = keys[i];
            if (objKey == FREE_KEY || objKey == REMOVED_KEY) {
                continue;
            }

            if (Objects.equals(value, values[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        //TODO
        throw new UnsupportedOperationException();
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

    /////////////////////////////////////////
    private V putValue(final K key, final V value, final int index) {
        keys[index] = key;
        values[index] = value;

        if (size >= threshold) {
            resize();
        } else {
            ++size;
        }
        return null;
    }

    private V replaceValue(final int index, final V value) {
        final V previousValue = (V) values[index];
        values[index] = value;
        return previousValue;
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

        keys = new Object[capacity];
        values = new Object[capacity];
        Arrays.fill(keys, FREE_KEY);
        Arrays.fill(values, FREE_KEY);

        threshold = (int) (capacity * fillFactor);
    }

    private void resize() {
        final Object[] oldKeys = keys;
        final Object[] oldValues = values;
        initDataBlock(size * 2);

        size = hasNull ? 1 : 0;

        Object oldKey;
        for (int i = 0; i < oldKeys.length; i++) {
            oldKey = oldKeys[i];
            if (oldKey != FREE_KEY && oldKey != REMOVED_KEY) {
                put((K) oldKey, (V) oldValues[i]);
                size++;
            }
        }
    }

    private int getStartIndex(final Object key) {
        return key.hashCode() & (keys.length - 1);
    }

    private int getNextIndex(final int index) {
        return (index + 1) & (keys.length - 1);
    }

}
