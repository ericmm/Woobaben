package woo.ba.ben.core;


import java.util.*;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.fill;

public class ArrayBackedHashSet<E> extends AbstractHashBase implements Set<E> {
    private final float fillFactor;
    private Object[] elements;
    private boolean hasNull = false;

    private int threshold;
    private int size;

    public ArrayBackedHashSet() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public ArrayBackedHashSet(final int size) {
        this(size, DEFAULT_LOAD_FACTOR);
    }

    public ArrayBackedHashSet(final int size, final float fillFactor) {
        checkFillFactorAndSize(size, fillFactor);
        this.fillFactor = fillFactor;
        initDataBlock(size);
    }

    ArrayBackedHashSet(final Object[] elements, final int size, final float fillFactor, final boolean hasNull, final int threshold) {
        this.fillFactor = fillFactor;
        this.size = size;
        this.threshold = threshold;
        this.hasNull = hasNull;
        this.elements = copyOf(elements, elements.length);
    }


    @Override
    public boolean contains(final Object element) {
        if (element == null) {
            return hasNull;
        }

        int index = getStartIndex(element, elements.length - 1);
        for (int i = 0; i < elements.length; i++) {
            if (elements[index] == FREE_KEY) {
                return false;
            } else if (elements[index].equals(element)) {
                return true;
            }
            index = getNextIndex(index, elements.length - 1);
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        final Object[] result = new Object[size];

        int index = 0;
        if (hasNull) {
            result[index++] = null;
        }
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] != FREE_KEY && elements[i] != REMOVED_KEY) {
                result[index++] = elements[i];
            }
        }
        return result;
    }

    @Override
    public <E> E[] toArray(final E[] a) {
        return (E[]) toArray();
    }

    @Override
    public boolean add(final E element) {
        if (element == null) {
            return insertNull();
        }

        int firstRemoved = -1;
        int index = getStartIndex(element, elements.length - 1);
        Object obj;
        for (int i = 0; i < elements.length; i++) {
            obj = elements[index];
            if (obj == FREE_KEY) { //end of chain
                if (firstRemoved != -1) {
                    index = firstRemoved;
                }
                return putValue(element, index);
            } else if (obj.equals(element)) {
                return false;
            } else if (obj == REMOVED_KEY && firstRemoved == -1) {
                firstRemoved = index;
            }
            index = getNextIndex(index, elements.length - 1);
        }

        if (firstRemoved != -1) {
            return putValue(element, firstRemoved);
        } else {
            final String message = "Cannot find a place to put, this should never happen! \n"
                    + "The element array is {" + Arrays.toString(elements) + "}";
            throw new RuntimeException(message);
        }
    }

    @Override
    public boolean remove(final Object element) {
        if (element == null) {
            return removeNull();
        }

        int index = getStartIndex(element, elements.length - 1);
        Object obj;
        for (int i = 0; i < elements.length; i++) {
            obj = elements[index];
            if (obj == FREE_KEY) {
                return false;
            } else if (obj.equals(element)) {
                --size;
                if (elements[getNextIndex(index, elements.length - 1)] == FREE_KEY) {
                    elements[index] = FREE_KEY;
                } else {
                    elements[index] = REMOVED_KEY;
                }
                return true;
            }
            index = getNextIndex(index, elements.length - 1);
        }
        return false;
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        //TODO: if there's a better way
        for (final Object element : collection) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        if (collection == null) {
            throw new NullPointerException("Input parameter is null");
        } else if (collection.isEmpty()) {
            return false;
        }

        final int newSize = size + collection.size();
        final int newCapacity = arraySize(newSize, fillFactor);
        if (newCapacity != elements.length) {
            final Object[] oldElements = elements;

            initDataBlock(newSize);
            size = hasNull ? 1 : 0;

            for (int i = 0; i < oldElements.length; i++) {
                if (oldElements[i] != FREE_KEY && oldElements[i] != REMOVED_KEY) {
                    add((E) oldElements[i]);
                    size++;
                }
            }
        }

        boolean modified = false;
        for (final E e : collection) {
            if (add(e)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        //TODO: if there's a better way

        Objects.requireNonNull(collection);
        boolean modified = false;
        final Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean removeAll(final Collection<?> collection) {
        //TODO: if there's a better way

        Objects.requireNonNull(collection);
        boolean modified = false;
        final Iterator<?> it = iterator();
        while (it.hasNext()) {
            if (collection.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        fill(elements, FREE_KEY);
        hasNull = false;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /////////////////////////////////////////
    private boolean putValue(final E element, final int index) {
        elements[index] = element;

        if (size >= threshold) {
            resize();
        } else {
            ++size;
        }
        return true;
    }

    private boolean insertNull() {
        if (hasNull) {
            return false;
        } else {
            hasNull = true;
            ++size;
            return true;
        }
    }

    private boolean removeNull() {
        if (hasNull) {
            hasNull = false;
            --size;
            return true;
        } else {
            return false;
        }
    }

    private void initDataBlock(final int size) {
        final int capacity = arraySize(size, fillFactor);

        elements = new Object[capacity];
        fill(elements, FREE_KEY);

        threshold = (int) (capacity * fillFactor);
    }

    private void resize() {
        final Object[] oldElements = elements;
        initDataBlock(size * 2);

        size = hasNull ? 1 : 0;

        Object old;
        for (int i = 0; i < oldElements.length; i++) {
            old = oldElements[i];
            if (old != FREE_KEY && old != REMOVED_KEY) {
                add((E) old);
                size++;
            }
        }
        fill(oldElements, null);
    }
}
