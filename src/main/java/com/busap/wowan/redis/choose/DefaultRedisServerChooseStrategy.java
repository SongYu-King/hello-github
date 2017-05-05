package com.busap.wowan.redis.choose;

import com.busap.wowan.redis.vo.RedisServer;

import java.util.List;
import java.util.Random;



public class DefaultRedisServerChooseStrategy implements
		RedisServerChooseStrategy {

	private Random random = new Random();

	@Override
	public RedisServer choose(List<RedisServer> serverList, String key) {

		// 没有可用的redis
		if (serverList.size() == 0) {
			return null;
		}
		// 只有一个redis，就直接返回
		if (serverList.size() == 1) {
			return serverList.get(0);
		}
		int index = 0;
		// 不指定key，默认使用随机的一个server
		if (key == null || "".equals(key)) {
			index = random.nextInt(serverList.size());
		} else {
			// 通过要操作的key的hashCode定位
			int hashCode = key.hashCode();

			index = hashCode % serverList.size();
		}
		
		if (index < 0) {
			index = Math.abs(index);
		}

		return serverList.get(index);
	}

}
