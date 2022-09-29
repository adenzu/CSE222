package com.company;

/**
 * Binary tree for binary search.
 * @param <E>   Type of items this tree contains.
 */
public interface SearchTree<E> {
    /**
     * Adds given item to tree.
     * @param item  Item to be added.
     * @return      <code>true</code> if addition was successful, <code>false</code> otherwise.
     */
    boolean add(E item);

    /**
     * Checks if tree contains given item.
     * @param target    Item to be checked.
     * @return          <code>true</code> if tree contains given item, <code>false</code> otherwise.
     */
    boolean contains(E target);

    /**
     * Checks if tree contains given item.
     * @param target    Item to be checked.
     * @return          Equal item if tree contains given item, <code>null</code> otherwise.
     */
    E find(E target);

    /**
     * Deletes given item from tree.
     * @param target    Item to be deleted.
     * @return          <code>true</code> if deletion was successful, <code>false</code> otherwise.
     */
    E delete(E target);

    /**
     * Removes given item from tree.
     * @param target    Item to be removed.
     * @return          <code>true</code> if removal was successful, <code>false</code> otherwise.
     */
    boolean remove(E target);
}
