package demo.rabbitmq;



public class TestMQ {

	public static void main(String[] args) {

		// 初始化连接工厂
		String configPath = "X:\\Private Secretary\\workspace\\rabbitmq-util\\src\\main\\resources\\rabbitmq-2.xml";
		MQConnectFactory factory = MQConnectFactory.getInstance();
		factory.init(configPath);

		String clusterName = "test_1";
		String exchangeName = "ex_1";
		String queueName = "que_1";
		String routingKey = "**";
		byte[] messages = "hello rabbit mq!".getBytes();
		try {
//			MQUtil.exchangeDelete(clusterName, exchangeName);
//			MQUtil.queueDelete(clusterName, queueName);
			MQUtil.exchangeDeclare(clusterName, exchangeName);
			MQUtil.queueDeclare(clusterName, queueName);
			MQUtil.queueBind(clusterName, queueName, exchangeName, routingKey);
			// 消息发送
			MQUtil.sendMsg(clusterName, queueName, messages);
		} catch (Exception e) {
			e.printStackTrace();
		}


		boolean autoAck = true;
		class ConsumerHandler implements MQConsumerHandler {
			@Override
			public boolean handler(String message) {
				System.out.println("receive:" + message);
				return true;
			}
		}
		MQConsumerHandler handler = new ConsumerHandler();
		MQMqConsumerTemplate consumer = new MQMqConsumerTemplate(autoAck, queueName, clusterName, handler);
		// 消息接收
		consumer.reciveMsg();

	}
}
