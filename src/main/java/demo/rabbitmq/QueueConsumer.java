package demo.rabbitmq;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import org.apache.commons.lang.SerializationUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 消费者：消费者可以以线程方式运行，对于不同的事件有不同的回调函数，其中最主要的是处理新消息到来的事件。 读取队列的程序端，实现了Runnable接口。
 * 
 * @author shangyu
 * 
 */
public class QueueConsumer extends EndPoint implements Runnable, Consumer {

	public QueueConsumer(String endpointName) throws IOException {
		super(endpointName);
	}

	public void run() {
		try {
			// start consuming messages. Auto acknowledge messages.
			channel.basicConsume(endPointName, true, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called when consumer is registered.
	 */
	public void handleConsumeOk(String consumerTag) {
		System.out.println("Consumer " + consumerTag + " registered");
	}

	/**
	 * Called when new message is available.
	 */
	public void handleDelivery(String consumerTag, Envelope env,
			BasicProperties props, byte[] body) throws IOException {
		Map map = (HashMap) SerializationUtils.deserialize(body);
		System.out.println("Message Number " + map.get("message number") + " received.");
	}

	public void handleCancel(String arg0) throws IOException {
	}

	public void handleCancelOk(String arg0) {
	}

	public void handleRecoverOk(String arg0) {
	}

	public void handleShutdownSignal(String arg0, ShutdownSignalException arg1) {
	}

}
