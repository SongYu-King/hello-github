package com.busap.wowan.redis.vo;


import com.busap.wowan.redis.constants.Constants;

import java.util.HashSet;
import java.util.Set;

public class MasterServer extends RedisServer {

    /**
     * 该Redis是否可读
     */
    private boolean isRead = false;

    /**
     * 该Redis是否可写
     */
    private boolean isWrite = true;

    /**
     * 该redis是否是模拟
     */
    private boolean isMock = false;

    private ClusterInfo cluster;

    private Set<RedisServer> slaveServerList;

    public Set<RedisServer> getSlaveServerList() {
        return slaveServerList;
    }

    public void setSlaveServerList(Set<RedisServer> slaveServerList) {
        this.slaveServerList = slaveServerList;
    }

    public ClusterInfo getCluster() {
        return cluster;
    }

    public void setCluster(ClusterInfo cluster) {
        this.cluster = cluster;
    }


    public boolean isMock() {
        return isMock;
    }

    public void setMock(boolean isMock) {
        this.isMock = isMock;
    }

    /**
     * @param cluster    集群信息
     * @param masterName master的名称
     */
    public MasterServer(ClusterInfo cluster, String masterName) {
        this.cluster = cluster;
        this.name = cluster.getRedisClusterName() + Constants.PATH_SPLIT
                + masterName;
        this.slaveServerList = new HashSet<RedisServer>();
        this.cluster.addMasterServer(this);
    }

    /**
     * 添加slave redis到该master redis
     *
     * @param slaveServer
     */
    public void addSlaveServer(SlaveServer slaveServer) {
        this.slaveServerList.add(slaveServer);
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
