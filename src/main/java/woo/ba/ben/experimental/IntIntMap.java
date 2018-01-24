package woo.ba.ben.experimental;


public interface IntIntMap {
    int get(int key);

    int put(int key, int value);

    int remove(int key);

    int size();

    boolean isEmpty();

    void clear();

    boolean equals(Object o);

    int hashCode();
}
