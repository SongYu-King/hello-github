package demo.rabbitmq;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * 测试类:先运行一个消费者线程，然后开始产生大量的消息，这些消息会被消费者取走。
 * 
 * @author shangyu
 * 
 */
public class Main {
	public Main() throws Exception {

		QueueConsumer consumer = new QueueConsumer("queue");
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();

		Producer producer = new Producer("queue");

		for (int i = 0; i < 100000; i++) {
			HashMap message = new HashMap();
			message.put("message number", i+"");
			producer.sendMessage(message);
			System.out.println("Message Number " + i + " sent.");
		}
	}

	/**
	 * @param args
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {
		new Main();
	}
}
