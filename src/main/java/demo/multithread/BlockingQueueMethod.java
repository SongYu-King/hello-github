/**
 * @file_name: BlockingQueueMethod.java
 */
package demo.multithread;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author 王尚禹
 * @version 1.0
 * @description 阻塞队列方法BlockingQueue它是一个已经在内部实现了同步的队列，
 * 实现方式采用的是await()/signal()方法。它可以在生成对象时指定容量大小。
 * 它用于阻塞操作的是put()和take()方法。 put()方法类似于我们上面的
 * <code>AwaitSignalMethod</code>生产者线程，容量最大时，自动阻塞。 take()方法类似于我们上面的
 * <code>AwaitSignalMethod</code>消费者线程，容量为0时，自动阻塞。
 * @date 下午8:42:50
 */
public class BlockingQueueMethod {
    private LinkedBlockingQueue<Object> queue = new LinkedBlockingQueue<Object>(
            10);
    private int MAX = 10;

    /**
     * constructor
     */
    public BlockingQueueMethod() {
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
        new BlockingQueueMethod().start();
    }

    class Producer extends Thread {
        public void run() {
            while (true) {
                // synchronized(this){
                try {
                    if (queue.size() == MAX)
                        System.out.println("warning: it's full!");
                    Object o = new Object();
                    queue.put(o);
                    System.out.println("Producer: " + o);
                } catch (InterruptedException e) {
                    System.out.println("producer is interrupted!");
                }
                // }
            }
        }
    }

    class Consumer extends Thread {
        public void run() {
            while (true) {
                // synchronized(this){
                try {
                    if (queue.size() == 0)
                        System.out.println("warning: it's empty!");
                    Object o = queue.take();
                    System.out.println("Consumer: " + o);
                } catch (InterruptedException e) {
                    System.out.println("producer is interrupted!");
                }
                // }
            }
        }
    }

}
/*
 * 你发现这个例子中的问题了吗？
 *  如果没有，我建议你运行一下这段代码，仔细观察它的输出，是不是有下面这个样子的？为什么会这样呢？
 *   ………
 *   warning: it's full!
 *   Producer: java.lang.object@4526e2a
 *   ………
 * 你可能会说这是因为put()和System.out.println()之间没有同步造成的，我也这样认为，我也这样认为，
 * 但是你把run()中的synchronized前面的注释去掉，重新编译运行，有改观吗？
 * 没有。为什么？
 * 这是因为，当缓冲区已满，生产者在put()操作时，put()内部调用了await()方法，放弃了线程的执行，
 * 然后消费者线程执行，调用take()方法，take()内部调用了signal()方法，通知生产者线程可以执行，
 * 致使在消费者的println()还没运行的情况下生产者的println()先被执行，所以有了上面的输出。
 * run()中的synchronized其实并没有起什么作用。
 * 对于BlockingQueue大家可以放心使用，这可不是它的问题，只是在它和别的对象之间的同步有问题。
 * 对于这种多重嵌套同步的问题，以后再谈吧，欢迎大家讨论啊！
 */

