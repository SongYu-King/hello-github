/**
 * @file_name: PipedStreamMethod.java
 */
package demo.multithread;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * @author 王尚禹
 * @version 1.0
 * @description 管道方法PipedInputStream/PipedOutputStream
 * 这个类位于java.io包中，是解决同步问题的最简单的办法，一个线程将数据写入管道，
 * 另一个线程从管道读取数据，这样便构成了一种生产者/消费者的缓冲区编程模式。
 * 在这个代码没有使用Object对象，而是简单的读写字节值，
 * 这是因为PipedInputStream/PipedOutputStream不允许传输对象
 * @date 下午9:09:13
 */
public class PipedStreamMethod {

    private PipedOutputStream pos;
    private PipedInputStream pis;
    // private ObjectOutputStream oos;
    // private ObjectInputStream ois;

    /**
     * constructor
     */
    public PipedStreamMethod() {
        try {
            pos = new PipedOutputStream();
            pis = new PipedInputStream(pos);
            // oos = new ObjectOutputStream(pos);
            // ois = new ObjectInputStream(pis);
        } catch (IOException e) {
            System.out.println(e);
        }
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
        // TODO Auto-generated method stub

    }

    class Producer extends Thread {
        public void run() {
            try {
                while (true) {
                    int b = (int) (Math.random() * 255);
                    System.out.println("Producer: a byte, the value is " + b);
                    pos.write(b);
                    pos.flush();
                    // Object o = new MyObject();
                    // oos.writeObject(o);
                    // oos.flush();
                    // System.out.println("Producer: " + o);
                }
            } catch (Exception e) {
                // System.out.println(e);
                e.printStackTrace();
            } finally {
                try {
                    pos.close();
                    pis.close();
                    // oos.close();
                    // ois.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }

    class Consumer extends Thread {
        public void run() {
            try {
                while (true) {
                    int b = pis.read();
                    System.out.println("Consumer: a byte, the value is " + String.valueOf(b));
                    // Object o = ois.readObject();
                    // if(o != null)
                    // System.out.println("Consumer: " + o);
                }
            } catch (Exception e) {
                // System.out.println(e);
                e.printStackTrace();
            } finally {
                try {
                    pos.close();
                    pis.close();
                    // oos.close();
                    // ois.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }

    // class MyObject implements Serializable {
    // }
}
