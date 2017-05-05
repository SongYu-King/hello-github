package com.busap.wowan.redis.vo;

import com.busap.wowan.redis.choose.RedisServerChooseStrategy;

import java.util.HashSet;
import java.util.Set;


public class ClusterInfo {

    /**
     * redis集群名称,方便在一个配置文件中配置多个集群
     */
    private String redisClusterName;

    /**
     * 在这个集群中的master redis的列表
     */
    private Set<MasterServer> masterServerList;

    /**
     * 最大空闲连接
     */
    private int maxIdle;

    /**
     * 最大活跃连接
     */
    private int maxActive;

    /**
     * 获取连接时的最大等待时间，单位：毫秒
     */
    private int maxWait;

    /**
     * 每个连接的超时连接
     */
    private int timeout;

    /**
     * redis服务器选择策略
     */
    private RedisServerChooseStrategy serverChooseStrategy;

    public RedisServerChooseStrategy getServerChooseStrategy() {
        return serverChooseStrategy;
    }

    public void setServerChooseStrategy(
            RedisServerChooseStrategy serverChooseStrategy) {
        this.serverChooseStrategy = serverChooseStrategy;
    }

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

    public ClusterInfo(String redisClusterName) {
        this.redisClusterName = redisClusterName;
        this.masterServerList = new HashSet<MasterServer>();
    }

    public Set<MasterServer> getMasterServerList() {
        return masterServerList;
    }

    public void setMasterServerList(Set<MasterServer> masterServerList) {
        this.masterServerList = masterServerList;
    }

    public String getRedisClusterName() {
        return redisClusterName;
    }

    public void setRedisClusterName(String redisClusterName) {
        this.redisClusterName = redisClusterName;
    }

    public void addMasterServer(MasterServer masterServer) {
        this.masterServerList.add(masterServer);
    }
}
