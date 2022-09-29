package com.company;

import java.util.Arrays;

/**
 * A hashmap class implemented with chain method using binary search trees for chaining.
 * @param <K>   Key class that must extend Comparable.
 * @param <V>   Value class.
 */
public class HashMapChain<K extends Comparable<K>, V> implements KWHashMap<K, V>{
    private static final float LOAD_THRESHOLD = 3f;
    private static final int INITIAL_CAPACITY = 31;
    private BinarySearchTree<Entry<K, V>>[] data;
    private int size;

    /**
     * Constructs the hashmap with the default capacity 31.
     */
    @SuppressWarnings("unchecked")
    public HashMapChain() {
        data = new BinarySearchTree[INITIAL_CAPACITY];
        Arrays.setAll(data, i -> new BinarySearchTree<>());
    }

    /**
     * Returns the corresponding value to the given key.
     * @param key   Key object to get the corresponding value.
     * @return      Value of the entry whose key is equal to given key.
     */
    @Override
    public V get(K key) {
        Entry<K, V> entry = data[hash(key)].find(new Entry<>(key, null));

        if(entry == null) return null;
        else return entry.value;
    }

    /**
     * Checks if this hashmap is empty or not.
     * @return  <code>true</code> if this hashmap is empty, <code>false</code> otherwise.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Puts given key and value objects as a pair in this hashmap.
     * @param key   Key to get the value.
     * @param value Value to be stored.
     * @return      The old value of given key, if there was no such thing then <code>null</code>.
     */
    @Override
    public V put(K key, V value) {
        if(size > data.length * LOAD_THRESHOLD) expand();
        if(data[hash(key)].add(new Entry<>(key, value))){
            size++;
            return null;
        }
        else{ // If couldn't be added due to existing key
            Entry<K, V> entry = data[hash(key)].find(new Entry<>(key, null));
            V oldValue  = entry.value;
            entry.value = value;
            return oldValue;
        }
    }

    /**
     * Removes the entry (key and value pair) from this hashmap that has the equal key as given.
     * @param key   Equal key of the entry that is about to be removed.
     * @return      The value of the entry that was removed, <code>null</code> if removal wasn't successful.
     */
    @Override
    public V remove(K key) {
        if(size < data.length * LOAD_THRESHOLD * 0.25f) shrink();
        Entry<K, V> entry = data[hash(key)].delete(new Entry<>(key, null));
        if(entry == null) return null;
        else{
            size--;
            return entry.value;
        }
    }

    /**
     * Returns size of this hashmap.
     * @return  Size of this hashmap.
     */
    @Override
    public int size() {
        return size;
    }

    private int hash(K key) {
        int hash = key.hashCode() % data.length;
        if(hash < 0) hash += data.length;
        return hash;
    }

    private void expand() {
        rehash(data.length * 2 + 1);
    }

    private void shrink() {
        rehash((data.length - 1) / 2);
    }

    /**
     * Resizes the array and re-hashes the entries.
     * @param newCapacity   New size for the array.
     */
    @SuppressWarnings("unchecked")
    private void rehash(int newCapacity) {
        BinarySearchTree<Entry<K, V>>[] oldData = data;

        data = new BinarySearchTree[(int) Util.findPrimeLessNearest(newCapacity + 1)];
        Arrays.setAll(data, i -> new BinarySearchTree<>());

        size = 0;
        for(BinarySearchTree<Entry<K, V>> binarySearchTree : oldData) {
            for(Entry<K, V> entry : binarySearchTree) {
                if(entry != null) put(entry.key, entry.value);
            }
        }
    }

    /**
     * Entries that hold key and value objects. Used to store object pairs in HashMapChain class.
     * @param <K>   Key class that must extend Comparable.
     * @param <V>   Value class.
     */
    private static class Entry<K extends Comparable<K>, V> implements Comparable<Entry<K, V>> {
        private K key;
        private V value;

        /**
         * Constructs an Entry with given key and value.
         * @param key   Key of this entry.
         * @param value Value corresponding to key.
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * @return  Key of this entry.
         */
        public K getKey() {
            return key;
        }

        /**
         * @return  Value of this entry.
         */
        public V getValue() {
            return value;
        }

        /**
         * Sets this entry's value to given value.
         * @param value New value for this entry.
         */
        public void setValue(V value) {
            this.value = value;
        }

        /**
         * Compares this entry to another entry. Which in result compares their keys.
         * @param other The object to be compared.
         * @return      A negative integer, zero, or a positive integer as this entry's key is less than, equal to, or
         *              greater than the specified entry's key.
         */
        @Override
        public int compareTo(Entry<K, V> other) {
            return key.compareTo(other.key);
        }
    }
}
