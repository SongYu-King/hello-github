package com.busap.wowan.redis.choose;

import com.busap.wowan.redis.vo.RedisServer;

import java.util.List;


public interface RedisServerChooseStrategy {

    /**
     * 从待选的redis服务器中选择一个合适的redis服务器
     *
     * @param serverList 可用的redis服务器列表
     * @param key        需要操作的key
     * @return 合适的redis服务器，如果没有合适的可以返回空
     */
    public RedisServer choose(List<RedisServer> serverList, String key);

}
