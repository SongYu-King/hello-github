package demo.rabbitmq;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MQConnectFactory {

	private static MQConnectFactory instance = new MQConnectFactory();

	/**
	 * 集群连接工厂 key:集群名称，value：对应的连接工厂
	 */
	private Map<String, ConnectionFactory> clusterConnectionMap = new HashMap<String, ConnectionFactory>();

	/**
	 * 集群地址列表 key:集群名称，value：对应的地址列表
	 */
	private Map<String, Address[]> clusterAddressMap = new HashMap<String, Address[]>();

	private MQConnectFactory() {
	}

	public static MQConnectFactory getInstance() {
		return instance;
	}

	/**
	 * 初始化连接工厂，只需要在系统启动时初始化一次OK
	 * 
	 * @param configPath
	 */
	public void init(String configPath) {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dombuilder = factory.newDocumentBuilder();
			InputStream is = MQConnectFactory.class.getResourceAsStream("/"
					+ configPath);

			if (is == null) {
				is = new FileInputStream(configPath);
			}

			Document doc = dombuilder.parse(is);
			// 顶级配置元素
			Element configure = doc.getDocumentElement();
			// cluster节点
			NodeList clusterNode = configure.getChildNodes();
			if (clusterNode != null) {
				for (int i = 0; i < clusterNode.getLength(); i++) {
					Node cluster = clusterNode.item(i);
					if (cluster.getNodeType() == Node.ELEMENT_NODE) {
						// 集群的连接工厂
						ConnectionFactory connectionFactory = new ConnectionFactory();
						// 集群名称
						String clusterName = cluster.getAttributes()
								.getNamedItem("name").getNodeValue();
						clusterConnectionMap
								.put(clusterName, connectionFactory);

						// 获取这个集群下的配置
						NodeList clusterConfigNode = cluster.getChildNodes();

						if (clusterConfigNode != null) {
							// port,如果没有配置的话，采用默认端口
							int port = 5672;
							for (int ii = 0; ii < clusterConfigNode.getLength(); ii++) {
								Node node = clusterConfigNode.item(ii);
								if (node.getNodeType() == Node.ELEMENT_NODE) {
									// 虚拟主机
									if ("vhost".equals(node.getNodeName())) {
										String vhost = node.getTextContent();
										if (null != vhost && !"".equals(vhost)) {
											connectionFactory
													.setVirtualHost(vhost);
										}
										continue;
									}
									// 登录用户
									if ("username".equals(node.getNodeName())) {
										String username = node.getTextContent();
										if (null != username
												&& !"".equals(username)) {
											connectionFactory
													.setUsername(username);
										}
										continue;
									}
									// 登录密码
									if ("password".equals(node.getNodeName())) {
										String password = node.getTextContent();
										if (null != password
												&& !"".equals(password)) {
											connectionFactory
													.setPassword(password);
										}
										continue;
									}
									// 超时时间
									if ("timeout".equals(node.getNodeName())) {
										String timeout = node.getTextContent();
										if (null != timeout
												&& !"".equals(timeout)) {
											connectionFactory
													.setConnectionTimeout(Integer
															.valueOf(timeout));
										}
										continue;
									}
									if ("port".equals(node.getNodeName())) {
										String portStr = node.getTextContent();
										if (null != portStr
												&& !"".equals(portStr)) {
											port = Integer.valueOf(portStr);
										}
										continue;
									}
									// server列表
									if ("servers".equals(node.getNodeName())) {
										Address[] address;
										NodeList serverNode = node
												.getChildNodes();
										if (serverNode != null
												&& serverNode.getLength() > 0) {
											Set<Address> addressList = new HashSet<Address>();
											for (int index = 0; index < serverNode
													.getLength(); index++) {
												Node server = serverNode
														.item(index);
												if (server.getNodeType() == Node.ELEMENT_NODE) {
													Address addr = new Address(
															server.getTextContent(),
															port);
													addressList.add(addr);
												}
											}

											address = addressList
													.toArray(new Address[addressList
															.size()]);
											clusterAddressMap.put(clusterName,
													address);
										}
									}
								}
							}
						}
					}
				}
			} else {
				System.out.println("没有发现集群节点，不能进行初始化");
				System.exit(-1);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (SAXException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * 获取集群中的一个连接
	 * @param clusterName 集群的名称
	 * @return
	 * @throws IOException 建立连接失败或配置错误
	 */
	public Connection getConnect(String clusterName) throws IOException {
		if (!clusterConnectionMap.containsKey(clusterName)) {
			throw new IOException("没有指定名称[" + clusterName + "]的集群配置");
		}
		if (!clusterAddressMap.containsKey(clusterName)) {
			throw new IOException("指定的集群[" + clusterName + "]没有配置相应的服务器");
		}

		return clusterConnectionMap.get(clusterName).newConnection(
				clusterAddressMap.get(clusterName));
	}

}
