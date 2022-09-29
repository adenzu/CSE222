package com.company;

import java.util.Iterator;

/**
 * A hashmap class that is implemented both using coalesced hashing and double hashing methods.
 * @param <K>   Key class.
 * @param <V>   Value class.
 */
public class HashMapHybrid<K, V> implements KWHashMap<K, V> {
    private static final float LOAD_THRESHOLD = 0.75f;
    private static final int INITIAL_CAPACITY = 31;
    private Entry<K, V>[] data;
    private Integer[] nexts;
    private int size;

    /**
     * Constructs this hashmap with default capacity 31.
     */
    @SuppressWarnings("unchecked")
    public HashMapHybrid() {
        data  = new Entry[INITIAL_CAPACITY];
        nexts = new Integer[INITIAL_CAPACITY];
    }

    /**
     * Searches and returns the value that is corresponding to the given key.
     * @param key   Key to access desired value.
     * @return      Value corresponding to the given key. <code>Null</code> if no such entry exists.
     */
    @Override
    public V get(K key) {
        // Get hash value
        int index = hash(key).next();

        // Find the entry with same key
        while(data[index] != null && !data[index].key.equals(key)){
            if(nexts[index] == null) return null;
            index = nexts[index];
        }

        // If no such entry return null
        if(data[index] == null) return null;

        return data[index].value;
    }

    /**
     * Checks if this hashmap is empty.
     * @return  <code>true</code> if this hashmap is empty, <code>false</code> otherwise.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Puts given key and value as an entry in this hashmap.
     * @param key   Key of the entry.
     * @param value Value of the entry.
     * @return      The old value of the entry with given key, <code>null</code> if there's no such entry.
     */
    @Override
    public V put(K key, V value) {
        if(size > data.length * LOAD_THRESHOLD) expand();

        // Hash value iterator
        Iterator<Integer> indexIt = hash(key);

        // Original index for this entry to be
        int index = indexIt.next();

        // If it's empty then put it
        if(data[index] == null){
            data[index] = new Entry<>(key, value);
            size++;
            return null;
        }
        // Else if it has different value change it to given value
        else if(data[index].key.equals(key)){
            V oldValue = data[index].value;
            data[index].value = value;
            return oldValue;
        }

        // If entry at the index is another entry, then iterate through to find chaining's end
        int predecessorIndex = index;
        while(nexts[predecessorIndex] != null) {
            predecessorIndex = nexts[predecessorIndex];

            // Maybe the entry is along the way
            if(data[predecessorIndex].key.equals(key)) {
                V oldValue = data[index].value;
                data[index].value = value;
                return oldValue;
            }
        }

        // Now that it is reached find a valid index to put the entry
        while(indexIt.hasNext()){
            index = indexIt.next();

            if(data[index] == null){
                data[index] = new Entry<>(key, value);
                nexts[predecessorIndex] = index;
                size++;
                return null;
            }
            else if(data[index].key.equals(key)){
                V oldValue = data[index].value;
                data[index].value = value;
                return oldValue;
            }
        }

        // If couldn't put it anywhere
        return null;
    }

    /**
     * Removes the entry with a key that is equal to the given key.
     * @param key   Key of the entry.
     * @return      Value of the entry that was removed, <code>null</code> if there's no such entry.
     */
    @Override
    public V remove(K key) {
        // Original index
        int index = hash(key).next();

        // Check there
        if(data[index] == null){
            return null;
        }
        else if(data[index].key.equals(key)){
            V value = data[index].value;
            data[index] = null;
            nexts[index] = null;
            return value;
        }

        // Iterate with coalesced chaining method
        while(nexts[index] != null){
            if(data[nexts[index]] == null) break;
            else if(data[nexts[index]].key.equals(key)) {
                V value = data[nexts[index]].value;
                data[nexts[index]] = null;
                Integer nextIndex = nexts[nexts[index]];
                nexts[nexts[index]] = null;
                nexts[index] = nextIndex;
                return value;
            }
            index = nexts[index];
        }

        return null;
    }

    /**
     * @return Size of this hashmap.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Generates the string form of this hashmap's table.
     * @return  String form of this hashmap's table.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < data.length; ++i){
            if(data[i] != null)
                stringBuilder.append(i).append(" ".repeat(5 - Integer.toString(i).length())).append(data[i].key).append(" ".repeat(5 - data[i].key.toString().length())).append(nexts[i]).append("\n");
            else
                stringBuilder.append(i).append(" ").append(nexts[i]).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Generates a hash value iterator for the given key.
     * @param key   Key whose hash value will be generated.
     * @return      Hash value iterator.
     */
    private Iterator<Integer> hash(K key) {
        int num = (int) Util.findPrimeLessNearest((data.length / 10) * 8);
        int hash1 = key.hashCode() % data.length;
        int hash2 = num - key.hashCode() % num;

        /**
         * Local iterator class just for this use.
         */
        class Hash implements Iterator<Integer> {
            private final int maxIteration;
            private int i = 1;

            public Hash(int maxIteration){
                this.maxIteration = maxIteration;
            }

            @Override
            public boolean hasNext() {
                return i < maxIteration;
            }

            @Override
            public Integer next() {
                return ((hash1 + (i++) * hash2) % data.length + data.length) % data.length;
            }
        }

        return new Hash(data.length);
    }

    private void expand() {
        rehash(data.length * 2 + 1);
    }

    private void shrink() {
        rehash((data.length - 1) / 2);
    }

    /**
     * Resizes this hashmap and re-hashes the entries in it.
     * @param newCapacity   New capacity of this hash map.
     */
    @SuppressWarnings("unchecked")
    private void rehash(int newCapacity) {
        Entry<K, V>[] oldData = data;
        data  = new Entry[(int) Util.findPrimeLessNearest(newCapacity + 1)];
        nexts = new Integer[data.length];
        size  = 0;
        for(Entry<K, V> entry : oldData){
            if(entry != null) put(entry.key, entry.value);
        }
    }

    /**
     * Entry class for this hashmap.
     * @param <K>   Key class.
     * @param <V>   Value class.
     */
    private static class Entry<K, V> {
        private K key;
        private V value;

        /**
         * Constructs an entry with given key and value.
         * @param key   Key of this entry.
         * @param value Value of this entry.
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * @return Returns key of this entry.
         */
        public K getKey() {
            return key;
        }

        /**
         * @return Returns value of this entry.
         */
        public V getValue() {
            return value;
        }

        /**
         * Sets value of this entry to given value.
         */
        public void setValue(V value) {
            this.value = value;
        }
    }
}
