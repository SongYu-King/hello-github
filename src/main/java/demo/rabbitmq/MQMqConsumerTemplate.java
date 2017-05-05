package demo.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;


public class MQMqConsumerTemplate {

	/**
	 * 默认最大重试次数
	 */
	public static int RETRY_MAX_TIMES = 5;

	/**
	 * 默认重试间隔秒数
	 */
	public static int RETRY_INTERVAL_SECONDS = 10;

	/**
	 * 是否自动确认
	 */
	private boolean autoAck;

	/**
	 * 消息queue名称
	 */
	private String queueName;

	/**
	 * 是否在接收中
	 */
	private volatile boolean reciving;

	/**
	 * 集群名称
	 */
	private String clusterName;

	/**
	 * 消息处理器
	 */
	private MQConsumerHandler handler;

	/**
	 * 发生异常时是否进行重试
	 */
	private boolean retry;

	/**
	 * 发生异常时最大重试次数
	 */
	private int retryMaxTimes;

	/**
	 * 重试间隔的秒数
	 */
	private int retryIntervalSeconds;

	/**
	 * 已重试的次数
	 */
	private int retryedTimes;

	/**
	 * 消息处理接收处理模板，默认发生异常进行重试，最大重试次数5次，重试间隔时间10s
	 * @param autoAck 是否自动确认
	 * @param queueName 队列名称
	 * @param clusterName 集群名称
	 * @param handler 消息处理器
	 */
	public MQMqConsumerTemplate(boolean autoAck, String queueName,
			String clusterName, MQConsumerHandler handler) {
		this(autoAck, queueName, clusterName, handler, true, RETRY_MAX_TIMES,
				RETRY_INTERVAL_SECONDS);
	}

	/**
	 * 消息处理接收处理模板，建议该方法只是在不需要进行重试的时候进行调用
	 * @param autoAck 是否自动确认
	 * @param queueName 队列名称
	 * @param clusterName 集群名称
	 * @param handler 消息处理器
	 * @param isRetry 发生异常时，是否进行重试，默认最大重试次数0次，重试间隔时间0s
	 */
	public MQMqConsumerTemplate(boolean autoAck, String queueName,
			String clusterName, MQConsumerHandler handler, boolean isRetry) {
		
			this(autoAck, queueName, clusterName, handler, isRetry, 0, 0);
		
	}

	public MQMqConsumerTemplate(boolean autoAck, String queueName,
			String clusterName, MQConsumerHandler handler, boolean isRetry,
			int retryMaxTimes, int retryIntervalSeconds) {
		this.autoAck = autoAck;
		this.queueName = queueName;
		this.clusterName = clusterName;
		this.handler = handler;
		this.retry = isRetry;
		this.retryMaxTimes = retryMaxTimes;
		this.retryIntervalSeconds = retryIntervalSeconds;
		this.retryedTimes = 0;
	}

	/**
	 * 开始接收MQ的消息，并把消息交由handler处理，如果{@link #autoAck}是<code>false</code>
	 * ,该方法会根据处理器处理的结果来ack<br>
	 * 该方法是阻塞的，会一直等待新的消息过来<br>
	 * 该方法不会主动退出<br>
	 * 建议采用一个独立的线程调用该方法
	 * 
	 */
	public void reciveMsg() {
		Connection connection = null;
		com.rabbitmq.client.Channel channel = null;
		reciving = true;

		try {
			connection = MQConnectFactory.getInstance().getConnect(clusterName);
			channel = connection.createChannel();
			channel.basicQos(1);
			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queueName, autoAck, consumer);
			while (reciving) {
				//消息接收设置默认timeout，避免长时间进行无效等待
				QueueingConsumer.Delivery delivery = consumer.nextDelivery(5*1000);
				//每次在正确接收消息后，默认将已重试次数进行重置
				retryedTimes = 0;
				
				//本次没有收到消息
				if(delivery == null || delivery.getBody() == null || "".equals(delivery.getBody())) {
					continue;
				}
				
				boolean handlerResult = handler.handler(new String(delivery
						.getBody()));

				// 不是自动ack的，需要手动ack
				if (!autoAck && handlerResult) {
					channel.basicAck(delivery.getEnvelope().getDeliveryTag(),
							true);
				} else if (!autoAck && !handlerResult) {
					channel.basicNack(delivery.getEnvelope().getDeliveryTag(),
							true, true);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			//先主动关闭原先的连接
			if(connection != null && connection.isOpen()) {
				try {
					connection.close();
				} catch (IOException e1) {
				}
			}
			//允许进行重试
			if(retry && retryedTimes++ < retryMaxTimes){
				System.out.println("retry" + retryedTimes);
				//先进行间隔时间的休眠，然后进行重试
				try {
					Thread.sleep(retryIntervalSeconds * 1000);
				} catch (InterruptedException e1) {
				}
				
				reciveMsg();
			}
		} finally {
			if(connection != null && connection.isOpen()) {
				try {
					connection.close();
				} catch (IOException e1) {
				}
			}
		}
		
		System.out.println("quit");
	}

	/**
	 * 停止接收消息
	 */
	public void stop() {
		reciving = false;
	}

}
