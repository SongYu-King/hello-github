package com.busap.wowan.redis.vo;

public abstract class RedisServer {

	String name;
	
	String serverIp;
	
	String serverPort;
	
	/**
	 * 最大空闲连接
	 */
	 int maxIdle;
	
	/**
	 * 最大活跃连接
	 */
	 int maxActive;
	
	/**
	 * 获取连接时的最大等待时间，单位：毫秒
	 */
	 int maxWait;
	
	/**
	 * 每个连接的超时连接
	 */
	 int timeout;

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	
	/**
	 * @return 该redis服务器是否允许读操作
	 */
	public abstract boolean isRead();
	
	/**
	 * 改变Redis服务器允许读操作
	 */
	public abstract void setRead(boolean isRead);
	
	/**
	 * @return 该redis服务器是否允许写操作
	 */
	public abstract boolean isWrite();
	
	/**
	 * 改变Redis服务器允许写操作
	 */
	public abstract void setWrite(boolean isWrite);
	
}
