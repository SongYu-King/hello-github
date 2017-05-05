package demo.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Map;


public class MQUtil {

	public enum ExchangeType {
		/**
		 * 通过routingKey和exchange决定的那个唯一的queue可以接收消息
		 */
		DIRECT("direct"),
		/**
		 * 所有bind到此exchange的queue都可以接收消息
		 */
		FANOUT("fanout"),
		/**
		 * 所有符合routingKey(此时可以是一个表达式)的routingKey所bind的queue可以接收消息
		 * 表达式符号说明：#代表一个或多个字符，*代表任何字符 例：#.a会匹配a.a，aa.a，aaa.a等 *.a会匹配a.a，b.a，c.a等
		 * 注：使用RoutingKey为#，Exchange Type为topic的时候相当于使用fanout
		 */
		TOPIC("topic");

		private String value;

		private ExchangeType(String type) {
			this.value = type;
		}

		public String getValue() {
			return this.value;
		}
	}

	private static MQUtil instance = new MQUtil();

	public static MQUtil getInstance() {
		return instance;
	}

	private MQUtil() {
	}

	/**
	 * 申明一个exchange，默认可持久,exchangeType为direct<br>
	 * <b>一个exchange申明一次即可</b>
	 * 
	 * @param clusterName
	 *            集群名称
	 * @param exchangeName
	 *            exchange名称
	 */
	public static void exchangeDeclare(String clusterName, String exchangeName)
			throws Exception {
		exchangeDeclare(clusterName, exchangeName, ExchangeType.DIRECT);
	}

	/**
	 * 申明一个exchange，默认可持久<br>
	 * <b>一个exchange申明一次即可</b>
	 * 
	 * @param clusterName
	 *            集群名称
	 * @param exchangeName
	 *            exchange名称
	 * @param exchangeType
	 *            exchange类型{@link ExchangeType}
	 */
	public static void exchangeDeclare(String clusterName, String exchangeName,
			ExchangeType exchangeType) throws Exception {
		exchangeDeclare(clusterName, exchangeName, exchangeType, true, false,
				null);
	}

	/**
	 * 申明一个exchange<br>
	 * <b>一个exchange申明一次即可</b>
	 * 
	 * @param clusterName
	 *            集群名称
	 * @param exchangeName
	 *            exchange名称
	 * @param exchangeType
	 *            exchange类型{@link ExchangeType}
	 * @param durable
	 *            是否持久
	 * @param autoDelete
	 *            是否自动删除（长期不使用）
	 * @param arguments
	 *            exchage的配置参数
	 */
	public static void exchangeDeclare(String clusterName, String exchangeName,
			ExchangeType exchangeType, boolean durable, boolean autoDelete,
			Map<String, Object> arguments) throws Exception {
		Connection connection = null;
		Channel channel = null;

		try {
			connection = MQConnectFactory.getInstance().getConnect(clusterName);
			channel = connection.createChannel();

			channel.exchangeDeclare(exchangeName, exchangeType.getValue(),
					durable, autoDelete, arguments);
		} catch (Exception e) {
			throw e;
		} finally {
			//如果申明时发生异常，会自动关闭该连接，所以这里需要判断一下是否被打开
			if (channel != null && channel.isOpen()) {
				channel.close();
			}
			if (connection != null && connection.isOpen()) {
				connection.close();
			}
		}
	}
	
	/**
	 * 删除指定的exchange
	 * @param clusterName 集群名称
	 * @param exchangeName exchange名称
	 * @throws Exception 
	 */
	public static void exchangeDelete(String clusterName,String exchangeName) throws Exception {
		Connection connection = null;
		Channel channel = null;

		try {
			connection = MQConnectFactory.getInstance().getConnect(clusterName);
			channel = connection.createChannel();

			channel.exchangeDelete(exchangeName);
		} catch (Exception e) {
			throw e;
		} finally {
			//如果申明时发生异常，会自动关闭该连接，所以这里需要判断一下是否被打开
			if (channel != null && channel.isOpen()) {
				channel.close();
			}
			if (connection != null && connection.isOpen()) {
				connection.close();
			}
		}
	}

	/**
	 * 申明queue,默认可持久<br>
	 * <b>只需要申明一次即可</b>
	 * 
	 * @see #queueDeclare(String, String, boolean, boolean, boolean, Map)
	 * @param clusterName
	 *            mq集群名称
	 * @param queueName
	 *            queue名称
	 * @throws Exception
	 */
	public static void queueDeclare(String clusterName, String queueName)
			throws Exception {
		queueDeclare(clusterName, queueName, true, false, false, null);
	}

	/**
	 * 申明queue<br>
	 * <b>只需要申明一次即可</b>
	 * 
	 * @param clusterName
	 *            mq集群名称
	 * @param queueName
	 *            queue名称
	 * @param durable
	 *            该queue是否持久化
	 * @param exclusive
	 *            该queue是否具有排他性
	 * @param autoDelete
	 *            该队列是否长期不使用，自动删除
	 * @param arguments
	 *            其他配置参数
	 * @throws IOException
	 */
	public static void queueDeclare(String clusterName, String queueName,
			boolean durable, boolean exclusive, boolean autoDelete,
			Map<String, Object> arguments) throws Exception {

		Connection connection = null;
		Channel channel = null;
		try {
			// 获取集群的连接
			connection = MQConnectFactory.getInstance().getConnect(clusterName);
			// 创建channel
			channel = connection.createChannel();
			// 申明queue
			channel.queueDeclare(queueName, durable, exclusive, autoDelete,
					arguments);
		} catch (Exception e) {
			throw e;
		} finally {
			if (channel != null && channel.isOpen()) {
				channel.close();
			}
			if (connection != null && connection.isOpen()) {
				connection.close();
			}
		}
	}
	
	/**
	 * 删除指定的queue
	 * @param clusterName 集群名称
	 * @param queueName 队列名称
	 * @throws Exception
	 */
	public static void queueDelete(String clusterName,String queueName) throws Exception {
		Connection connection = null;
		Channel channel = null;
		try {
			// 获取集群的连接
			connection = MQConnectFactory.getInstance().getConnect(clusterName);
			// 创建channel
			channel = connection.createChannel();

			channel.queueDelete(queueName);
		} catch (Exception e) {
			throw e;
		} finally {
			if (channel != null && channel.isOpen()) {
				channel.close();
			}
			if (connection != null && connection.isOpen()) {
				connection.close();
			}
		}
	}

	/**
	 * 将一个queue绑定到一个exchange，绑定一次即可
	 * @param clusterName 集群名称
	 * @param queue 队列名称
	 * @param exchange exchange名称
	 * @param routingKey 路由关键字，请注意exchange的类型
	 * @throws Exception
	 */
	public static void queueBind(String clusterName, String queue,
			String exchange, String routingKey) throws Exception {
		Connection connection = null;
		Channel channel = null;
		try {
			// 获取集群的连接
			connection = MQConnectFactory.getInstance().getConnect(clusterName);
			// 创建channel
			channel = connection.createChannel();

			channel.queueBind(queue, exchange, routingKey);
		} catch (Exception e) {
			throw e;
		} finally {
			if (channel != null && channel.isOpen()) {
				channel.close();
			}
			if (connection != null && connection.isOpen()) {
				connection.close();
			}
		}
	}

	/**
	 * 发送消息到指定的queue,该消息默认采用持久、无优先级、明文的方式发送
	 * @param clusterName 集群名称
	 * @param queueName 队列名称
	 * @param messages 消息
	 * @throws Exception 
	 */
	public static void sendMsg(String clusterName,String queueName,byte[] messages) throws Exception {
		Connection connection = null;
		Channel channel = null;
		try {
			// 获取集群的连接
			connection = MQConnectFactory.getInstance().getConnect(clusterName);
			// 创建channel
			channel = connection.createChannel();
			AMQP.BasicProperties.Builder bob = new AMQP.BasicProperties.Builder();
			AMQP.BasicProperties persistentBasic = bob.deliveryMode(2).priority(0)
					.contentType("text/plain").build();
			
			channel.basicPublish("", queueName, persistentBasic, messages);
		} catch (Exception e) {
			throw e;
		} finally {
			if (channel != null) {
				channel.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	/**
	 * 发送消息到指定的queue,该消息默认采用持久、无优先级、明文的方式发送
	 * @param clusterName 集群名称
	 * @param exchageName 交换机名称
	 * @param queueName 队列名称
	 * @param messages 消息
	 * @throws Exception 
	 */
	public static void sendMsg(String clusterName,String exchageName,String routingKey,byte[] messages) throws Exception {
		Connection connection = null;
		Channel channel = null;
		try {
			// 获取集群的连接
			connection = MQConnectFactory.getInstance().getConnect(clusterName);
			// 创建channel
			channel = connection.createChannel();
			AMQP.BasicProperties.Builder bob = new AMQP.BasicProperties.Builder();
			AMQP.BasicProperties persistentBasic = bob.deliveryMode(2).priority(0)
					.contentType("text/plain").build();
			
			channel.basicPublish(exchageName, routingKey, persistentBasic, messages);
		
		} catch (Exception e) {
			throw e;
		} finally {
			if (channel != null) {
				channel.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	

}
