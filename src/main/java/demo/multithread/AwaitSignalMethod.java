/**
 * @file_name: AwaitSignalMethod.java
 */
package demo.multithread;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 王尚禹
 * @version 1.0
 * @description await()和signal()同步可以实现更小粒度上的控制，
 * 功能基本上和wait()/notify()相同，完全可以取代它们，
 * 但是它们和新引入的锁定机制Lock直接挂钩，具有更大的灵活性。
 * @date 下午8:11:50
 */
public class AwaitSignalMethod {
    private LinkedList<Object> myList = new LinkedList<Object>();
    private int MAX = 10;
    private final Lock lock = new ReentrantLock();
    private final Condition full = lock.newCondition();
    private final Condition empty = lock.newCondition();

    /**
     * constructor
     */
    public AwaitSignalMethod() {
    }

    public void start() {
        new Producer().start();
        new Consumer().start();
    }

    /**
     * @param args
     * @description
     */
    public static void main(String[] args) {
        new AwaitSignalMethod().start();
    }

    class Producer extends Thread {
        public void run() {
            while (true) {
                lock.lock();
                try {
                    while (myList.size() == MAX) {
                        System.out.println("warning: it's full!");
                        full.await();
                    }
                    Object o = new Object();
                    if (myList.add(o)) {
                        System.out.println("Producer: " + o);
                        empty.signal();
                    }
                } catch (InterruptedException ie) {
                    System.out.println("producer is interrupted!");
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    class Consumer extends Thread {
        public void run() {
            while (true) {
                lock.lock();
                try {
                    while (myList.size() == 0) {
                        System.out.println("warning: it's empty!");
                        empty.await();
                    }
                    Object o = myList.removeLast();
                    System.out.println("Consumer: " + o);
                    full.signal();
                } catch (InterruptedException ie) {
                    System.out.println("consumer is interrupted!");
                } finally {
                    lock.unlock();
                }
            }
        }
    }

}
