package woo.ba.ben.core;


import java.util.*;

import static java.util.Arrays.fill;

/**
 * inspired by Mikhail Vorontsov's ObjObjMap
 */
public class ArrayBackedHashMap<K, V> extends AbstractHashBase implements Map<K, V> {
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
        checkFillFactorAndSize(size, fillFactor);
        this.fillFactor = fillFactor;
        initDataBlock(size);
    }

    @Override
    public V get(final Object key) {
        if (key == null) {
            return (V) nullValue;
        }

        int index = getStartIndex(key, keys.length - 1);
        Object objKey;
        for (int i = 0; i < keys.length; i++) {
            objKey = keys[index];
            if (objKey == FREE_KEY) {
                return null;
            } else if (objKey.equals(key)) {
                return (V) values[index];
            }
            index = getNextIndex(index, keys.length - 1);
        }
        return null;
    }

    @Override
    public V put(final K key, final V value) {
        if (key == null) {
            return insertNullKey(value);
        }

        int firstRemoved = -1;
        int index = getStartIndex(key, keys.length - 1);
        Object objKey;
        for (int i = 0; i < keys.length; i++) {
            objKey = keys[index];
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
            index = getNextIndex(index, keys.length - 1);
        }

        if (firstRemoved != -1) {
            return putValue(key, value, firstRemoved);
        } else {
            final String message = "Cannot find a place to put, this should never happen! \n"
                    + "FREE_KEY=[" + FREE_KEY + "], REMOVED_KEY=[" + REMOVED_KEY + "], \n"
                    + "key array is {" + Arrays.toString(keys) + "}";
            throw new RuntimeException(message);
        }
    }

    @Override
    public V remove(final Object key) {
        if (key == null) {
            return removeNullKey();
        }

        int index = getStartIndex(key, keys.length - 1);
        Object objKey;
        for (int i = 0; i < keys.length; i++) {
            objKey = keys[index];
            if (objKey == FREE_KEY) {
                return null;
            } else if (objKey.equals(key)) {
                --size;
                if (keys[getNextIndex(index, keys.length - 1)] == FREE_KEY) {
                    keys[index] = FREE_KEY;
                } else {
                    keys[index] = REMOVED_KEY;
                }
                return replaceValue(index, null);
            }
            index = getNextIndex(index, keys.length - 1);
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        fill(keys, FREE_KEY);
        fill(values, FREE_KEY);
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

        fill(keys, FREE_KEY);
        fill(values, FREE_KEY);

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
}
