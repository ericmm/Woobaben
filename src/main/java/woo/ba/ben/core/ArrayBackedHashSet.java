package woo.ba.ben.core;


import java.util.*;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.fill;
import static java.util.Objects.requireNonNull;

public class ArrayBackedHashSet<E> extends AbstractHashBase implements Set<E> {
    private static final int INITIAL_INDEX = -3;
    private static final int NULL_ELEMENT_INDEX = -2;

    private final float fillFactor;
    private Object[] elements;
    private boolean hasNull = false;
    private int threshold;
    private int size;
    private int updateVersion;

    public ArrayBackedHashSet() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayBackedHashSet(final int size) {
        this(size, DEFAULT_LOAD_FACTOR);
    }

    public ArrayBackedHashSet(final int size, final float fillFactor) {
        checkFillFactorAndSize(size, fillFactor);
        this.fillFactor = fillFactor;
        this.updateVersion = 0;
        initDataBlock(size);
    }

    ArrayBackedHashSet(final Object[] elements, final int size, final float fillFactor, final boolean hasNull, final int threshold) {
        this.fillFactor = fillFactor;
        this.updateVersion = 0;
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
        return findIndex(elements, element) != NOT_FOUND_INDEX;
    }

    @Override
    public Iterator<E> iterator() {
        return new SetIterator();
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

        int firstRemoved = NOT_FOUND_INDEX;
        int index = getStartIndex(element, elements.length);
        for (int i = 0; i < elements.length; i++) {
            if (elements[index] == FREE_KEY) { //end of chain
                if (firstRemoved != NOT_FOUND_INDEX) {
                    index = firstRemoved;
                }
                return putValue(element, index);
            } else if (elements[index].equals(element)) {
                return false;
            } else if (elements[index] == REMOVED_KEY && firstRemoved == NOT_FOUND_INDEX) {
                firstRemoved = index;
            }
            index = getNextIndex(index, elements.length);
        }

        if (firstRemoved != NOT_FOUND_INDEX) {
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

        final int foundIndex = findIndex(elements, element);
        if (foundIndex != NOT_FOUND_INDEX) {
            --size;
            ++updateVersion;
            removeAt(elements, foundIndex);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(final Collection<?> collection) {
        requireNonNull(collection);
        if (collection.isEmpty()) {
            return size == 0;
        }

        //loop through smaller collection
        final int collectionSize = collection.size();
        if (collectionSize > size) {
            return containsCollection(this, collection);
        } else {
            return containsCollection(collection, this);
        }
    }

    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        requireNonNull(collection);
        if (collection.isEmpty()) {
            return false;
        }

        final int newSize = size + collection.size();
        if (arraySize(newSize, fillFactor) != elements.length) {
            resize(newSize);
        }

        boolean modified = false;
        for (final E element : collection) {
            if (add(element)) {
                modified = true;
            }
        }

        if (modified) {
            ++updateVersion;
        }
        return modified;
    }

    @Override
    public boolean retainAll(final Collection<?> collection) {
        requireNonNull(collection);
        if (collection.isEmpty()) {
            if (size > 0) {
                clear();
                return true;
            }
            return false;
        }

        //check null
        boolean modified = false;
        if (!collection.contains(null) && hasNull) {
            modified = true;
            removeNullSurely();
        }

        //TODO: loop through smaller collection?
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] != FREE_KEY && elements[i] != REMOVED_KEY && !collection.contains(elements[i])) {
                --size;
                modified = true;
                removeAt(elements, i);
            }
        }

        if (modified) {
            ++updateVersion;
        }
        return modified;
    }

    @Override
    public boolean removeAll(final Collection<?> collection) {
        requireNonNull(collection);
        if (collection.isEmpty()) {
            return false;
        }

        //check null
        boolean modified = false;
        if (collection.contains(null) && hasNull) {
            modified = true;
            removeNullSurely();
        }

        //TODO: loop through smaller collection?
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] != FREE_KEY && elements[i] != REMOVED_KEY && collection.contains(elements[i])) {
                --size;
                modified = true;
                removeAt(elements, i);
            }
        }

        if (modified) {
            ++updateVersion;
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
        updateVersion = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /////////////////////////////////////////
    private boolean putValue(final E element, final int index) {
        elements[index] = element;

        ++updateVersion;
        if (size >= threshold) {
            resize(size * 2);
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
            ++updateVersion;
            return true;
        }
    }

    private boolean removeNull() {
        if (hasNull) {
            removeNullSurely();
            return true;
        } else {
            return false;
        }
    }

    private void removeNullSurely() {
        hasNull = false;
        --size;
        ++updateVersion;
    }

    private void initDataBlock(final int size) {
        final int capacity = arraySize(size, fillFactor);

        elements = new Object[capacity];
        fill(elements, FREE_KEY);

        threshold = (int) (capacity * fillFactor);
    }

    private void resize(final int newSize) {
        final Object[] oldElements = elements;
        initDataBlock(newSize);

        size = hasNull ? 1 : 0;

        for (int i = 0; i < oldElements.length; i++) {
            if (oldElements[i] != FREE_KEY && oldElements[i] != REMOVED_KEY) {
                add((E) oldElements[i]);
            }
        }
    }

    private boolean containsCollection(final Collection<?> smallCollection, final Collection<?> largeCollection) {
        for (final Object element : smallCollection) {
            if (!largeCollection.contains(element)) {
                return false;
            }
        }
        return true;
    }

    private class SetIterator<E> implements Iterator<E> {
        private int snapshotVersion;
        private int currentIndex;
        private int nextIndex;

        SetIterator() {
            this.snapshotVersion = updateVersion;
            currentIndex = INITIAL_INDEX;
            nextIndex = INITIAL_INDEX;
        }

        @Override
        public boolean hasNext() {
            if (snapshotVersion != updateVersion) {
                throw new ConcurrentModificationException();
            }
            nextIndex = findNextIndex();
            return nextIndex == NULL_ELEMENT_INDEX || nextIndex != NOT_FOUND_INDEX;
        }

        private int findNextIndex() {
            if (currentIndex == INITIAL_INDEX && hasNull) {
                return NULL_ELEMENT_INDEX;
            }

            int index = 0;
            if (currentIndex != INITIAL_INDEX && currentIndex != NULL_ELEMENT_INDEX) {
                index = currentIndex;
            }

            for (int i = index; i < elements.length; i++) {
                if (elements[i] != FREE_KEY && elements[i] != REMOVED_KEY) {
                    return i;
                }
            }
            return NOT_FOUND_INDEX;
        }

        @Override
        public E next() {
            if (snapshotVersion != updateVersion) {
                throw new ConcurrentModificationException();
            }

            nextIndex = findNextIndex();
            if (nextIndex == NULL_ELEMENT_INDEX) {
                currentIndex = NULL_ELEMENT_INDEX;
                return null;
            } else if (nextIndex == NOT_FOUND_INDEX) {
                throw new NoSuchElementException();
            } else {
                currentIndex = nextIndex;
                return (E) elements[currentIndex];
            }
        }

        @Override
        public void remove() {
            if (snapshotVersion != updateVersion) {
                throw new ConcurrentModificationException();
            }

            if (currentIndex == INITIAL_INDEX) {
                throw new IllegalStateException();
            } else if (currentIndex == NULL_ELEMENT_INDEX) {
                removeNullSurely();
                snapshotVersion = updateVersion;
            } else {
                removeAt(elements, currentIndex);
                updateVersion++;
                snapshotVersion = updateVersion;
            }
        }
    }
}
