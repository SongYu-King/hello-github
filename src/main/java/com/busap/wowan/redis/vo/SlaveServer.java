package com.busap.wowan.redis.vo;

import com.busap.wowan.redis.constants.Constants;

public class SlaveServer extends RedisServer {
	
	/**
	 * 该Redis是否可读
	 */
	private boolean isRead = true;
	
	/**
	 * 该Redis是否可写
	 */
	private boolean isWrite = false;
	
	private MasterServer masterServer;

	public MasterServer getMasterServer() {
		return masterServer;
	}

	public void setMasterServer(MasterServer masterServer) {
		this.masterServer = masterServer;
	}
	
	/**
	 * @param masterServer 主redis server信息
	 * @param serverName slave server名称
	 */
	public SlaveServer(MasterServer masterServer,String serverName) {
		this.masterServer = masterServer;
		this.name = this.masterServer.getName() + Constants.PATH_SPLIT + serverName;
		this.masterServer.addSlaveServer(this);
	}

	@Override
	public boolean isRead() {
		return isRead;
	}

	@Override
	public boolean isWrite() {
		return isWrite;
	}

	@Override
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	@Override
	public void setWrite(boolean isWrite) {
		this.isWrite = isWrite;
	}

}
