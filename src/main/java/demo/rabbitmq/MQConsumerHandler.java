package demo.rabbitmq;


public interface MQConsumerHandler {
	
	/**
	 * 处理从MQ中接收到的消息
	 * @param message 接收到的消息
	 * @return 消息处理是否成功
	 */
	public boolean handler(String message);

}
