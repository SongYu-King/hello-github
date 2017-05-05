package demo.thrift;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.thrift.transport.TSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericConnectionProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(GenericConnectionProvider.class);

	/** 服务的IP地址 */
	private String serviceIP;
	/** 服务的端口 */
	private int servicePort;

	public String getServiceIP() {
		return serviceIP;
	}

	public void setServiceIP(String serviceIP) {
		this.serviceIP = serviceIP;
	}

	public int getServicePort() {
		return servicePort;
	}

	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}

	public int getConTimeOut() {
		return conTimeOut;
	}

	public void setConTimeOut(int conTimeOut) {
		this.conTimeOut = conTimeOut;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}

	/** 连接超时配置 */
	private int conTimeOut;
	/** 可以从缓存池中分配对象的最大数量 */
	private int maxActive = GenericObjectPool.DEFAULT_MAX_ACTIVE;
	/** 缓存池中最大空闲对象数量 */
	private int maxIdle = GenericObjectPool.DEFAULT_MAX_IDLE;
	/** 缓存池中最小空闲对象数量 */
	private int minIdle = GenericObjectPool.DEFAULT_MIN_IDLE;
	/** 阻塞的最大数量 */
	private long maxWait = GenericObjectPool.DEFAULT_MAX_WAIT;
	/** 从缓存池中分配对象，是否执行PoolableObjectFactory.validateObject方法 */
	private boolean testOnBorrow = true;
	private boolean testOnReturn = GenericObjectPool.DEFAULT_TEST_ON_RETURN;
	private boolean testWhileIdle = true;
	/** 对象缓存池 */
	private GenericObjectPool objectPool = null;

	@SuppressWarnings("deprecation")
	public void init() {
		// 对象池
		objectPool = new GenericObjectPool();
		//
		objectPool.setMaxActive(maxActive);
		objectPool.setMaxIdle(maxIdle);
		objectPool.setMinIdle(minIdle);
		objectPool.setMaxWait(maxWait);
		objectPool.setTestOnBorrow(testOnBorrow);
		objectPool.setTestOnReturn(testOnReturn);
		objectPool.setTestWhileIdle(testWhileIdle);
		objectPool
				.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_BLOCK);
		ThriftPoolableObjectFactory thriftPoolableObjectFactory = new ThriftPoolableObjectFactory(
				serviceIP, servicePort, conTimeOut);
		objectPool.setFactory(thriftPoolableObjectFactory);

		logger.debug("{}:{}的thrift连接池初始化完成", serviceIP, servicePort);
	}

	public void destroy() {
		try {
			objectPool.close();
		} catch (Exception e) {
			logger.error("关闭thrift连接池异常", e);
			throw new RuntimeException("erorr destroy()", e);
		}
	}

	/**
	 * 获取连接
	 * 
	 * @return
	 */
	public TSocket getConnection() {
		try {
			TSocket socket = (TSocket) objectPool.borrowObject();
			return socket;
		} catch (Exception e) {
			throw new RuntimeException("error getConnection()", e);
		}
	}

	/**
	 * 归还连接
	 * 
	 * @param socket
	 */
	public void returnCon(TSocket socket) {
		try {
			socket.close();
			objectPool.invalidateObject(socket);
			//这里是因为在server重启后，不能自动重连，所以这里先不需要连接池了
//			objectPool.returnObject(socket);
		} catch (Exception e) {
			throw new RuntimeException("error returnCon()", e);
		}
	}
	
	/**
	 * 归还操作异常的连接
	 * @param socket
	 */
	public void returnBrokenCon(TSocket socket) {
		try {
			socket.close();
			objectPool.invalidateObject(socket);
		} catch (Exception e) {
			throw new RuntimeException("error returnBrokenCon()", e);
		}
	}

}
