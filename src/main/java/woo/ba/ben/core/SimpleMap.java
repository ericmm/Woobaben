package woo.ba.ben.core;


public interface SimpleMap<K, V> {
    V get(Object key);
    V put(K key, V value);
    V remove(Object key);
    int size();
}
