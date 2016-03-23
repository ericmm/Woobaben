package woo.ba.ben.core;


import java.util.*;

import static java.util.Arrays.fill;
import static java.util.Collections.EMPTY_LIST;

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

    private Object valueForNullKey = null;
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
            return (V) valueForNullKey;
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
                    + "The key array is {" + Arrays.toString(keys) + "}";
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
        valueForNullKey = null;
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

        int index = getStartIndex(key, keys.length - 1);
        for (int i = 0; i < keys.length; i++) {
            if (keys[index] == FREE_KEY) {
                return false;
            }else if (keys[index].equals(key)) {
                return true;
            }
            index = getNextIndex(index, keys.length - 1);
        }
        return false;
    }

    @Override
    public boolean containsValue(final Object value) {
        if (hasNull && Objects.equals(value, valueForNullKey)) {
            return true;
        }

        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == FREE_KEY || keys[i] == REMOVED_KEY) {
                continue;
            }
            if (Objects.equals(value, values[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> anotherMap) {
        if (anotherMap == null || anotherMap.isEmpty()) {
            return;
        }

        final int newSize = size + anotherMap.size();
        final int newCapacity = arraySize(newSize, fillFactor);
        if (newCapacity != keys.length) {
            final Object[] oldKeys = keys;
            final Object[] oldValues = values;

            allocateArrays(newSize);

            size = hasNull ? 1 : 0;

            for (int i = 0; i < oldKeys.length; i++) {
                if (oldKeys[i] != FREE_KEY && oldKeys[i] != REMOVED_KEY) {
                    put((K) oldKeys[i], (V) oldValues[i]);
                    size++;
                }
            }
        }

        for (final Entry<? extends K, ? extends V> entry : anotherMap.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<K> keySet() {
        return new ArrayBackedHashSet<>(keys, size, fillFactor, hasNull, threshold);
    }

    @Override
    public Collection<V> values() {
        if(size == 0) {
            return EMPTY_LIST;
        }

        int index = 0;
        final Object[] theValues = new Object[size];
        if (hasNull) {
            theValues[index++] = valueForNullKey;
        }
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] != FREE_KEY && keys[i] != REMOVED_KEY) {
                theValues[index++] = values[i];
            }
        }
        return Arrays.asList((V[]) theValues);
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
            final Object previousValue = valueForNullKey;
            valueForNullKey = value;
            return (V) previousValue;
        } else {
            valueForNullKey = value;
            hasNull = true;
            ++size;
            return null;
        }
    }

    private V removeNullKey() {
        if (hasNull) {
            final Object previousValue = valueForNullKey;
            valueForNullKey = null;
            hasNull = false;
            --size;
            return (V) previousValue;
        } else {
            return null;
        }
    }

    private void initDataBlock(final int size) {
        allocateArrays(arraySize(size, fillFactor));
    }

    private void allocateArrays(final int capacity) {
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

        fill(oldKeys, null);
        fill(oldValues, null);
    }
}
