package woo.ba.ben.core;


import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

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


    @Override
    public boolean contains(final Object element) {
        if (element == null) {
            return hasNull;
        }

        Object obj;
        for (int i = 0; i < elements.length; i++) {
            obj = elements[i];
            if (obj == FREE_KEY || obj == REMOVED_KEY) {
                continue;
            }

            if (element.equals(obj)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return null;
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
                    + "FREE_KEY=[" + FREE_KEY + "], REMOVED_KEY=[" + REMOVED_KEY + "], \n"
                    + "element array is {" + Arrays.toString(elements) + "}";
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
    public boolean containsAll(final Collection<?> c) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        //TODO
        throw new UnsupportedOperationException();
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
    }
}
