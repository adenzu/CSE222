package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Binary search tree where every node has nodes on their left with lesser value then theirs,
 * and has nodes on their right with greater value then theirs.
 * @param <E>   Type of items.
 */
public class BinarySearchTree<E extends Comparable<E>> implements SearchTree<E> {
    private E[] data;
    private int size;

    /**
     * Default constructor.
     */
    @SuppressWarnings("unchecked")
    public BinarySearchTree() {
        data = (E[]) new Comparable[1];
    }

    /**
     * Adds given item to this tree.
     * @param item  Item to be added.
     * @return      <code>true</code> if item is added, <code>false</code> otherwise.
     */
    @Override
    public boolean add(E item) {
        int index = 0;

        while(index < data.length){
            if(data[index] == null){
                data[index] = item;
                size++;
                return true;
            }
            else if(data[index].equals(item)){
                return false;
            }
            else if(data[index].compareTo(item) > 0){
                index = 2 * index + 1;
            }
            else{
                index = 2 * index + 2;
            }
        }
        
        expand();
        data[index] = item;
        size++;
        return true;
    }

    /**
     * Checks if tree contains given item.
     * @param target    Item to be checked.
     * @return          <code>true</code> if item is found, <code>false</code> otherwise.
     */
    @Override
    public boolean contains(E target) {
        return find(target) != null;
    }

    /**
     * Finds equal item in the tree to the given item and returns it.
     * @param target    Item to be checked.
     * @return          An item equal to the given one if it's found, <code>null</code> otherwise.
     */
    @Override
    public E find(E target) {
        int index = 0;

        while(index < data.length && data[index] != null) {
            if(data[index].equals(target)){
                return data[index];
            }
            else if(data[index].compareTo(target) > 0){
                index = 2 * index + 1;
            }
            else{
                index = 2 * index + 2;
            }
        }

        return null;
    }

    /**
     * Deletes given item from the tree.
     * @param target    Item to be deleted.
     * @return          The item that is deleted or <code>null</code> if deletion was unsuccessful.
     */
    public E delete(E target) {
        E returned = deleteHelper(target, 0);
        if(returned != null) size--;
        if(shouldShrink()) shrink();
        return returned;
    }

    /**
     * Removes given item from the tree.
     * @param target    Item to be removed.
     * @return          <code>true</code> if removal was successful, <code>false</code> otherwise.
     */
    @Override
    public boolean remove(E target) {
        return delete(target) != null;
    }

    /**
     * @return Size of the tree.
     */
    public int getSize() {
        return size;
    }

    /**
     * Generates a <code>String</code> representation for the tree.
     * @return  <code>String</code> Representation.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for(StringBuilder line : toString(0)){
            result.append(line);
            result.append("\n");
        }

        return result.toString();
    }

    /**
     * Searches and deletes first instance of given item in the tree starting from given root.
     * @param target    Target to be deleted.
     * @param root      Root index of the tree.
     * @return          Deleted item, <code>null</code> if deletion was unsuccessful.
     */
    private E deleteHelper(E target, int root) {
        int index = root;

        while(index < data.length && data[index] != null){
            if(data[index].equals(target)){
                int swapIndex;
                E returned = data[index];

                if(2 * index + 1 < data.length){

                    if(data[2 * index + 1] == null){
                        swapIndex = minIndex(2 * index + 2);
                    }
                    else if(data[2 * index + 2] == null){
                        swapIndex = maxIndex(2 * index + 1);
                    }
                    else{
                        swapIndex = switch (size % 2) {
                            case 0 -> maxIndex(2 * index + 1);
                            case 1 -> minIndex(2 * index + 2);
                            default -> index;   // impossible
                        };
                    }

                    if(index == swapIndex){
                        size--;
                        data[index] = null;
                    }
                    else{
                        data[index] = deleteHelper(data[swapIndex], swapIndex);
                    }
                }
                else{
                    size--;
                    data[index] = null;
                }

                return returned;
            }
            else if(data[index].compareTo(target) > 0){
                index = 2 * index + 1;
            }
            else{
                index = 2 * index + 2;
            }
        }

        return null;
    }

    private List<StringBuilder> toString(int rootIndex) {
        if(rootIndex >= data.length) return new ArrayList<>(Collections.singleton(new StringBuilder("null")));

        List<StringBuilder> result = new ArrayList<>(Collections.singleton(new StringBuilder().append(data[rootIndex])));

        if(data[rootIndex] != null){
            List<StringBuilder> left = toString(2 * rootIndex + 1);
            for(StringBuilder line : left){
                result.add(line.insert(0, "  "));
            }

            List<StringBuilder> right = toString(2 * rootIndex + 2);
            for(StringBuilder line : right){
                result.add(line.insert(0, "  "));
            }
        }

        return result;
    }

    /**
     * Expands the <code>data</code> array.
     */
    @SuppressWarnings("unchecked")
    private void expand() {
        E[] oldData = data;
        data = (E[]) new Comparable[2 * data.length + 1];
        System.arraycopy(oldData, 0, data, 0, oldData.length);
    }

    /**
     * Shrinks the <code>data</code> array.
     */
    @SuppressWarnings("unchecked")
    private void shrink() {
        E[] oldData = data;
        data = (E[]) new Comparable[(data.length - 1) / 2];
        System.arraycopy(oldData, 0, data, 0, data.length);
    }

    /**
     * Should the <code>data</code> array be shrunk.
     * @return  <code>true</code> if it should, <code>false</code> otherwise.
     */
    private boolean shouldShrink() {
        return furthestIndex(0) < data.length / 2;
    }

    /**
     * Returns the index of the node at the largest index that is not null.
     * @param root  Starting node.
     * @return      Index of node with the largest index which is not null.
     */
    private int furthestIndex(int root) {
        if(root >= data.length || data[root] == null) return (root - 1) / 2;
        return Math.max(furthestIndex(2 * root + 1), furthestIndex(2 * root + 2));
    }

    /**
     * Returns the index of rightmost node.
     * @param rootIndex Starting index.
     * @return          Index of rightmost node.
     */
    private int maxIndex(int rootIndex) {
        while(rootIndex < data.length && data[rootIndex] != null) {
            rootIndex = 2 * rootIndex + 2;
        }

        return (rootIndex - 1) / 2;
    }

    /**
     * Returns the index of leftmost node.
     * @param rootIndex Starting index.
     * @return          Index of leftmost node.
     */
    private int minIndex(int rootIndex) {
        while(rootIndex < data.length && data[rootIndex] != null) {
            rootIndex = 2 * rootIndex + 1;
        }

        return (rootIndex - 1) / 2;
    }
}
