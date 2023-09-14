import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Data structure stores a collection of non-overlapping sets. Uses a
 * hash map as a backend. Does not allow duplicate elements to exist.
 */
public class DisjointSet<T> {
    
    // set containing the list of all roots of sets, or the set itself if the set has only one element
    private HashMap<SetElement,SetElement> nodes;

    /**
     * Constructor for the disjoint set. Creates a new and empty set.
     */
    public DisjointSet() {
        nodes = new HashMap<>();
    }

    /**
     * Constructor for the disjoint set. Multiple elements taken as input,
     * and all elements are in their own nonoverlapping set
     * @param collection containing individual sets
     */
    public DisjointSet(Collection<? extends T> collection) {
        nodes = new HashMap<>(collection  
            .stream()
            .map(element -> new SetElement(element))
            .collect(Collectors.toMap(Function.identity(), Function.identity()))
        );
    }

    /**
     * Makes a new set with the element as its only member
     * @param element the lone element of the new set
     * @return true if new set is created
     */
    public boolean makeSet(T element) {
        return nodes.putIfAbsent(new SetElement(element), new SetElement(element)) == null ? true : false;
    }

    /**
     * Finds an element that serves as the representative for a set of elements,
     * which includes this element
     * @param element an element in a set
     * @return the element that serves as the set representative for this element, or null if element DNE
     */
    public T find(T element) {
        SetElement wrap = nodes.get(new SetElement(element));
        if (wrap == null) 
            return null;

        SetElement pointer = wrap;
        while (pointer.parent != pointer)
            pointer = pointer.parent;

        SetElement represent = pointer;
        while (pointer.parent != pointer) {
            wrap = pointer;
            pointer = pointer.parent;
            wrap.setParent(represent);
        }

        return represent.element;
    }

    /**
     * Finds the union of two sets by merging them, determining the representative of 
     * the following set using set ranks
     * @param one an element in the first set
     * @param two an element in the second set
     * @return the representative of the union set
     */
    public T union(T one, T two) {
        one = find(one);
        SetElement setOne = nodes.get(new SetElement(one));
        two = find(two);
        SetElement setTwo = nodes.get(new SetElement(two));
        if (setOne.equals(setTwo)) {
            return one;
        } else if (setOne.rank > setTwo.rank) {
            setTwo.setParent(setOne);
            return one;
        } else if (setTwo.rank > setOne.rank) {
            setOne.setParent(setTwo);
            return two;
        } else {
            setTwo.setParent(setOne);
            setOne.rank++;
            return one;
        }
    }

    /**
     * Makes a new set with several elements already in it. If a duplicate element already exists, 
     * then that element is not added
     * @deprecated this function sucks, and could lead to a few bugs and false results
     * @param collection containing elements in the new disjoint set
     * @return true if new set is created
     */
    @Deprecated
    public boolean makeSetWithElements(Collection<? extends T> collection) {
        if (collection.isEmpty()) return false;
        SetElement represent = new SetElement(collection.stream().findFirst().get());
        collection.stream()
            .map(element -> new SetElement(element).setParent(represent))
            .map(element -> nodes.putIfAbsent(element, element));
        return true;
    }

    /**
     * A private class that wraps each element in the set
     */
    private class SetElement {

        // contains the wrapped element
        private T element;

        // the parent pointer in a set
        private SetElement parent;

        // the upper bound for the element's height in a set
        private int rank;

        /**
         * Constructs the wrapper for the element
         * @param element to wrap
         */
        private SetElement(T element) {
            this.element = element;
            this.parent = this;
            this.rank = 0;
        }

        /**
         * Defines the parent of this element given it is in a set with the parent
         * @param parent
         * @return this set element
         */
        private SetElement setParent(SetElement parent) {
            this.parent = parent;
            return this;
        }

        /**
         * Determines whether the set element is equal to another or not
         * @param o the object to determine if it is equal
         * @return true if the object is equal
         */
        public boolean equals(Object o) {
            if (o instanceof DisjointSet<?>.SetElement) {
                DisjointSet<?>.SetElement s = (DisjointSet<?>.SetElement)o;
                return this.element.equals(s.element);
            }
            return false;
        }

    }

}
