package woo.ba.ben.core;


import java.util.Objects;

public interface SimpleMap<K, V> {
    V get(Object key);

    default V getOrDefault(final Object key, final V defaultValue) {
        final V v;
        return (((v = get(key)) != null) || containsKey(key))
                ? v
                : defaultValue;
    }

    V put(K key, V value);

    default V putIfAbsent(final K key, final V value) {
        V v = get(key);
        if (v == null) {
            v = put(key, value);
        }

        return v;
    }

    V remove(Object key);

    default boolean remove(final Object key, final Object value) {
        final Object curValue = get(key);
        if (!Objects.equals(curValue, value) ||
                (curValue == null && !containsKey(key))) {
            return false;
        }
        remove(key);
        return true;
    }

    int size();

    void clear();

    boolean isEmpty();

    boolean containsKey(Object key);

    boolean containsValue(Object value);

    void iterate(EntryProcessor processor);

    interface EntryProcessor<K, V> {
        void processEntry(K key, V value);
    }
}
