package com.company;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * LDLinkedList is a linkedlist data structure that has lazy deletion
 * strategy for node removal.
 * @param <E>   Type of the elements of the list
 */
public class LDLinkedList<E> extends AbstractList<E> implements List<E> {
    private int size = 0;
    private Node head = null;
    private Node removedHead = null, removedTail = null;

    @Override
    public boolean add(E e) {

        // If there exists a removed node
        if(removedHead != null){
            // New removed list head
            Node newRemovedHead = removedHead.getNext();

            // Set value and link to head
            removedHead.setValue(e);
            removedHead.setNext(head);

            // Update head
            head = removedHead;

            // Update removed list head
            removedHead = newRemovedHead;
        }
        else{
            // Update head
            head = new Node(e, head);
        }

        // New element added
        ++size;
        return true;
    }

    @Override
    public E remove(int index) {
        Node removed;

        // If first element is to be removed, which is head
        if(index == 0){
            removed = head;
            head = head.getNext();
        }
        else{
            // Get to the previous node before the tail
            Node prev = new LDIter(index - 1).next;

            // So removed node can be got
            removed = prev.getNext();

            // and also be removed
            prev.setNext(removed.getNext());
        }

        // A node is removed
        --size;

        // Add that to removed list
        addRemoved(removed);
        return removed.getValue();
    }

    @Override
    public E get(int index) {
        if(!Util.isInRange(0, this.size - 1, index)){
            throw new IndexOutOfBoundsException();
        }
        return new LDIter(index).next();
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<E> iterator() {
        return new LDIter();
    }

    private void addRemoved(Node node) {
        // If no removed node is stored
        if(removedHead == null){
            // Let the given be the first
            removedHead = removedTail = node;
        }
        else{
            // Add to the end
            removedTail.setNext(node);
            removedTail = removedTail.getNext();
        }
        removedTail.setNext(null);
    }

    /**
     * Node class is element container class for LDLinkedlist
     */
    private class Node {
        private E value;
        private Node next = null;

        /**
         * Constructs this node with given value
         * @param value Value of this newly constructed node
         */
        private Node(E value) {
            this.value = value;
        }

        /**
         * Constructs this node with given value and given next node
         * @param value Value of this newly constructed node
         * @param next  Next node linked to this node
         */
        private Node(E value, Node next) {
            this.value = value;
            this.next = next;
        }

        /**
         * Returns value of this node
         * @return  Value of this node
         */
        private E getValue() {
            return value;
        }

        /**
         * Returns next node of this node
         * @return  Next node that is linked to this node
         */
        private Node getNext() {
            return next;
        }

        /**
         * Sets value of this node
         * @param  value    New value of this node
         */
        private void setValue(E value) {
            this.value = value;
        }

        /**
         * Sets next node of this node
         * @param  next New next node that will be linked to this node
         */
        private void setNext(Node next) {
            this.next = next;
        }
    }

    /**
     * LDIter is the iterator class of LDLinkedlist class
     */
    private class LDIter implements Iterator<E> {
        private Node next;
        private LDIter() {
            this(head);
        }

        /**
         * Construct this iterator at the given index
         * @param index Index of the first
         *              element that will be returned upon
         *              call of function <code>next</code>
         */
        private LDIter(int index) {
            this();
            while(index-- > 0) next();
        }

        /**
         * Constructs this iterator with given node
         * as its first value to be returned upon
         * <code>next</code> call.
         * @param node  First element to be returned upon
         *              <code>next</code> call.
         */
        private LDIter(Node node) {
            next = node;
        }

        /**
         * Checks if there exist a next node to iterate
         * @return  <code>true</code> if there exist a next
         *          node to iterate.
         */
        @Override
        public boolean hasNext() {
            return next != null;
        }

        /**
         * Returns the next element
         * @return  Next element
         */
        @Override
        public E next() {
            if(next == null){
                throw new NoSuchElementException();
            }
            Node temp = next;
            next = next.getNext();
            return temp.getValue();
        }
    }
}
