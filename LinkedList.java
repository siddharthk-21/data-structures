import java.lang.Iterable;
import java.util.Iterator;
import java.lang.IndexOutOfBoundsException;
import java.util.NoSuchElementException;

/**
 * Double linked list that is always sorted
 */
public class LinkedList<T> implements Iterable<T> {

    // The first and last nodes in the linked list
    private Node<T> first, last;

    // The number of nodes in the linked list
    private int num;

    /**
     * A constructor for the linked list. This one makes an empty list
     */
    public LinkedList () {
        first = null;
        last = null;
        num = 0;
    }

    /**
     * A constructor for the linked list. This one makes a list given
     * a set of elements
     * @param lst The set of elements you want in the linked list. Each element must be of the exact same type
     */
    @SafeVarargs
    public LinkedList (T... lst) {
        if (lst[0] != null) {
            first = new Node<T>(lst[0], null, null);
            last = first;
        }
        for (int i = 1; i < lst.length; i++)
            append(lst[i]);
        num = lst.length;
    }

    /**
     * Returns the iterator for the linked list
     * @return The iterator
     */
    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator<T>(this);
    }

    /**
     * Get the first element
     * @return The first element in the linked list
     */
    public T first () {
        return first.element;
    }

    /**
     * Get the last element
     * @return The last element in the linked list
     */
    public T last () {
        return last.element;
    }

    /**
     * Get the length of the linked list
     * @return The number of elements in the list
     */
    public int length () {
        return num;
    }

    /**
     * Whether there are any elements in the list or not
     * @return True if there are no elements in the list
     */
    public boolean isEmpty() {
        return num == 0;
    }

    /**
     * Counts the number of nodes in the linked list via linear scan. If there is a difference between the 
     * number of nodes counted and the expected number of nodes, updates the expected value
     * @return The number of nodes in the linked list
     */
    @SuppressWarnings("unused")
    private int countElements() {
        int count = 0;
        for (T elem : this) {
            ++count;
        }
        return num = count;
    }

    /**
     * Adds a new element to the front of the linked list
     * @param element The element to add to the front
     * @return The linked list with the new element added
     */
    public LinkedList<T> prepend (T element) {
        first = new Node<T>(element, null, first);
        if (first.next != null)
            first.next.setPrevious(first);
        else 
            last = first;
        ++num;
        return this;
    }

    /**
     * Adds a new element to the end of the linked list
     * @param element The element to add to the back
     * @return The linked list with the new element added
     */
    public LinkedList<T> append (T element) {
        last = new Node<T>(element, last, null);
        if (last.previous != null)
            last.previous.setNext(last);
        else
            first = last;
        ++num;
        return this;
    }

    /**
     * Deletes all elements from the linked list
     * @return True if no elements in the linked list
     */
    public LinkedList<T> clear () {
        first = null; 
        last = first;
        num = countElements();
        return this;
    }

    /**
     * Given an index, finds the node at that index in the linked list. 0 is the start, while -1 is the end
     * @param index The index of the element to find
     * @return The element found
     * @throws IndexOutOfBoundsException if index is not in range
     */
    private Node<T> find (int index) {
        index = index % num;
        int count = 0;
        Node<T> ptr = first;
        while (ptr.next != null) {
            if (index == count++)
                return ptr;
            ptr = ptr.next;
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * Given an element, finds the first node with that element in the linked list
     * @param index The element to find
     * @return The first occurrence of the element
     * @throws NoSuchElementException if element does not exist in the list
     */
    private Node<T> find (T element) {
        for (Node<T> ptr = first; ptr.next != null; ptr = ptr.next) {
            if (ptr.element.equals(element))
                return ptr;
        }
        throw new NoSuchElementException();
    }

    /**
     * Removes a given element from the list
     * @param element The element to remove
     * @return The linked list with the element removed
     * @throws NoSuchElementException if element is not in list
     */
    public LinkedList<T> remove (T element) {
        Node<T> remove = find(element);
        if (remove.next == null && remove.previous == null) 
            clear();
        if (remove.next != null)
            remove.next.setPrevious(remove.previous);
        if (remove.previous != null)
            remove.previous.setNext(remove.next);
        --num;
        return this;
    }

    /**
     * Removes an element from a list at a given index, and moves forward all the other elements to fill in
     * @param index The index to remove the element from
     * @return The linked list with the element removed
     * @throws IndexOutOfBoundsException if index is not in range
     */
    public LinkedList<T> remove (int index) {
        Node<T> remove = find(index);
        if (remove.next == null && remove.previous == null) 
            clear();
        if (remove.next != null)
            remove.next.setPrevious(remove.previous);
        if (remove.previous != null)
            remove.previous.setNext(remove.next);
        --num;
        return this;
    }

    /**
     * Inserts a given element to the list
     * @param element The element to insert
     * @return The linked list with the element added
     */
    public LinkedList<T> insert (T element) {
        return append(element);
    }

    /**
     * Inserts a given element to a specific index in the list, and pushes back all the other elements
     * @param element The element to insert
     * @param index The index to insert it at
     * @return The linked list with the element inserted
     * @throws IndexOutOfBoundsException if index is not in range
     */
    public LinkedList<T> insert (T element, int index) {
        if (isEmpty() && index == 0) 
            return append(element);
        
        Node<T> push = find(index);
        if (push.next == null) 
            return append(element);
        else if (push.previous == null)
            return prepend(element);
        else {
            push.previous.setNext(new Node<T>(element, push.previous, push)).next.setPrevious(push.previous.next);
            ++num;
            return this;
        }
    }

    /**
     * Inner class of a node of a linked list
     */
    private class Node<U> {

        // Element held within node
        private U element;

        // Address of previous and next nodes in linked list
        private Node<U> previous, next;

        /**
         * Constructor for the node
         * @param element The element held within the node
         * @param previous The previous node in the linked list
         * @param next The next node in the linked list
         */
        private Node (U element, Node<U> previous, Node<U> next) {
            this.element = element;
            this.next = next;
            this.previous = previous;

            if (previous != null)
                previous.setNext(this);
            if (next != null)
                next.setPrevious(this);
        }

        /**
         * Access the next node
         * @param element The node you want to be next
         * @return The next node once this method sets it
         */
        private Node<U> setNext(Node<U> next) {
            this.next = next;
            return next;
        }

        /**
         * Access the previous node
         * @param element The node you want to be previous
         * @return The previous ndoe once this method sets it
         */
        private Node<U> setPrevious(Node<U> previous) {
            this.previous = previous;
            return previous;
        }

    }

    /**
     * Inner class to iterate over the linked list
     */
    private class LinkedListIterator<U> implements Iterator<U> {

        // Pointer variable
        private LinkedList<U>.Node<U> ptr;

        /**
         * A constructor for the iterator for a particular linked list
         * @param lst The list to iterate over
         */
        public LinkedListIterator(LinkedList<U> lst) {
            ptr = lst.first;
        }

        /**
         * Checks if more nodes in the list
         * @return True if more nodes in the list
         */
        @Override
        public boolean hasNext() {
            return ptr != null;
        }

        /**
         * Returns the element in the node, and moves on to next node in list
         * @return The element in the list
         * @throw NoSuchElementException if no more elements in list
         */
        @Override
        public U next() {
            if (hasNext()) {
                U element = ptr.element;
                ptr = ptr.next;
                return element;
            } else
                throw new NoSuchElementException();
        }
    }

}