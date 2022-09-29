package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data structure consisting of nodes that may or may not have other nodes on their left and right.
 * @param <E>   Type of elements contained in the tree.
 */
public class BinaryTree<E> {
    /**
     * Root of the tree.
     */
    protected Node<E> root;

    /**
     * Default constructor.
     */
    public BinaryTree() {}

    /**
     * Merges two trees with the given value at the top.
     * @param data      Value to be at the top.
     * @param leftTree  Left sub tree of this tree.
     * @param rightTree Right sub tree of this tree.
     */
    public BinaryTree(E data, BinaryTree<E> leftTree, BinaryTree<E> rightTree) {
        root = new Node<>(data, leftTree.root, rightTree.root);
    }

    /**
     * Constructs a tree with given node as root.
     * @param root  New tree's root.
     */
    protected BinaryTree(Node<E> root) {
        this.root = root;
    }

    /**
     * Checks if tree is empty.
     * @return  <code>true</code> if binary tree is empty, <code>false</code> otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * @return Left sub tree.
     */
    public BinaryTree<E> getLeftSubtree() {
        return new BinaryTree<>(root.getLeft());
    }

    /**
     * @return Right sub tree.
     */
    public BinaryTree<E> getRightSubtree() {
        return new BinaryTree<>(root.getRight());
    }

    /**
     * @return  The value of the top node in the tree.
     */
    public E getData() {
        return root.getData();
    }

    /**
     * @return  <code>String</code> representation of the tree.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for(StringBuilder line : toString(root)){
            result.append(line);
            result.append("\n");
        }

        return result.toString();
    }

    /**
     * The class that contains element values.
     * @param <E>   Type of the element.
     */
    protected static class Node<E> {
        /**
         * Data that is hold by this node.
         */
        protected E data;

        /**
         * Left and right nodes.
         */
        protected Node<E> left, right;

        /**
         * Constructs the node with given item.
         * @param data  Item to contain.
         */
        protected Node(E data) {
            this.data = data;
        }

        /**
         * Constructs the node with given item.
         * @param data  Item to contain.
         * @param left  Left node.
         * @param right Right node.
         */
        protected Node(E data, Node<E> left, Node<E> right) {
            this(data);
            this.left = left;
            this.right = right;
        }

        /**
         * Checks if this node has left node.
         * @return  <code>true</code> if this node has left node, <code>false</code> otherwise.
         */
        protected boolean hasLeft() {
            return left != null;
        }

        /**
         * Checks if this node has right node.
         * @return  <code>true</code> if this node has right node, <code>false</code> otherwise.
         */
        protected boolean hasRight() {
            return right != null;
        }

        /**
         * Checks if node is leaf.
         * @return  <code>true</code> if it is, <code>false</code> otherwise.
         */
        protected boolean isLeaf() {
            return left == null && right == null;
        }

        /**
         * Generates <code>String</code> representation of this node.
         * @return  <code>String</code> representation of this node.
         */
        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    ", left=" + (left == null ? null : left.data) +
                    ", right=" + (right == null ? null : right.data) +
                    '}';
        }

        /**
         * @return  Data of this node.
         */
        protected E getData() {
            return data;
        }

        /**
         * @return  Left node of this node.
         */
        protected Node<E> getLeft() {
            return left;
        }

        /**
         * @return  Right node of this node.
         */
        protected Node<E> getRight() {
            return right;
        }

        /**
         * Sets data of this node to given data.
         * @param data  New data value for this node.
         */
        protected void setData(E data) {
            this.data = data;
        }

        /**
         * Sets left node of this node to given node.
         * @param left  New left node for this node.
         */
        protected void setLeft(Node<E> left) {
            this.left = left;
        }

        /**
         * Sets right node of this node to given node.
         * @param right  New right node value for this node.
         */
        protected void setRight(Node<E> right) {
            this.right = right;
        }
    }

    private List<StringBuilder> toString(Node<E> root) {
        if(root == null) return new ArrayList<>(Collections.singleton(new StringBuilder("null")));

        List<StringBuilder> result = new ArrayList<>(Collections.singleton(new StringBuilder().append(root.data)));

        for(StringBuilder line : toString(root.left)){
            result.add(line.insert(0, "  "));
        }

        for(StringBuilder line : toString(root.right)){
            result.add(line.insert(0, "  "));
        }

        return result;
    }
}
