/**
 * @file_name: TestThreadPool.java
 */
package com.demo.threadpool;

import java.io.Serializable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 王尚禹
 * @version 1.0
 * @description 自定义类型的线程池
 * @date 下午4:40:51
 */
public class TestThreadPool {

    private static int produceTaskSleepTime = 2;
    private static int consumeTaskSleepTime = 3000;
    private static int produceTaskMaxNumber = 15;

    /**
     * constructor
     */
    public TestThreadPool() {
    }

    /**
     * @param args
     * @description
     */
    public static void main(String[] args) {
        int corePoolSize = 2;
        int maximumPoolSize = 9;
        int keepAliveTime = 2;
        TimeUnit unit = TimeUnit.SECONDS;
        // 线程池所使用的缓冲队列
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(6);
        // 线程池对拒绝任务的处理策略
        RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardOldestPolicy();
        // 构造一个线程池
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        for (int i = 0; i < produceTaskMaxNumber; i++) {
            try {
                // 产生一个任务，并将其加入到线程池
                String task = "task@" + i;
                System.out.println("put " + task);
                threadPool.execute(new ThreadTask(task));
                // 便于观察，等待一段时间
                Thread.sleep(produceTaskSleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 关闭线程池
        threadPool.shutdown();
    }

    /**
     * @author 王尚禹
     * @version 1.0
     * @description 线程池执行的任务
     * @date 下午5:13:08
     */
    public static class ThreadTask implements Runnable, Serializable {

        /**
         * Serialization
         */
        private static final long serialVersionUID = -1656816705530506564L;
        // 保存任务所需要的数据
        private Object threadTaskData;

        /**
         * @param threadTaskData
         */
        public ThreadTask(Object threadTask) {
            super();
            this.threadTaskData = threadTask;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            // 处理一个任务，打印一条语句
            System.out.println("start …" + threadTaskData);
            try {
                // 便于观察，等待一段时间
                Thread.sleep(consumeTaskSleepTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            threadTaskData = null;
        }

        public Object getTask() {
            return this.threadTaskData;
        }
    }

}

/*
 * 说明：
 * 1、在这段程序中，一个任务就是一个Runnable类型的对象，也就是一个ThreadPoolTask类型的对象。
 * 2、一般来说任务除了处理方式外，还需要处理的数据，处理的数据通过构造方法传给任务。
 * 3、在这段程序中，main()方法相当于一个残忍的领导，他派发出许多任务，丢给一个叫 threadPool的任劳任怨的小组来做。
 * 这个小组里面队员至少有两个，如果他们两个忙不过来， 任务就被放到任务列表里面。
 * 如果积压的任务过多，多到任务列表都装不下(超过3个)的时候，就雇佣新的队员来帮忙。但是基于成本的考虑，不能雇佣太多的队员， 至多只能雇佣 4个。
 * 如果四个队员都在忙时，再有新的任务， 这个小组就处理不了了，任务就会被通过一种策略来处理，我们的处理方式是不停的派发，
 * 直到接受这个任务为止(更残忍！呵呵)。 因为队员工作是需要成本的，如果工作很闲，闲到
 * 3SECONDS都没有新的任务了，那么有的队员就会被解雇了，但是，为了小组的正常运转，即使工作再闲，小组的队员也不能少于两个。
 *
 * 4、通过调整 produceTaskSleepTime和 consumeTaskSleepTime的大小来实现对派发任务和处理任务的速度的控制，
 * 改变这两个值就可以观察不同速率下程序的工作情况。 5、通过调整4中所指的数据，再加上调整任务丢弃策略，
 * 换上其他三种策略，就可以看出不同策略下的不同处理方式。 6、对于其他的使用方法，参看jdk的帮助，很容易理解和使用。
 */


