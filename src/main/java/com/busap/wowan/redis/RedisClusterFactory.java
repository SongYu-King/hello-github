package com.busap.wowan.redis;

import com.busap.wowan.redis.vo.ClusterInfo;
import com.busap.wowan.redis.vo.RedisServer;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisClusterFactory {

    /**
     * 初始化redis集群池
     *
     * @param redisConf 配置文件 <li>直接使用文件名，但是该文件必须在classpath中</li> <li>直接使用配置文件的绝对路径</li>
     * @return 初始化是否成功
     */
    public static boolean init(String redisConf) {
        boolean result = false;

        // 从配置文件中解析集群信息
        Map<String, ClusterInfo> clusterInfo = ClusterConfigParse
                .loadConf(redisConf);

        RedisPool.getInstance().buildPool(clusterInfo);

        result = true;

        return result;
    }

    /**
     * 将key和value对应。如果key已经存在了，它会被覆盖，而不管它是什么类型。
     *
     * @param clusterName 集群名称
     * @param key
     * @param value
     * @throws JedisException 一般会出现在jedis连接上
     */
    public static void set(String clusterName, String key, String value) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            jedis.set(key, value);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回key的value。如果key不存在，返回null。如果key的value不是string，
     * 就返回null，因为GET只处理string类型的values。
     *
     * @param clusterName 集群名称
     * @param key
     * @return
     * @throws JedisException 一般会出现在jedis连接上
     */
    public static String get(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.get(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回key是否存在。
     *
     * @param key
     * @return
     * @throws JedisException 一般会出现在jedis连接上
     */
    public static Boolean exists(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.exists(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }

    }

    /**
     * 返回key存储value的类型，包括none,string, list, set, zset and hash
     * 如果key不存在返回none,返回null是出现了异常
     *
     * @param clusterName
     * @param key
     * @return
     * @throws JedisException 一般会出现在jedis连接上
     */
    public static String type(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.type(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }

    }

    /**
     * 设置key的过期时间。如果key已过期，将会被自动删除。 设置了过期时间的key被称之为volatile。
     * 在key过期之前可以重新更新他的过期时间，也可以使用PERSIST命令删除key的过期时间。
     *
     * @param clusterName
     * @param key
     * @param seconds     超时的时间，单位：秒
     * @return 1 如果设置了过期时间；0 如果没有设置过期时间，或者不能设置过期时间
     * @throws JedisException 一般会出现在jedis连接上
     */
    public static Long expire(String clusterName, String key, int seconds) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.expire(key, seconds);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 设置key在什么时间过期。
     *
     * @param clusterName
     * @param key
     * @param unixTime    unix的时间戳，以秒为单位
     * @return 1 如果设置了过期时间；0 如果没有设置过期时间，或者不能设置过期时间
     * @throws JedisException 一般会出现在jedis连接上
     */
    public static Long expireAt(String clusterName, String key, long unixTime) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.expireAt(key, unixTime);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回key剩余的时间
     *
     * @param clusterName
     * @param key
     * @return 剩余的秒数。-2 key不存在；-1 没有过期时间
     * @throws JedisException 一般会出现在jedis连接上
     */
    public static Long ttl(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.ttl(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 设置或者清空key的value(字符串)在offset处的bit值。
     * 那个位置的bit要么被设置，要么被清空，这个由value（只能是0或者1）来决定。 当key不存在的时候，就创建一个新的字符串value。
     * 要确保这个字符串大到在offset处有bit值。参数offset需要大于等于0，
     * 并且小于232(限制bitmap大小为512)。当key对应的字符串增大的时候， 新增的部分bit值都是设置为0。<br>
     * <note>警告</note>：当set最后一个bit(offset等于232-1)并且key还没有一个字符串value或者其
     * value是个比较小的字符串时，Redis需要立即分配所有内存，这有可能会导致服务阻塞一会。 在一台2010MacBook
     * Pro上，offset为232-1（分配512MB）需要～300ms，
     * offset为230-1(分配128MB)需要～80ms，offset为228-1（分配32MB）需要～30ms，
     * offset为226-1（分配8MB）需要8ms。注意，一旦第一次内存分配完， 后面对同一个key调用SETBIT就不会预先得到内存分配
     *
     * @param clusterName
     * @param key
     * @param offset
     * @param value
     * @return 在offset处原来的bit值
     * @throws JedisException 一般会出现在jedis连接上
     */
    public static Boolean setbit(String clusterName, String key, long offset,
                                 boolean value) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.setbit(key, offset, value);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回key对应的string在offset处的bit值 当offset超出了字符串长度的时候， 这个字符串就被假定为由0比特填充的连续空间。
     * 当key不存在的时候，它就认为是一个空字符串，所以offset总是超出范围， 然后value也被认为是由0比特填充的连续空间。到内存分配。
     *
     * @param clusterName
     * @param key
     * @param offset
     * @return 在offset处的bit值
     * @throws JedisException 一般会出现在jedis连接上
     */
    public static Boolean getbit(String clusterName, String key, long offset) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.getbit(key, offset);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 这个命令的作用是覆盖key对应的string的一部分，从指定的offset处开始，覆盖value的长度。
     * 如果offset比当前key对应string还要长
     * ，那这个string后面就补0以达到offset。不存在的keys被认为是空字符串，所以这个命令可以确保key有一个足够大的字符串
     * ，能在offset处设置value。<br>
     * <br>
     * 注意，offset最大可以是229-1(536870911),因为redis字符串限制在512M大小。如果你需要超过这个大小，你可以用多个keys
     * 。<br>
     * <br>
     * 警告：当set最后一个字节并且key还没有一个字符串value或者其value是个比较小的字符串时，Redis需要立即分配所有内存，
     * 这有可能会导致服务阻塞一会。在一台2010MacBook
     * Pro上，set536870911字节（分配512MB）需要～300ms，set134217728字节
     * (分配128MB)需要～80ms，set33554432比特位
     * （分配32MB）需要～30ms，set8388608比特（分配8MB）需要8ms。注意
     * ，一旦第一次内存分配完，后面对同一个key调用SETRANGE就不会预先得到内存分配
     *
     * @param key
     * @param offset
     * @param value
     * @return 该命令修改后的字符串长度
     * @throws JedisException 一般会出现在jedis连接上
     */
    public static Long setrange(String clusterName, String key, long offset,
                                String value) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.setrange(key, offset, value);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回key对应的字符串value的子串，这个子串是由start和end位移决定的（两者都在string内）。
     * 可以用负的位移来表示从string尾部开始数的下标。所以-1就是最后一个字符，-2就是倒数第二个，以此类推。
     * 这个函数处理超出范围的请求时，都把结果限制在string内。
     *
     * @param clusterName
     * @param key
     * @param startOffset
     * @param endOffset
     * @return key对应的字符串value的子串
     * @throws JedisException 一般会出现在jedis连接上
     */
    public static String getrange(String clusterName, String key,
                                  long startOffset, long endOffset) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.getrange(key, startOffset, endOffset);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 自动将key对应到value并且返回原来key对应的value。如果key存在但是对应的value不是字符串，就返回错误。
     *
     * @param clusterName
     * @param key
     * @param value
     * @return
     * @throws JedisException 一般会出现在jedis连接上
     */
    public static String getSet(String clusterName, String key, String value)
            throws JedisException {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.getSet(key, value);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 如果key不存在，就设置key对应字符串value。在这种情况下，该命令和SET一样。 当key已经存在时，就不做任何操作。
     *
     * @param clusterName
     * @param key
     * @param value
     * @return 1 如果key被set;0 如果key没有被set
     */
    public static Long setnx(String clusterName, String key, String value) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.setnx(key, value);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 设置key对应字符串value，并且设置key在给定的seconds时间之后超时过期。这个操作时原子的
     *
     * @param clusterName
     * @param key
     * @param seconds     超时秒数
     * @param value
     * @return
     */
    public static String setex(String clusterName, String key, int seconds,
                               String value) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.setex(key, seconds, value);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 将key对应的数字减decrement。如果key不存在，操作之前，key就会被置为0。
     * 如果key的value类型错误或者是个不能表示成数字的字符串，就返回错误。 这个操作最多支持64位有符号的正型数字。这个是原子性的操作
     *
     * @param key
     * @param integer 需要减少的值
     * @return 减少之后的value值
     */
    public static Long decrBy(String clusterName, String key, long integer) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.decrBy(key, integer);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 对key对应的数字做减1操作。如果key不存在，那么在操作之前，这个key对应的值会被置为0。
     * 如果key有一个错误类型的value或者是一个不能表示成数字的字符串，就返回错误。 这个操作最大支持在64位有符号的整型数字。
     *
     * @param clusterName
     * @param key
     * @return 减少后的值
     */
    public static Long decr(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.decr(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 将key对应的数字加integer。如果key不存在，操作之前，key就会被置为0。
     * 如果key的value类型错误或者是个不能表示成数字的字符串，就返回错误。 这个操作最多支持64位有符号的正型数字。 该操作是原子性
     *
     * @param clusterName
     * @param key
     * @param integer     要增加的值
     * @return 增加后的值
     */
    public static Long incrBy(String clusterName, String key, long integer) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.incrBy(key, integer);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 对key对应的数字做加1操作。如果key不存在，那么在操作之前，这个key对应的值会被置为0.
     * 如果key有一个错误类型的value或者是一个不能表示成数字的字符串，就返回错误。 这个操作最大支持在64位有符号的整型数字。<br>
     * <br>
     * 该操作是原子性的
     *
     * @param clusterName
     * @param key
     * @return 增加后的值
     */
    public static Long incr(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.incr(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 如果 key 已经存在，并且值为字符串，那么这个命令会把 value 追加到原来值（value）的结尾。 如果 key
     * 不存在，那么它将首先创建一个空字符串的key，再执行追加操作，这种情况 APPEND 将类似于 SET 操作。
     *
     * @param clusterName
     * @param key
     * @param value
     * @return append后字符串值（value）的长度
     */
    public static Long append(String clusterName, String key, String value) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.append(key, value);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 设置 key 指定的哈希集中指定字段的值。如果 key 指定的哈希集不存在， 会创建一个新的哈希集并与 key
     * 关联。如果字段在哈希集中存在，它将被重写。
     *
     * @param clusterName
     * @param key
     * @param field
     * @param value
     * @return 1如果field是一个新的字段;0如果field原来在map里面已经存在
     */
    public static Long hset(String clusterName, String key, String field,
                            String value) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * key 指定的哈希集中该字段所关联的值
     *
     * @param clusterName
     * @param key
     * @param field
     * @return 该字段所关联的值。当字段不存在或者 key 不存在时返回nil。
     */
    public static String hget(String clusterName, String key, String field) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.hget(key, field);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 只在 key 指定的哈希集中不存在指定的字段时，设置字段的值。 <li>如果 key 指定的哈希集不存在，会创建一个新的哈希集并与 key 关联。
     * </li> <li>如果字段已存在，该操作无效果</li>
     *
     * @param clusterName
     * @param key
     * @param field
     * @param value
     * @return 1：如果字段是个新的字段，并成功赋值;0：如果哈希集中已存在该字段，没有操作被执行
     */
    public static Long hsetnx(String clusterName, String key, String field,
                              String value) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.hsetnx(key, field, value);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 设置 key 指定的哈希集中指定字段的值。该命令将重写所有在哈希集中存在的字段。 如果 key 指定的哈希集不存在，会创建一个新的哈希集并与
     * key 关联
     *
     * @param clusterName
     * @param key
     * @param hash
     * @return redis状态码
     */
    public static String hmset(String clusterName, String key,
                               Map<String, String> hash) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.hmset(key, hash);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回 key 指定的哈希集中指定字段的值。 对于哈希集中不存在的每个字段，返回 nil 值。
     * 因为不存在的keys被认为是一个空的哈希集，对一个不存在的 key 执行 HMGET 将返回一个只含有 nil 值的列表
     *
     * @param clusterName
     * @param key
     * @param fields
     * @return 含有给定字段及其值的列表，并保持与请求相同的顺序。
     */
    public static List<String> hmget(String clusterName, String key,
                                     String... fields) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            List<String> result = jedis.hmget(key, fields);

            //针对只有个1个元素的，直接判断该元素是否为空，如果为空，直接返回一个空的list
            if (result != null && result.size() == 1) {
                if (result.get(0) == null || "".equals(result.get(0))) {
                    return null;
                }
            }

            return result;
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 增加 key 指定的哈希集中指定字段的数值。 如果 key 不存在，会创建一个新的哈希集并与 key 关联。
     * 如果字段不存在，则字段的值在该操作执行前被设置为 0. HINCRBY 支持的值的范围限定在 64位 有符号整数
     *
     * @param clusterName
     * @param key
     * @param field
     * @param value
     * @return 增值操作执行后的该字段的值。
     */
    public static Long hincrBy(String clusterName, String key, String field,
                               long value) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.hincrBy(key, field, value);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回字段是否是 key 指定的哈希集中存在的字段。
     *
     * @param clusterName
     * @param key
     * @param field
     * @return
     */
    public static Boolean hexists(String clusterName, String key, String field) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.hexists(key, field);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 从 key 指定的哈希集中移除指定的域。在哈希集中不存在的域将被忽略。 如果 key
     * 指定的哈希集不存在，它将被认为是一个空的哈希集，该命令将返回0。
     *
     * @param clusterName
     * @param key
     * @param field
     * @return 返回从哈希集中成功移除的域的数量，不包括指出但不存在的那些域
     */
    public static Long hdel(String clusterName, String key, String... field) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.hdel(key, field);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回 key 指定的哈希集包含的字段的数量。
     *
     * @param clusterName
     * @param key
     * @return 哈希集中字段的数量，当 key 指定的哈希集不存在时返回 0
     */
    public static Long hlen(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.hlen(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回 key 指定的哈希集中所有字段的名字。
     *
     * @param clusterName
     * @param key
     * @return 哈希集中的字段列表，当 key 指定的哈希集不存在时返回空列表。
     */
    public static Set<String> hkeys(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.hkeys(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回 key 指定的哈希集中所有字段的值。
     *
     * @param clusterName
     * @param key
     * @return 哈希集中的值的列表，当 key 指定的哈希集不存在时返回空列表。
     */
    public static List<String> hvals(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.hvals(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回 key 指定的哈希集中所有的字段和值。 返回值中，每个字段名的下一个是它的值，所以返回值的长度是哈希集大小的两倍
     *
     * @param clusterName
     * @param key
     * @return 哈希集中字段和值的列表。当 key 指定的哈希集不存在时返回空列表。
     */
    public static Map<String, String> hgetAll(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.hgetAll(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 从队列的右边入队一个或多个元素
     *
     * @param clusterName
     * @param key
     * @param values
     * @return 入队后的队列长度
     */
    public static Long rpush(String clusterName, String key, String... values) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.rpush(key, values);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 从队列的左边入队一个或多个元素
     *
     * @param clusterName
     * @param key
     * @param values
     * @return 入队后的队列长度
     */
    public static Long lpush(String clusterName, String key, String... values) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.lpush(key, values);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 获得队列(List)的长度
     *
     * @param clusterName
     * @param key
     * @return
     */
    public static Long llen(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.llen(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回指定位置范围内的元素列表
     *
     * @param clusterName
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static List<String> lrange(String clusterName, String key,
                                      long start, long end) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 修剪掉指定范围内的清单
     *
     * @param clusterName
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static String ltrim(String clusterName, String key, long start,
                               long end) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.ltrim(key, start, end);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回索引位置的元素
     *
     * @param clusterName
     * @param key
     * @param index
     * @return 请求的元素，如果索引超过队列的范围返回nil
     */
    public static String lindex(String clusterName, String key, long index) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.lindex(key, index);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 设置队列里面指定位置元素的值,指定的位置超过队列的范围，就发生错误
     *
     * @param clusterName
     * @param key
     * @param index
     * @param value
     * @return
     */
    public static String lset(String clusterName, String key, long index,
                              String value) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.lset(key, index, value);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 删除队列中指定的元素
     *
     * @param clusterName
     * @param key
     * @param count       删除的次数及方向 <li>count<0 从队列的头开始删除，删除count的绝对值次</li> <li>count>0
     *                    从队列的尾部开始删除，删除count次</li> <li>count==0 删除全部</li>
     * @param value
     * @return 删除元素的个数
     */
    public static Long lrem(String clusterName, String key, long count,
                            String value) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.lrem(key, count, value);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 从队列的左边出队一个元素
     *
     * @param clusterName
     * @param key
     * @return 队列的第一个元素，如果key不存在返回nil
     */
    public static String lpop(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.lpop(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 从队列的右边出队一个元素
     *
     * @param clusterName
     * @param key
     * @return 队列的最后一个元素，如果key不存在，返回nil
     */
    public static String rpop(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.rpop(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 向set中添加成员
     *
     * @param clusterName
     * @param key
     * @param member
     * @return 1 如果key被添加;0 如果key没有被添加
     */
    public static Long sadd(String clusterName, String key, String... member) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.sadd(key, member);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 将源key中的成员移到目标key中。该方法不支持按源key与目标key不在同一个redis的操作。
     *
     * @param clusterName
     * @param srckey
     * @param dstkey
     * @param member
     * @return 1 成功；0：失败
     */
    public static Long smove(String clusterName, final String srckey, final String dstkey,
                             final String member) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, srckey);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.smove(srckey, dstkey, member);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 对key列表求交集，暂时还不支持，keys分布到不同的机器
     *
     * @param clusterName
     * @param keys
     * @return
     */
    public static Set<String> sinter(String clusterName, final String... keys) {
        if (keys == null || keys.length == 0) {
            return new HashSet<String>();
        }
        RedisServer server = RedisPool.getInstance().getReadServer(
                clusterName, keys[0]);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.sinter(keys);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 求交集并将交集结果存储到目标key中，如果目标key存在，目标key中的内容被覆盖。暂时不支持keys分布到多个机器
     *
     * @param clusterName
     * @param dstkey
     * @param keys
     * @return 交集的结果大小
     */
    public static Long sinterstore(String clusterName, final String dstkey, final String... keys) {

        if (keys == null || keys.length == 0) {
            return 0L;
        }
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, keys[0]);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.sinterstore(dstkey, keys);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 对key列表求并集，暂不支持keys分布不同机器
     *
     * @param clusterName
     * @param keys
     * @return
     */
    public static Set<String> sunion(String clusterName, final String... keys) {
        if (keys == null || keys.length == 0) {
            return new HashSet<String>();
        }
        RedisServer server = RedisPool.getInstance().getReadServer(
                clusterName, keys[0]);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.sunion(keys);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 对key列表求并集，并将结果存储到目标key中，暂不支持keys分布不同机器
     *
     * @param clusterName
     * @param dstkey
     * @param keys
     * @return
     */
    public static Long sunionstore(String clusterName, final String dstkey, final String... keys) {
        if (keys == null || keys.length == 0) {
            return 0L;
        }
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, keys[0]);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.sunionstore(dstkey, keys);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 求keys列表的差集，暂不支持keys分布到不同的机器
     *
     * @param clusterName
     * @param keys
     * @return
     */
    public static Set<String> sdiff(String clusterName, final String... keys) {
        if (keys == null || keys.length == 0) {
            return new HashSet<String>();
        }
        RedisServer server = RedisPool.getInstance().getReadServer(
                clusterName, keys[0]);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.sdiff(keys);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 求keys列表的差集并存储到目标key，暂不支持keys分布到不同的机器
     *
     * @param clusterName
     * @param dstkey
     * @param keys
     * @return
     */
    public static Long sdiffstore(String clusterName, final String dstkey, final String... keys) {
        if (keys == null || keys.length == 0) {
            return 0L;
        }
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, keys[0]);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.sdiffstore(dstkey, keys);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回该set中所有的成员列表
     *
     * @param clusterName
     * @param key
     * @return 如果该key不存在返回nil
     */
    public static Set<String> smembers(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.smembers(key);
        } catch (Exception e) {

            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 移除set中指定的成员列表，如果该成员不存在忽略，如果该key的类型不是set将发生错误
     *
     * @param clusterName
     * @param key
     * @param member
     * @return 移除的成员个数，不存在的成员不计数
     */
    public static Long srem(String clusterName, String key, String... member) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.srem(key, member);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 随机返回并删除一个成员
     *
     * @param clusterName
     * @param key
     * @return 被移除的成员，如果成员不存在返回nil
     */
    public static String spop(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.spop(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 计算该set中的成员个数
     *
     * @param clusterName
     * @param key
     * @return 成员个数，该key不存在返回0
     */
    public static Long scard(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.scard(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 判断指定的成员是否存储在key中
     *
     * @param clusterName
     * @param key
     * @param member
     * @return
     */
    public static Boolean sismember(String clusterName, String key,
                                    String member) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.sismember(key, member);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 从set中随机返回一个元素<br>
     * <note>从redis2.6开始支持随机返回多个元素，但是当前版本的jedis还不支持</note>
     *
     * @param clusterName
     * @param key
     * @return
     */
    public static String srandmember(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.srandmember(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 该命令添加指定的成员到key对应的有序集合中，每个成员都有一个分数。 你可以指定多个分数/成员组合(jedis不支持)。 <li>
     * 如果一个指定的成员已经在对应的有序集合中了，那么其分数就会被更新成最新的，并且该成员会重新调整到正确的位置，以确保集合有序。</li> <li>
     * 如果key不存在，就会创建一个含有这些成员的有序集合，就好像往一个空的集合中添加一样。</li> <li>
     * 如果key存在，但是它并不是一个有序集合，那么就返回一个错误。</li> <li>
     * 分数的值必须是一个表示数字的字符串，并且可以是double类型的浮点数。</li>
     *
     * @param clusterName
     * @param key
     * @param score
     * @param member
     * @return 添加到有序集合的成员个数，不包括只更新分数的成员
     */
    public static Long zadd(String clusterName, String key, double score,
                            String member) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zadd(key, score, member);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * @param clusterName
     * @param key
     * @param scoreMembers
     * @return
     * @see #zadd(String, String, double, String)
     */
    public static Long zadd(String clusterName, String key,
                            Map<String, Double> scoreMembers) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zadd(key, scoreMembers);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * <p>
     * 返回有序集key中，指定区间内的成员。其中成员按score值递增(从小到大)来排序。具有相同score值的成员按字典序来排列。
     * </p>
     * <p>
     * 如果你需要成员按score值递减(score相等时按字典序递减)来排列，请使用ZREVRANGE命令。
     * 下标参数start和stop都以0为底，也就是说，以0表示有序集第一个成员，以1表示有序集第二个成员，以此类推。
     * 你也可以使用负数下标，以-1表示最后一个成员，-2表示倒数第二个成员，以此类推。
     * </p>
     * <p>
     * 超出范围的下标并不会引起错误。如果start的值比有序集的最大下标还要大，或是start >
     * stop时，ZRANGE命令只是简单地返回一个空列表。
     * 另一方面，假如stop参数的值比有序集的最大下标还要大，那么Redis将stop当作最大下标来处理。
     * </p>
     * <p>
     * 可以通过使用WITHSCORES选项，来让成员和它的score值一并返回，返回列表以value1,score1, ...,
     * valueN,scoreN的格式表示，而不是value1,...,valueN。客户端库可能会返回一些更复杂的数据类型，比如数组、元组等。
     * </p>
     *
     * @param clusterName
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Set<String> zrange(String clusterName, String key,
                                     long start, long end) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 从key对应的有序集合中删除给定的成员。如果给定的成员不存在就忽略。
     *
     * @param clusterName
     * @param key
     * @param member
     * @return 返回的是从有序集合中删除的成员个数，不包括不存在的成员。
     */
    public static Long zrem(String clusterName, String key, String... member) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrem(key, member);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * <p>
     * 为有序集key的成员member的score值加上增量increment。如果key中不存在member，就在key中添加一个member，
     * score是increment（就好像它之前的score是0.0）。如果key不存在，就创建一个只含有指定member成员的有序集合。
     * </p>
     * <p>
     * 当key不是有序集类型时，返回一个错误。
     * </p>
     * <p>
     * score值必须是字符串表示的整数值或双精度浮点数，并且能接受double精度的浮点数。也有可能给一个负数来减少score的值。
     * </p>
     *
     * @param clusterName
     * @param key
     * @param score
     * @param member
     * @return member成员的新score值
     */
    public static Double zincrby(String clusterName, String key, double score,
                                 String member) {
        RedisServer server = RedisPool.getInstance().getWriteServer(
                clusterName, key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zincrby(key, score, member);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * <p>
     * 返回有序集key中成员member的排名。其中有序集成员按score值递增(从小到大)顺序排列。排名以0为底，也就是说，
     * score值最小的成员排名为0。
     * </p>
     * <p>
     * 使用ZREVRANK命令可以获得成员按score值递减(从大到小)排列的排名。
     * </p>
     *
     * @param clusterName
     * @param key
     * @param member
     * @return <li>如果member是有序集key的成员，返回member的排名的整数。</li> <li>
     * 如果member不是有序集key的成员，返回Bulk reply: nil。</li>
     */
    public static Long zrank(String clusterName, String key, String member) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrank(key, member);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * <p>
     * 返回有序集key中成员member的排名，其中有序集成员按score值从大到小排列。排名以0为底，也就是说，score值最大的成员排名为0。
     * </p>
     * <p>
     * 使用ZRANK命令可以获得成员按score值递增(从小到大)排列的排名。
     * </p>
     *
     * @param clusterName
     * @param key
     * @param member
     * @return <li>如果member是有序集key的成员，返回member的排名。整型数字。</li><li>
     * 如果member不是有序集key的成员，返回Bulk reply: nil.</li>
     */
    public static Long zrevrank(String clusterName, String key, String member) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrevrank(key, member);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回有序集key中，指定区间内的成员。其中成员的位置按score值递减(从大到小)来排列。具有相同score值的成员按字典序的反序排列。
     * 除了成员按score值递减的次序排列这一点外，ZREVRANGE命令的其他方面和ZRANGE命令一样。
     *
     * @param clusterName
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Set<String> zrevrange(String clusterName, String key,
                                        long start, long end) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 在{@link #zrange(String, String, long, long)}的基础上返回score
     *
     * @param clusterName
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Set<Tuple> zrangeWithScores(String clusterName, String key,
                                              long start, long end) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrangeWithScores(key, start, end);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 在{@link #zrevrange(String, String, long, long)}的基础上增加score
     *
     * @param clusterName
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static Set<Tuple> zrevrangeWithScores(String clusterName,
                                                 String key, long start, long end) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrevrangeWithScores(key, start, end);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回key的有序集元素个数。
     *
     * @param clusterName
     * @param key
     * @return key存在的时候，返回有序集的元素个数，否则返回0。
     */
    public static Long zcard(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zcard(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回有序集key中，成员member的score值。
     *
     * @param clusterName
     * @param key
     * @param member
     * @return 如果member元素不是有序集key的成员，或key不存在，返回nil。
     */
    public static Double zscore(String clusterName, String key, String member) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zscore(key, member);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 对队列、set、有序set排序，ASC模式
     *
     * @param clusterName
     * @param key
     * @return
     */
    public static List<String> sort(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.sort(key);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 对队列、set、有序set排序
     *
     * @param clusterName
     * @param key
     * @param sortingParameters 排序参数
     * @return
     */
    public static List<String> sort(String clusterName, String key,
                                    SortingParams sortingParameters) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.sort(key, sortingParameters);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回有序集key中，score值在min和max之间(默认包括score值等于min或max)的成员。
     * 关于参数min和max的详细使用方法，请参考ZRANGEBYSCORE命令。
     *
     * @param clusterName
     * @param key
     * @param min
     * @param max
     * @return 指定分数范围的元素个数。
     */
    public static Long zcount(String clusterName, String key, double min,
                              double max) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zcount(key, min, max);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回有序集key中，score值在min和max之间(默认包括score值等于min或max)的成员。
     * 关于参数min和max的详细使用方法，请参考ZRANGEBYSCORE命令。<br>
     * <b>可以带(符号表示没有等于</b>
     *
     * @param clusterName
     * @param key
     * @param min
     * @param max
     * @return 指定分数范围的元素个数。
     */
    public static Long zcount(String clusterName, String key, String min,
                              String max) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zcount(key, min, max);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回指定score访问内的有序集合
     *
     * @param clusterName
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Set<String> zrangeByScore(String clusterName, String key,
                                            double min, double max) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回指定score访问内的有序集合<br>
     * <b>可以带(符号表示没有等于</b>
     *
     * @param clusterName
     * @param key
     * @param min
     * @param max
     * @return
     */
    public static Set<String> zrangeByScore(String clusterName, String key,
                                            String min, String max) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrangeByScore(key, min, max);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回key的有序集合中的分数在max和min之间的所有元素（包括分数等于max或者min的元素）。与有序集合的默认排序相反，对于这个命令，
     * 元素被认为是从高分到低具有相同分数的元素按字典反序。
     *
     * @param clusterName
     * @param key
     * @param max
     * @param min
     * @return
     */
    public static Set<String> zrevrangeByScore(String clusterName, String key,
                                               double max, double min) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrevrangeByScore(key, max, min);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回指定score访问内的有序集合<br>
     *
     * @param clusterName
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public static Set<String> zrangeByScore(String clusterName, String key,
                                            double min, double max, int offset, int count) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回key的有序集合中的分数在max和min之间的所有元素（包括分数等于max或者min的元素）。与有序集合的默认排序相反，对于这个命令，
     * 元素被认为是从高分到低具有相同分数的元素按字典反序。<br>
     * <b>可以带(符号表示没有等于</b>
     *
     * @param clusterName
     * @param key
     * @param max
     * @param min
     * @return
     */
    public static Set<String> zrevrangeByScore(String clusterName, String key,
                                               String max, String min) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrevrangeByScore(key, max, min);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回指定score访问内的有序集合<br>
     * <b>可以带(符号表示没有等于</b>
     *
     * @param clusterName
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    public static Set<String> zrangeByScore(String clusterName, String key,
                                            String min, String max, int offset, int count) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrangeByScore(key, min, max, offset, count);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 返回key的有序集合中的分数在max和min之间的所有元素（包括分数等于max或者min的元素）。与有序集合的默认排序相反，对于这个命令，
     * 元素被认为是从高分到低具有相同分数的元素按字典反序。<br>
     *
     * @param clusterName
     * @param key
     * @param max
     * @param min
     * @param offset
     * @param count
     * @return
     */
    public static Set<String> zrevrangeByScore(String clusterName, String key,
                                               double max, double min, int offset, int count) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrevrangeByScore(key, max, min, offset, count);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    public static Set<Tuple> zrangeByScoreWithScores(String clusterName,
                                                     String key, double min, double max) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    public static Set<Tuple> zrevrangeByScoreWithScores(String clusterName,
                                                        String key, double max, double min) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrevrangeByScoreWithScores(key, max, min);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    public static Set<Tuple> zrangeByScoreWithScores(String clusterName,
                                                     String key, double min, double max, int offset, int count) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    public static Set<String> zrevrangeByScore(String clusterName, String key,
                                               String max, String min, int offset, int count) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrevrangeByScore(key, max, min, offset, count);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    public static Set<Tuple> zrangeByScoreWithScores(String clusterName,
                                                     String key, String min, String max) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    public static Set<Tuple> zrevrangeByScoreWithScores(String clusterName,
                                                        String key, String max, String min) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrevrangeByScoreWithScores(key, max, min);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    public static Set<Tuple> zrangeByScoreWithScores(String clusterName,
                                                     String key, String min, String max, int offset, int count) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    public static Set<Tuple> zrevrangeByScoreWithScores(String clusterName,
                                                        String key, double max, double min, int offset, int count) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset,
                    count);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    public static Set<Tuple> zrevrangeByScoreWithScores(String clusterName,
                                                        String key, String max, String min, int offset, int count) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zrevrangeByScoreWithScores(key, max, min, offset,
                    count);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 移除有序集key中，指定排名(rank)区间内的所有成员。下标参数start和stop都以0为底，0处是分数最小的那个元素。这些索引也可是负数，
     * 表示位移从最高分处开始数。例如，-1是分数最高的元素，-2是分数第二高的，依次类推。
     *
     * @param clusterName
     * @param key
     * @param start
     * @param end
     * @return 被移除成员的数量。
     */
    public static Long zremrangeByRank(String clusterName, String key,
                                       long start, long end) {
        RedisServer server = RedisPool.getInstance().getWriteServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zremrangeByRank(key, start, end);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 移除有序集key中，所有score值介于min和max之间(包括等于min或max)的成员。
     *
     * @param clusterName
     * @param key
     * @param start
     * @param end
     * @return 删除的元素的个数。
     */
    public static Long zremrangeByScore(String clusterName, String key, double start, double end) {
        RedisServer server = RedisPool.getInstance().getWriteServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zremrangeByScore(key, start, end);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * @param clusterName
     * @param key
     * @param start
     * @param end
     * @return
     * @see{@link #zremrangeByScore(String, String, double, double)},可以不包含start或end，用(符号表示
     */
    public static Long zremrangeByScore(String clusterName, String key, String start, String end) {
        RedisServer server = RedisPool.getInstance().getWriteServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.zremrangeByScore(key, start, end);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 插入一个值到指定值的前面或者后面
     *
     * @param clusterName
     * @param key
     * @param where       前或者后
     * @param pivot       位置值
     * @param value       插入值
     * @return 插入后的队列长度，-1表示位置值没有找到
     */
    public static Long linsert(String clusterName, String key, LIST_POSITION where, String pivot,
                               String value) {
        RedisServer server = RedisPool.getInstance().getWriteServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.linsert(key, where, pivot, value);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 向一个队列的头部插入一个指定值，如果key不存在，不操作。
     *
     * @param clusterName
     * @param key
     * @param value
     * @return
     */
    public static Long lpushx(String clusterName, String key, String value) {
        RedisServer server = RedisPool.getInstance().getWriteServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.lpushx(key, value);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 向一个队列的尾部插入一个指定值，如果key不存在，不操作。
     *
     * @param clusterName
     * @param key
     * @param value
     * @return
     */
    public static Long rpushx(String clusterName, String key, String value) {
        RedisServer server = RedisPool.getInstance().getWriteServer(clusterName,
                key);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.rpushx(key, value);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 根据key删除，暂不支持多个key分布到不同的redis
     *
     * @param clusterName
     * @param keys
     * @return
     */
    public static Long del(String clusterName, final String... keys) {
        RedisServer server = RedisPool.getInstance().getWriteServer(clusterName,
                keys[0]);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.del(keys);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 获取通过正则可以匹配的key
     *
     * @param clusterName 集群名称
     * @param pattern     匹配模式
     * @return
     */
    public static Set<String> keys(String clusterName, String pattern) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName,
                null);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());
            return jedis.keys(pattern);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 获取通过正则可以匹配的key对应给定的keys到他们相应的values上。
     * MSET会用新的value替换已经存在的value，就像普通的SET命令一样。<br>
     * MSET是原子的，所以所有给定的keys是一次性set的。
     * 客户端不可能看到这种一部分keys被更新而另外的没有改变的情况。
     *
     * @param clusterName 集群名称
     * @param keysvalues  键值对数组
     * @return
     */
    public static String mset(String clusterName, String... keysvalues) {
        RedisServer server = RedisPool.getInstance().getWriteServer(clusterName, keysvalues[0]);

        Jedis jedis = null;
        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());

            return jedis.mset(keysvalues);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    public static double incrByFloat(String clusterName, String key, double increment) {
        RedisServer server = RedisPool.getInstance().getWriteServer(clusterName, key);
        Jedis jedis = null;

        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());

            return jedis.incrByFloat(key, increment);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    public static double hincrByFloat(String clusterName, String key, String field, double increment) {
        RedisServer server = RedisPool.getInstance().getWriteServer(clusterName, key);
        Jedis jedis = null;

        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());

            return jedis.hincrByFloat(key, field, increment);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    public static List<String> mget(String clusterName, String... keys) {
        RedisServer server = RedisPool.getInstance().getReadServer(clusterName, keys[0]);
        Jedis jedis = null;

        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());

            return jedis.mget(keys);
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }

    /**
     * 移除Key的过期时间
     *
     * @param clusterName
     * @param key
     * @return
     */
    public static boolean persist(String clusterName, String key) {
        RedisServer server = RedisPool.getInstance().getWriteServer(clusterName, key);
        Jedis jedis = null;

        try {
            jedis = RedisPool.getInstance().getJedisByServerName(
                    server.getName());

            return jedis.persist(key).longValue() == 1;
        } catch (Exception e) {
            if (jedis != null) {
                RedisPool.getInstance().releaseBrokenJedis(server.getName(),
                        jedis);
                jedis = null;
            }
            throw new JedisException(e);
        } finally {
            if (jedis != null) {
                RedisPool.getInstance().releaseJedis(server.getName(), jedis);
            }
        }
    }
}
