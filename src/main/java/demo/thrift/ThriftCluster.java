package demo.thrift;

import org.apache.thrift.transport.TSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * thrift连接池集群，使用前请先初始化
 * 建议一个系统中只应该存在一个
 * 
 * @version 1.0
 */
public class ThriftCluster {

	private static Random random = new Random();
	
	private static int DEFAULT_MIN_IDLE = 5;
	
	private static int DEFAULT_MAX_IDLE = 5;

	private static int DEFAULT_MAX_ACTIVE = 10;

	private static int DEFAULT_MAX_WAIT = 2000;

	private static int DEFAULT_TIMEOUT = 3000;

	private static final ThriftCluster instance = new ThriftCluster();
	
	public static ThriftCluster getInstance(){
		return instance;
	}

	private static final Logger logger = LoggerFactory
			.getLogger(ThriftCluster.class);

	private ThriftCluster() {
	}

	private Map<String, Map<String, GenericConnectionProvider>> clusterIndex = new HashMap<String, Map<String, GenericConnectionProvider>>();
	
	private boolean inited = false;

	/**
	 * 加载配置文件，同时进行初始化
	 * 
	 * @param thriftConfigFile
	 */
	public void init(String thriftConfigFile) {

		if (inited) {
			throw new RuntimeException("thrift集群信息已经被初始化，不允许重复初始化");
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder dombuilder = factory.newDocumentBuilder();

			InputStream is = ThriftCluster.class.getResourceAsStream("/"
					+ thriftConfigFile);

			if (is == null) {
				is = new FileInputStream(thriftConfigFile);
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
						// 集群名称
						String clusterName = cluster.getAttributes()
								.getNamedItem("name").getNodeValue();

						if (clusterIndex.containsKey(clusterName)) {
							logger.error("发现重复的集群名称{},重复项将被忽略", clusterName);
							continue;
						}
						// 生成thrift服务器的连接池
						Map<String, GenericConnectionProvider> thriftServerPool = new HashMap<String, GenericConnectionProvider>();
						clusterIndex.put(clusterName, thriftServerPool);

						// 获取这个集群下的pool和master列表
						NodeList clusterConfigNode = cluster.getChildNodes();

						if (clusterConfigNode != null
								&& clusterConfigNode.getLength() > 0) {

							// 该集群每个thrift连接池的默认配置
							int poolMinIdle = DEFAULT_MIN_IDLE;
							int poolMaxActive = DEFAULT_MAX_ACTIVE;
							int poolMaxIdle = DEFAULT_MAX_IDLE;
							int poolMaxWait = DEFAULT_MAX_WAIT;
							int poolTimeout = DEFAULT_TIMEOUT;
							
							for (int ii = 0; ii < clusterConfigNode.getLength(); ii++) {
								Node configNode = clusterConfigNode.item(ii);
								if (configNode.getNodeType() != Node.ELEMENT_NODE) {
									continue;
								}

								// 解析默认的thrift连接池配置
								if ("pool".equals(configNode.getNodeName())) {
									NodeList poolNode = configNode
											.getChildNodes();
									if (poolNode != null
											&& poolNode.getLength() > 0) {
										for (int poolIndex = 0; poolIndex < poolNode
												.getLength(); poolIndex++) {
											Node poolConfig = poolNode
													.item(poolIndex);
											if (poolConfig.getNodeType() != Node.ELEMENT_NODE) {
												continue;
											}

											if ("minIdle".equals(poolConfig
													.getNodeName())) {
												poolMinIdle = Integer
														.valueOf(poolConfig
																.getFirstChild()
																.getNodeValue());
												continue;
											}
											if ("maxIdle".equals(poolConfig
													.getNodeName())) {
												poolMaxIdle = Integer
														.valueOf(poolConfig
																.getFirstChild()
																.getNodeValue());
												continue;
											}
											if ("maxActive".equals(poolConfig
													.getNodeName())) {
												poolMaxActive = Integer
														.valueOf(poolConfig
																.getFirstChild()
																.getNodeValue());
												continue;
											}
											if ("maxWait".equals(poolConfig
													.getNodeName())) {
												poolMaxWait = Integer
														.valueOf(poolConfig
																.getFirstChild()
																.getNodeValue());
												continue;
											}
											if ("timeout".equals(poolConfig
													.getNodeName())) {
												poolTimeout = Integer
														.valueOf(poolConfig
																.getFirstChild()
																.getNodeValue());
												continue;
											}
										}
									}
								}

								// thrift机器连接池
								if ("server".equals(configNode.getNodeName())) {
									String address = configNode.getAttributes()
											.getNamedItem("address")
											.getNodeValue();
									String port = configNode.getAttributes()
											.getNamedItem("port")
											.getNodeValue();
									//InetSocketAddress isa = new InetSocketAddress(address, Integer.valueOf(port));
									String isa = address + port;
									
									if(thriftServerPool.containsKey(isa)) {
										logger.info("在同一个集群中出现了多个重复的thriftserver:{}:{}",address,port);
										continue;
									}

									GenericConnectionProvider connectPool = new GenericConnectionProvider();
									connectPool.setServiceIP(address);
									connectPool.setServicePort(Integer
											.valueOf(port));
									connectPool.setMaxIdle(poolMaxIdle);
									connectPool.setMaxActive(poolMaxActive);
									connectPool.setConTimeOut(poolTimeout);
									connectPool.setMaxWait(poolMaxWait);
									connectPool.setMinIdle(poolMinIdle);
									connectPool.init();
									
									thriftServerPool.put(isa, connectPool);
								}
							}

						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("thrift集群初始化失败", e);
			throw new RuntimeException(e);
		}

	}
	
	/**
	 * 获取某个集群下的连接
	 * @param clusterName 集群名称
	 * @return
	 */
	public TSocket getConnect(String clusterName){
		if(!clusterIndex.containsKey(clusterName)) {
			throw new RuntimeException("没有发现指定的集群");
		}
		
		Map<String, GenericConnectionProvider> pool = clusterIndex.get(clusterName);
		int size = pool.size();
		
		int index = random.nextInt()  % size;
		
		if(index < 0) {
			index = Math.abs(index);
		}
		
		String[] iasKey = pool.keySet().toArray(new String[size]);
		
		return pool.get(iasKey[index]).getConnection();
	}
	
	/**
	 * 归还某个集群下的连接
	 * @param clusterName 集群名称
	 * @param socket 
	 */
	public void releaseConnect(String clusterName,TSocket socket) {
		if(!clusterIndex.containsKey(clusterName)) {
			throw new RuntimeException("没有发现指定的集群");
		}
		Map<String, GenericConnectionProvider> pool = clusterIndex.get(clusterName);
		
		String address = socket.getSocket().getInetAddress().getHostAddress();
		int port = socket.getSocket().getPort();
		
		pool.get(address + port).returnCon(socket);
	}
	
	/**
	 * 归还操作时有错误的socket
	 * @param clusterName 集群名称
	 * @param socket 要归还的socket
	 */
	public void releaseBrokenConnect(String clusterName,TSocket socket) {
		if(!clusterIndex.containsKey(clusterName)) {
			throw new RuntimeException("没有发现指定的集群");
		}
		Map<String, GenericConnectionProvider> pool = clusterIndex.get(clusterName);
		
		String address = socket.getSocket().getInetAddress().getHostAddress();
		int port = socket.getSocket().getPort();
		pool.get(address + port).returnBrokenCon(socket);
	}

}
