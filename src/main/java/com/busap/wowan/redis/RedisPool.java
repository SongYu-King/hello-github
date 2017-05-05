package com.busap.wowan.redis;

import com.busap.wowan.redis.exception.ClusterNotServerException;
import com.busap.wowan.redis.vo.ClusterInfo;
import com.busap.wowan.redis.vo.MasterServer;
import com.busap.wowan.redis.vo.RedisServer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;


public class RedisPool {

    private static RedisPool pool = new RedisPool();

    protected static RedisPool getInstance() {
        return pool;
    }

    /**
     * 每个redis服务器对应的连接池，key为服务器的名称，值为该服务器的redis连接池
     */
    private Map<String, JedisPool> jedisPoolMap = new HashMap<String, JedisPool>();

    private Map<String, ClusterInfo> clusterMap;

    /**
     * 连接池是否初始化完成
     */
    private boolean inited = false;

    /**
     * 根据配置信息建立redis池
     *
     * @param clusterMap 集群配置信息
     */
    protected void buildPool(Map<String, ClusterInfo> clusterMap) {

        if (this.clusterMap == null) {
            this.clusterMap = clusterMap;
        } else {
            for (Map.Entry<String, ClusterInfo> entry : clusterMap.entrySet()) {
                if (this.clusterMap.containsKey(entry.getKey())) {
                    throw new RuntimeException("发现重复的Redis集群");
                }
                this.clusterMap.put(entry.getKey(), entry.getValue());
            }
        }

        for (Map.Entry<String, ClusterInfo> cluster : clusterMap.entrySet()) {
            for (MasterServer master : cluster.getValue().getMasterServerList()) {
                // 该redis是否是一个模拟的，如果是模拟的，就不生成连接池
                if (!master.isMock()) {
                    JedisPoolConfig masterPoolConfig = new JedisPoolConfig();

//					masterPoolConfig.setMaxActive(master.getMaxActive());
                    masterPoolConfig.setMaxIdle(master.getMaxIdle());
//					masterPoolConfig.setMaxWait(master.getMaxWait());
                    masterPoolConfig.setMinEvictableIdleTimeMillis(1000 * 1000);

                    JedisPool masterPool = new JedisPool(masterPoolConfig,
                            master.getServerIp(), Integer.valueOf(master
                            .getServerPort()), master.getTimeout());

                    jedisPoolMap.put(master.getName(), masterPool);
                }

                if (master.getSlaveServerList() != null
                        && master.getSlaveServerList().size() > 0) {
                    for (RedisServer slave : master.getSlaveServerList()) {
                        JedisPoolConfig slavePoolConfig = new JedisPoolConfig();

//						slavePoolConfig.setMaxActive(slave.getMaxActive());
                        slavePoolConfig.setMaxIdle(slave.getMaxIdle());
//						slavePoolConfig.setMaxWait(slave.getMaxWait());
                        slavePoolConfig
                                .setMinEvictableIdleTimeMillis(1000 * 1000);

                        JedisPool slavePool = new JedisPool(slavePoolConfig,
                                slave.getServerIp(), Integer.valueOf(slave
                                .getServerPort()), slave.getTimeout());

                        jedisPoolMap.put(slave.getName(), slavePool);
                    }
                }
            }
        }

        inited = true;
    }

    /**
     * 获得可写的redis server
     *
     * @param clusterName 集群名称
     * @param key         要操作的key
     * @return 操作的redis server
     */
    protected RedisServer getWriteServer(String clusterName, String key) {
        if (!inited) {
            throw new ClusterNotServerException("请先初始化集群，该集群未初始化");
        }

        ClusterInfo clusterInfo = clusterMap.get(clusterName);
        ;

        if (clusterInfo == null) {
            throw new ClusterNotServerException("没有找到名称为：" + clusterName
                    + "的集群");
        }
        Set<MasterServer> masterList = clusterInfo.getMasterServerList();

        if (masterList == null || masterList.size() == 0) {
            throw new ClusterNotServerException("集群名称为[" + clusterName
                    + "]下没有可写的master");
        }

        // 需要检测这些master是否有模拟的节点,需要将模拟的server踢出
        List<RedisServer> availableMasterList = new ArrayList<RedisServer>();
        for (MasterServer masterServer : masterList) {
            if (!masterServer.isMock()) {
                availableMasterList.add(masterServer);
            }
        }

        if (availableMasterList.size() == 0) {
            throw new ClusterNotServerException("集群名称为[" + clusterName
                    + "]下没有可用的master，该集群不允许写");
        }

        // 通过选择器来决定使用哪个redis
        RedisServer server = clusterInfo.getServerChooseStrategy().choose(
                availableMasterList, key);

        return server;

    }

    /**
     * 从集群中获取一个可读的redis
     *
     * @param clusterName 集群名称
     * @param key         操作的key
     * @return 可读的redis
     */
    protected RedisServer getReadServer(String clusterName, String key) {
        if (!inited) {
            throw new ClusterNotServerException("请先初始化集群，该集群未初始化");
        }

        ClusterInfo clusterInfo = clusterMap.get(clusterName);

        if (clusterInfo == null) {
            throw new ClusterNotServerException("没有找到名称为：" + clusterName
                    + "的集群");
        }
        // 该集群的master列表
        Set<MasterServer> masterList = clusterInfo.getMasterServerList();

        if (masterList == null || masterList.size() == 0) {
            throw new ClusterNotServerException("集群名称为[" + clusterName
                    + "]下没有可写的master");
        }

        // 可读的服务器列表
        /**
         * 为了达到读写分离的目的 主redis默认是不可读，如果其下没有可读的服务器列表，该主redis才可读
         */
        List<RedisServer> availableReadList = new ArrayList<RedisServer>();

        for (MasterServer masterServer : masterList) {
            Set<RedisServer> slaveServerList = masterServer
                    .getSlaveServerList();
            // 该master下没有slave
            if (slaveServerList == null || slaveServerList.size() == 0) {
                // 如果该master不是mock的，就说明该master是一个可读的
                // 将他加入到可读的列表中
                if (!masterServer.isMock()) {
                    availableReadList.add(masterServer);
                    continue;
                }
            } else {
                // 将所有的slave加入到可读的列表中
                availableReadList.addAll(slaveServerList);
            }
        }

        if (availableReadList.size() == 0) {
            throw new ClusterNotServerException("集群名称为[" + clusterName
                    + "]下没有可用的可读的redis，该集群不允许读");
        }

        // 通过选择器来决定使用哪个redis
        RedisServer server = clusterInfo.getServerChooseStrategy().choose(
                availableReadList, key);

        return server;
    }

    /**
     * 根据相应的redis server name来获取连接
     *
     * @param serverName redis server name
     * @return redis连接
     */
    protected Jedis getJedisByServerName(String serverName) {
        return jedisPoolMap.get(serverName).getResource();
    }

    /**
     * 释放redis连接
     *
     * @param serverName redis服务器名称
     * @param jedis      jedis连接
     */
    protected void releaseJedis(String serverName, Jedis jedis) {
        jedisPoolMap.get(serverName).returnBrokenResource(jedis);
    }

    /**
     * 释放在操作redis时发生错误的连接（彻底）
     *
     * @param serverName redis服务器名称
     * @param jedis      jedis连接
     */
    protected void releaseBrokenJedis(String serverName, Jedis jedis) {
        jedisPoolMap.get(serverName).returnBrokenResource(jedis);
    }

}
