package com.company;

/**
 * Binary heap data structure. Keeps the minimum value at root.
 * And as nodes are deeper the values are bigger.
 * @param <E>   Type of items contained.
 */
public class BinaryHeap<E extends Comparable<E>> extends BinaryTree<E>{
    /**
     * Constructs empty binary heap.
     */
    public BinaryHeap() {}

    private BinaryHeap(Node<E> root) {
        super(root);
    }

    /**
     * Returns left sub binary heap tree.
     * @return  Left sub binary heap tree.
     */
    @Override
    public BinaryHeap<E> getLeftSubtree() {
        return new BinaryHeap<>(root.left);
    }

    /**
     * Returns right sub binary heap tree.
     * @return  Right sub binary heap tree.
     */
    @Override
    public BinaryHeap<E> getRightSubtree() {
        return new BinaryHeap<>(root.right);
    }

    /**
     * Finds the element that is equal to the given one.
     * @param target    Element to be found.
     * @return          Equal element in the heap to the given. <code>null</code> if not found.
     */
    public E find(E target) {
        return find(root, target);
    }

    /**
     * Checks if given element is in heap.
     * @param target    Element to be checked.
     * @return          <code>true</code> if is in the heap, <code>false</code> otherwise.
     */
    public boolean contains(E target) {
        return find(target) != null;
    }

    /**
     * Deletes the minimum element in the heap.
     * @return  Deleted element. <code>null</code> if couldn't delete.
     */
    public E delete() {
        if(root == null) return null;
        else if(root.isLeaf()){
            E returned = root.data;
            root = null;
            return returned;
        }
        else{
            E returned = root.data;
            root.data = delete(root);
            relocate(root);
            return returned;
        }
    }

    /**
     * Adds given element to the heap if it's not already in it.
     * @param data  Element to be added.
     * @return      <code>true</code> if added, <code>false</code> otherwise.
     */
    public boolean add(E data) {
        if(contains(data)) return false;
        if(root == null){
            root = new Node<>(data);
            return true;
        }
        else return add(root, data);
    }

    /**
     * Merges given heap onto this heap.
     * @param other Other heap to merge it on this heap.
     * @return      Number of successfull element merges.
     */
    public int merge(BinaryHeap<E> other) {
        int addCount = 0;
        while(!other.isEmpty()){
            if(add(other.delete())) ++addCount;
        }
        return addCount;
    }

    /**
     * Deletes the last added element.
     * @param root  Current node. (Root of the heap to delete)
     * @return      Deleted value. <code>null</code> if couldn't delete.
     */
    private E delete(Node<E> root) {
        if(root.left.isLeaf()){
            if(root.hasRight()){
                E returned = root.right.data;
                root.right = null;
                return returned;
            }
            else{
                E returned = root.left.data;
                root.left = null;
                return returned;
            }
        }
        else if(allLeftDepth(root.left) == allLeftDepth(root.right)){
            return delete(root.right);
        }
        else{
            return delete(root.left);
        }
    }

    /**
     * Finds given element.
     * @param root      Currently checked node. (Root of the heap to find)
     * @param target    Element to be found.
     * @return          Found element. <code>null</code> if not found.
     */
    private E find(Node<E> root, E target) {
        if(root == null) return null;
        else if(root.data.compareTo(target) > 0) return null;
        else if(root.data.equals(target)) return root.data;

        if(root.hasLeft()){
            E returned = find(root.left, target);
            if(returned == null && root.hasRight()) returned = find(root.right, target);
            return returned;
        }
        else return null;
    }

    /**
     * Adds given data to this heap.
     * @param root  Current node that is iterated. (Root of the heap to add)
     * @param data  Data to be added.
     * @return      <code>true</code> if added, <code>false</code> otherwise.
     */
    private boolean add(Node<E> root, E data) {
        if(root.data.equals(data)){
            return false;
        }
        else if(root.left == null){
            if(root.data.compareTo(data) > 0){
                root.left = new Node<>(root.data);
                root.data = data;
            }
            else root.left = new Node<>(data);
            return true;
        }
        else if(root.right == null){
            if(root.data.compareTo(data) > 0){
                root.right = new Node<>(root.data);
                root.data = data;
            }
            else root.right = new Node<>(data);
            return true;
        }
        else if(allRightDepth(root.left) == allRightDepth(root.right)){
            boolean added = add(root.left, data);
            if(added && root.data.compareTo(root.left.data) > 0){
                E temp = root.data;
                root.data = root.left.data;
                root.left.data = temp;
            }
            return added;
        }
        else{
            boolean added = add(root.right, data);
            if(added && root.data.compareTo(root.right.data) > 0){
                E temp = root.data;
                root.data = root.right.data;
                root.right.data = temp;
            }
            return added;
        }
    }

    /**
     * Relocates the value at given node to the right downwards position.
     * @param root  Node to be relocated downwards.
     */
    private void relocate(Node<E> root) {
        if(root.right == null){
            if(root.hasLeft()){
                if(root.data.compareTo(root.left.data) > 0){
                    E temp = root.data;
                    root.data = root.left.data;
                    root.left.data = temp;
                    relocate(root.left);
                }
            }
        }
        else if(root.left.data.compareTo(root.right.data) > 0){
            if(root.data.compareTo(root.right.data) > 0){
                E temp = root.data;
                root.data = root.right.data;
                root.right.data = temp;
                relocate(root.right);
            }
        }
        else{
            if(root.data.compareTo(root.left.data) > 0){
                E temp = root.data;
                root.data = root.left.data;
                root.left.data = temp;
                relocate(root.left);
            }
        }
    }

    /**
     * Depth value when it is always iterated on left.
     * @param root  Root of heap.
     * @return      Depth.
     */
    private int allLeftDepth(Node<E> root) {
        if(root == null) return 0;
        return 1 + allLeftDepth(root.left);
    }

    /**
     * Depth value when it is always iterated on right.
     * @param root  Root of heap.
     * @return      Depth.
     */
    private int allRightDepth(Node<E> root) {
        if(root == null) return 0;
        return 1 + allRightDepth(root.right);
    }
}
