/**
 * @file_name: FixedThreadPool.java
 */
package demo.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 王尚禹
 * @version 1.0
 * @description 固定大小的线程池、单任务线程池、 可变尺寸的线程池、延迟连接池、单任务延迟连接池
 * @date 下午5:40:29
 */
public class FixedThreadPool {

    /**
     * constructor
     */
    public FixedThreadPool() {
    }

    /**
     * @param args
     * @description
     */
    public static void main(String[] args) {
        // 创建一个可重用固定线程数的线程池
        // ExecutorService pool = Executors.newFixedThreadPool(6);
        // 创建一个使用单个 worker 线程的 Executor，以无界队列方式来运行该线程。
        // ExecutorService pool = Executors.newSingleThreadExecutor();
        // 创建一个可根据需要创建新线程的线程池，但是当以前构造的线程可用时将重用它们。
        // ExecutorService pool = Executors.newCachedThreadPool();
        // 创建一个线程池，它可安排在给定延迟后运行命令或者定期地执行。
        // ScheduledExecutorService pool = Executors.newScheduledThreadPool(3);
        // 创建一个单线程执行程序，它可安排在给定延迟后运行命令或者定期地执行。
        ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();

        // 创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
        Thread t1 = new ThreadTask();
        Thread t2 = new ThreadTask();
        Thread t3 = new ThreadTask();
        Thread t4 = new ThreadTask();
        Thread t5 = new ThreadTask();
        // 将线程放入池中进行执行
        pool.execute(t1);
        pool.execute(t2);
        pool.execute(t3);
        // pool.execute(t4);
        // pool.execute(t5);
        // 使用延迟执行风格的方法
        pool.schedule(t4, 10, TimeUnit.MILLISECONDS);
        pool.schedule(t5, 10, TimeUnit.MILLISECONDS);
        // 关闭线程池
        pool.shutdown();
    }

    static class ThreadTask extends Thread {
        /*
         * (non-Javadoc)
         *
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "正在执行。。。");
        }

    }
}
