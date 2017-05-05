//package demo.rabbitmq;
//
//import com.rabbitmq.client.*;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class APILearn {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		AMQP.BasicProperties.Builder bob = new AMQP.BasicProperties.Builder();
//		AMQP.BasicProperties minBasic = bob.build();
//		AMQP.BasicProperties minPersistentBasic = bob.deliveryMode(2).build();
//		AMQP.BasicProperties persistentBasic = bob.priority(0).contentType("application/octet-stream").build();
//		AMQP.BasicProperties persistentTextPlain = bob.contentType("text/plain").build();
//
//		ConnectionFactory factory = new ConnectionFactory();
//		factory.setUsername(userName);
//		factory.setPassword(password);
//		factory.setVirtualHost(virtualHost);
//		factory.setHost(hostName);
//		factory.setPort(portNumber);
//		Connection conn = factory.newConnection();
//
//		ConnectionFactory factory = new ConnectionFactory();
//		factory.setUri("amqp://userName:password@hostName:portNumber/virtualHost");
//		Connection conn = factory.newConnection();
//
//		Channel channel = conn.createChannel();
//		channel.close();
//		conn.close();
//
//		ExecutorService es = Executors.newFixedThreadPool(20);
//		Connection conn = factory.newConnection(es);
//
//		Address[] addrArr = new Address[]{ new Address(hostname1, portnumber1)  , new Address(hostname2, portnumber2)};
//		Connection conn = factory.newConnection(addrArr);
//
////		import com.google.appengine.api.ThreadManager;
//		ConnectionFactory cf = new ConnectionFactory();
//		cf.setThreadFactory(ThreadManager.backgroundThreadFactory());
//
//		channel.exchangeDeclare(exchangeName, "direct", true);
//		String queueName = channel.queueDeclare().getQueue();
//		channel.queueBind(queueName, exchangeName, routingKey);
//
//		channel.exchangeDeclare(exchangeName, "direct", true);
//		channel.queueDeclare(queueName, true, false, false, null);
//		channel.queueBind(queueName, exchangeName, routingKey);
//
//		byte[] messageBodyBytes = "Hello, world!".getBytes();
//		channel.basicPublish(exchangeName, routingKey, null, messageBodyBytes);
//
//		channel.basicPublish(exchangeName, routingKey, mandatory,
//                MessageProperties.PERSISTENT_TEXT_PLAIN,
//                messageBodyBytes);
//
//		channel.basicPublish(exchangeName, routingKey,
//                new AMQP.BasicProperties.Builder().contentType("text/plain").deliveryMode(2)
//                  .priority(1).userId("bob".build()), messageBodyBytes);
//
//        boolean autoAck = false;
//        channel.basicConsume(queueName, autoAck, "myConsumerTag",
//             new DefaultConsumer(channel) {
//                 @Override
//                 public void handleDelivery(String consumerTag,
//                                            Envelope envelope,
//                                            AMQP.BasicProperties properties,
//                                            byte[] body)
//                     throws IOException
//                 {
//                     String routingKey = envelope.getRoutingKey();
//                     String contentType = properties.contentType;
//                     long deliveryTag = envelope.getDeliveryTag();
//                     // (process the message components here ...)
//                     channel.basicAck(deliveryTag, false);
//                 }
//             });
//	}
//}
