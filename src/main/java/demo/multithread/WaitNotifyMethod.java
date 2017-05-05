/**
 * @file_name: WaitNotifyMethod.java
 */
package demo.multithread;

import java.util.LinkedList;

/**
 * @description ����wait()/notify()ͬ����wait()��notify()�Ǹ���Object������������
 *              Ҳ����ζ�����е�JAVA�඼����������������
 *              wait()������ʾ����������������ʱ������߻�������߳�ֹͣ�Լ���ִ�У�������
 *              ʹ�Լ����ڵȴ�״̬������һ���߳̿�ʼִ�У�
 *              notify()������ʾ��������߻�����߶Ի���������ȡ��һ����Ʒʱ��
 *              ����һ���̷߳�����ִ��֪ͨ��ͬʱ������ʹ�Լ����ڵȴ�״̬��
 * 
 * @author ������
 * @date ����6:35:13
 * @version 1.0
 */
public class WaitNotifyMethod {

	private LinkedList<Object> myList = new LinkedList<Object>();
	private int MAX = 10;

	/**
	 * constructor
	 */
	public WaitNotifyMethod() {
	}

	public void start() {
		 new Producer().start();
		 new Consumer().start();
	}

	/**
	 * @description 
	 * @param args
	 */
	public static void main(String[] args) {
		new WaitNotifyMethod().start();
	}

	class Producer extends Thread {
		public void run() {
			while (true) {
				synchronized (myList) {
					try {
						while (myList.size() == MAX) {
							System.out.println("warning: it's full!");
							myList.wait();
						}
						Object o = new Object();
						if (myList.add(o)) {
							System.out.println("Producer: " + o.toString());
							myList.notifyAll();
						}
					} catch (InterruptedException ie) {
						System.out.println("producer is interrupted!");
					}
				}
			}
		}
	}

	class Consumer extends Thread {
		public void run() {
			while (true) {
				synchronized (myList) {
					try {
						while (myList.size() == 0) {
							System.out.println("warning: it's empty!");
							myList.wait();
						}
						Object o = myList.removeLast();
						System.out.println("Consumer: " + o.toString());
						myList.notify();
					} catch (InterruptedException ie) {
						System.out.println("consumer is interrupted!");
					}
				}
			}
		}
	}
}




