package demo.bst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 二叉查找树
 * Created by wangshangyu on 2017/5/4.
 */
public class BinarySearchTree<K extends Comparable<K>, V> {

    private static final Logger logger = LoggerFactory.getLogger(BinarySearchTree.class);

    /**
     * 基本结点实现
     */
    private Node root;

    private class Node {
        private K key;
        private V value;
        private Node left;
        private Node right;
        private int N;

        public Node(K key, V value, int n) {
            this.key = key;
            this.value = value;
            this.N = n;
        }
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null)
            return 0;
        return x.N;
    }


    /**
     * 查找键获取值——get
     */
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node root, K key) {
        if (root == null)
            return null;

        int c = key.compareTo(root.key);
        if (c == 0)
            return root.value;
        else if (c < 0)
            return get(root.left, key);
        else
            return get(root.right, key);
    }


    /**
     * 修改值/插入新值——put
     */
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private Node put(Node root, K key, V value) {
        if (root == null)
            return new Node(key, value, 1);

        int c = key.compareTo(root.key);
        if (c == 0)
            root.value = value;
        else if (c < 0)
            root.left = put(root.left, key, value);
        else
            root.right = put(root.right, key, value);

        root.N = size(root.left) + size(root.right) + 1;

        return root;
    }


    /**
     * 最大值/最小值——min/max
     */
    public K min() {
        return min(root).key;
    }

    private Node min(Node root) {
        if (root.left == null)
            return root;

        return min(root.left);
    }

    public K max() {
        return max(root).key;
    }

    private Node max(Node root) {
        if (root.right == null)
            return root;

        return max(root.right);
    }


    /**
     * 向上/向下取整——floor/ceiling
     */
    public K floor(K key) {
        Node x = floor(root, key);
        if (x == null)
            return null;
        return x.key;
    }

    private Node floor(Node root, K key) {
        if (root == null)
            return null;

        int c = key.compareTo(root.key);
        if (c < 0)
            return floor(root.left, key);
        else if (c > 0 && root.right != null && key.compareTo(min(root.right).key) >= 0)
            return floor(root.right, key);
        else
            return root;
    }

    public K ceiling(K key) {
        Node x = ceiling(root, key);
        if (x == null)
            return null;
        return x.key;
    }

    private Node ceiling(Node root, K key) {
        if (root == null)
            return null;

        int c = key.compareTo(root.key);
        if (c > 0)
            return ceiling(root.right, key);
        else if (c < 0 && root.left != null && key.compareTo(max(root.left).key) >= 0)
            return ceiling(root.left, key);
        else
            return root;
    }


    /**
     * 选择——select
     * <p>
     * 找出BST中序号为k的键
     */
    public K select(int k) {
        // 找出BST中序号为k的键
        return select(root, k);
    }

    private K select(Node root, int k) {
        if (root == null)
            return null;

        int c = k - size(root.left);
        if (c < 0)
            return select(root.left, k);
        else if (c > 0)
            return select(root.right, k - (size(root.left) + 1));
        else
            return root.key;
    }


    /**
     * 排名——rank
     * <p>
     * 找出BST中键为key的序号是多少
     */
    public int rank(K key) {
        // 找出BST中键为key的序号是多少
        return rank(root, key);
    }

    private int rank(Node root, K key) {
        if (root == null)
            return 0;

        int c = key.compareTo(root.key);
        if (c == 0)
            return size(root.left);
        else if (c < 0)
            return rank(root.left, key);
        else
            return 1 + size(root.left) + rank(root.right, key);
    }


    /**
     * 删除最小/最大键——deleteMin/deleteMax
     */
    public void deleteMin() {
        root = deleteMin(root);
    }

    private Node deleteMin(Node root) {
        if (root.left == null)
            return root.right;

        root.left = deleteMin(root.left);
        root.N = size(root.left) + size(root.right) + 1;
        return root;
    }

    public void deleteMax() {
        root = deleteMax(root);
    }

    private Node deleteMax(Node root) {
        if (root.right == null)
            return root.left;

        root.right = deleteMax(root.right);
        root.N = size(root.left) + size(root.right) + 1;
        return root;
    }


    /**
     * 删除任意键——delete
     */
    public void delete(K key) {
        root = delete(root, key);
    }

    private Node delete(Node root, K key) {
        if (root == null)
            return null;

        int c = key.compareTo(root.key);
        if (c == 0) {
            if (root.right == null)
                return root = root.left;
            if (root.left == null)
                return root = root.right;

            Node t = root;
            root = min(t.right);

            root.left = t.left;
            root.right = deleteMin(t.right);
        } else if (c < 0)
            root.left = delete(root.left, key);
        else
            root.right = delete(root.right, key);

        root.N = size(root.left) + size(root.right) + 1;
        return root;
    }


    /**
     * 中序打印树——print
     */
    public void print() {
        print(root);
    }

    private void print(Node root) {
        if (root == null)
            return;

        print(root.left);
        logger.info("{}", root.key);
        print(root.right);
    }
}
