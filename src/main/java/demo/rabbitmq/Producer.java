package demo.rabbitmq;

import org.apache.commons.lang.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;

/**
 * 生产者：生产者类的任务是向队列里写一条消息。我们使用Apache Commons Lang把可序列化的Java对象转换成 byte 数组。
 * @author shangyu
 *
 */
public class Producer extends EndPoint {

	public Producer(String endpointName) throws IOException {
		super(endpointName);
	}

	public void sendMessage(Serializable object) throws IOException {
		channel.basicPublish("", endPointName, null, SerializationUtils.serialize(object));
	}
}
