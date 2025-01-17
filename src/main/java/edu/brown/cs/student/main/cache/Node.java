package edu.brown.cs.student.main.cache;

public class Node<K, V> {
  K key;
  V val;
  Node<K, V> prev;
  Node<K, V> next;

  public Node(K key, V val) {
    this.key = key;
    this.val = val;
    this.prev = null;
    this.next = null;
  }
}
