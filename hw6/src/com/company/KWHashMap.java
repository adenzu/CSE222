package com.company;

/**
 * Hashmap.
 * @param <K> Key class.
 * @param <V> Value class.
 */
public interface KWHashMap<K, V> {
    /**
     * Returns the value of the entry with given key.
     * @param key   Key of the entry whose value will be returned.
     * @return      Value of the entry, <code>null</code> if no such entry was found.
     */
    V get(K key);

    /**
     * Checks if hashmap is empty.
     * @return  <code>true</code> if empty, <code>false</code> otherwise.
     */
    boolean isEmpty();

    /**
     * Puts given key and value to this hashmap, if there already exists an entry with same key change its value to
     * given value.
     * @param key   Key of the entry.
     * @param value Value of the entry.
     * @return      Old value of the entry, <code>null</code> if no such entry.
     */
    V put(K key, V value);

    /**
     * Removes the entry with same key as the given.
     * @param key   Key of the entry to be removed.
     * @return      Value of the entry to be removed. <code>null</code> if no entry was found.
     */
    V remove(K key);

    /**
     * @return Size of this hashmap.
     */
    int size();
}
