package edu.brown.cs.student.main.cache;

import java.util.HashMap;

/** Implementation of the Least Recently Used (LRU) cache class LRUCache. */
public class LRUCache<K, V> {
  private int capacity;
  private HashMap<K, Node<K, V>> cache;
  private Node<K, V> left;
  private Node<K, V> right;

  /**
   * Initialize the LRU cache of size capacity.
   *
   * @param capacity
   */
  public LRUCache(int capacity) {
    this.capacity = capacity;
    this.cache = new HashMap<K, Node<K, V>>();
    this.left = new Node<K, V>(null, null);
    this.right = new Node<K, V>(null, null);
    this.left.next = right;
    this.right.prev = left;
  }

  /**
   * Return the value corresponding to the key if the key exists, otherwise return -1.
   *
   * @param key
   * @return
   */
  public V get(K key) {
    // Return the value corresponding to the key if the key exists
    if (cache.containsKey(key)) {
      Node<K, V> node = cache.get(key);
      remove(node);
      insert(node);
      return node.val;
    }
    // Else otherwise return null.
    return null;
  }

  public void insert(Node<K, V> node) {
    Node<K, V> prev = this.right.prev;
    prev.next = node;
    node.prev = prev;
    node.next = this.right;
    this.right.prev = node;
  }

  /**
   * Update the value of the key if the key exists. Otherwise, add the key-value pair to the cache.
   * If the introduction of the new pair causes the cache to exceed its capacity, remove the least
   * recently used key.
   *
   * @param key
   * @param value
   */
  public void put(K key, V value) {
    // Update the value of the key if the key exists
    if (cache.containsKey(key)) {
      Node<K, V> node = cache.get(key);
      node.val = value;
      remove(node);
      insert(node);
      return;
    } else { // Otherwise, add the key-value pair to the cache
      Node<K, V> node = new Node<>(key, value);
      cache.put(key, node);
      insert(node);

      // If the introduction of the new pair causes the cache to exceed
      // its capacity
      if (cache.size() > capacity) {
        // Remove the least recently used key
        Node<K, V> toRemove = this.left.next;
        remove(toRemove);
        cache.remove(toRemove.key);
      }
    }
  }

  public void remove(Node<K, V> node) {
    Node<K, V> prev = node.prev;
    Node<K, V> next = node.next;
    prev.next = next;
    next.prev = prev;
  }

  // A key is considered used if a get or a put operation is called on it.

  // Ensure that get and put each run in O(1) average time complexity.

}
