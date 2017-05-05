package demo.bst;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BinarySearchTree Tester.
 *
 * @author ${USER}
 * @version 1.0
 * @since <pre>五月 5, 2017</pre>
 */
public class BinarySearchTreeTest {
    private static final Logger logger = LoggerFactory.getLogger(BinarySearchTreeTest.class);

    private BinarySearchTree<Integer, String> binarySearchTree = null;

    @Before
    public void before() throws Exception {
        binarySearchTree = new BinarySearchTree<Integer, String>();
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: put(K key, V value)
     */
    @Test
    public void testPut() throws Exception {
        logger.info("binary search tree put (K 7, V 'E')");
        binarySearchTree.put(7, "E");
        binarySearchTree.put(3, "D");
        binarySearchTree.put(6, "F");
        binarySearchTree.put(2, "G");
        binarySearchTree.put(5, "B");
        binarySearchTree.print();
    }

    /**
     * Method: size()
     */
    @Test
    public void testSize() throws Exception {
        logger.info("binary search tree size: {}", binarySearchTree.size());
    }

    /**
     * Method: get(K key)
     */
    @Test
    public void testGet() throws Exception {
        logger.info("binary search tree get key's value: {}", binarySearchTree.get(7));
    }

    /**
     * Method: select(int k)
     */
    @Test
    public void testSelect() throws Exception {
        logger.info("binary search tree select K: {}", binarySearchTree.select(1));
    }

    /**
     * Method: min()
     */
    @Test
    public void testMin() throws Exception {
        logger.info("binary search tree min K: {}", binarySearchTree.min());
    }

    /**
     * Method: max()
     */
    @Test
    public void testMax() throws Exception {
        logger.info("binary search tree max K: {}", binarySearchTree.max());
    }

    /**
     * Method: floor(K key)
     */
    @Test
    public void testFloor() throws Exception {
        logger.info("binary search tree floor K: {}", binarySearchTree.floor(8));
    }

    /**
     * Method: ceiling(K key)
     */
    @Test
    public void testCeiling() throws Exception {
        logger.info("binary search tree ceiling K: {}", binarySearchTree.ceiling(4));
    }

    /**
     * Method: rank(K key)
     */
    @Test
    public void testRank() throws Exception {
        logger.info("binary search tree rank K: {}", binarySearchTree.rank(2));
    }

    /**
     * Method: deleteMin()
     */
    @Test
    public void testDeleteMin() throws Exception {
    }

    /**
     * Method: deleteMax()
     */
    @Test
    public void testDeleteMax() throws Exception {
    }

    /**
     * Method: delete(K key)
     */
    @Test
    public void testDelete() throws Exception {
    }

    /**
     * Method: print()
     */
    @Test
    public void testPrint() throws Exception {
        binarySearchTree.print();
    }

}
