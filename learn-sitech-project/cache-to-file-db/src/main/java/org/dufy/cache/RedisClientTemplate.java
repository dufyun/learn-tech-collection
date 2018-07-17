package org.dufy.cache;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.params.sortedset.ZAddParams;

/**
 * Redis客户端操作工具类<br>
 *
 * 注意：JedisCluster 的info()，ping()等单机函数无法调用,返回(No way to dispatch this command to Redis Cluster)错误！
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/16
 */
public class RedisClientTemplate {

    /**
     * 使用redis集群进行操作
     */
    @Autowired
    private JedisCluster jedisCluster;


    //=============================    Key（键）=============================//

    /**
     * 检查给定 key 是否存在。
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        return jedisCluster.exists(key);
    }

    /**
     * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
     * @param key
     * @param expireSeconds
     * @return
     */
    public Boolean expire(String key, int expireSeconds) {
        jedisCluster.expire(key, expireSeconds);
        return true;
    }

    /**
     * 删除给定的一个或多个 key 。
     *
     * 不存在的 key 会被忽略。
     * @param keys
     * @return
     */
    public Long del(String... keys) {
        return jedisCluster.del(keys);
    }

    //=============================String（字符串）=============================//

    /**
     * 将字符串值 value 关联到 key 。
     *
     * 如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
     *
     * 对于某个原本带有生存时间（TTL）的键来说， 当 SET 命令成功在这个键上执行时， 这个键原有的 TTL 将被清除。
     * @param key
     * @param value
     * @return
     */
    public Boolean set(String key, String value) {
        jedisCluster.set(key,value);
        return true;
    }

    /**
     * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。
     *
     * 如果 key 已经存在， SETEX 命令将覆写旧值。
     *
     * SETEX 是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，该命令在 Redis 用作缓存时，非常实用。
     *
     * 等同于：
     * SET key value
     * EXPIRE key seconds  # 设置生存时间
     * @param key
     * @param value
     * @param expireSeconds
     * @return
     */
    public Boolean setex(String key, String value, int expireSeconds) {
        jedisCluster.setex(key, expireSeconds, value);
        return true;
    }

    /**
     * 返回 key 所关联的字符串值。
     *
     * 如果 key 不存在那么返回特殊值 nil 。
     *
     * 假如 key 储存的值不是字符串类型，返回一个错误，因为 GET 只能用于处理字符串值。
     * @param key
     * @return
     */
    public String get(String key) {
        return jedisCluster.get(key);
    }

    /**
     * 将 key 中储存的数字值增一。
     *
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * @param key
     * @return
     */
    public Long incr(String key) {
        return jedisCluster.incr(key);
    }


    //=============================Hash（哈希表）=============================//

    /**
     * 将哈希表 key 中的域 field 的值设为 value 。
     *
     * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。
     *
     * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
     * @param key
     * @param field
     * @param value
     * @return
     */
    public Long hset(String key, String field, String value) {
        return jedisCluster.hset(key, field, value);
    }


    /**
     * 返回哈希表 key 中给定域 field 的值。
     * @param key
     * @param field
     * @return
     */
    public String hget(String key, String field) {
        return jedisCluster.hget(key, field);
    }

    /**
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     * @param key
     * @param field
     * @return
     */
    public Boolean hdel(String key,  String... field) {
        jedisCluster.hdel(key, field);
        return true;
    }

    //=============================Set（集合）=============================//

    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
     * @param key
     * @param members
     * @return
     */
    public Long sadd(String key, String... members) {
        return jedisCluster.sadd(key, members);
    }

    /**
     * 返回集合 key 的基数(集合中元素的数量)。
     * @param key
     * @return
     */
    public Long scard(String key) {
        return jedisCluster.scard(key);
    }

    /**
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
     * @param key
     * @param member
     * @return
     */
    public Long srem(String key, String... member) {
        return jedisCluster.srem(key, member);
    }

    /**
     * 返回一个集合的全部成员，该集合是所有给定集合的交集。
     * @param keys
     * @return
     */
    public Set<String> sinter(String... keys) {
        Set<String> result=new HashSet<String>();
        int index = 0;
        for (String key : keys) {
            Set<String> temp=jedisCluster.smembers(key);
            if(index==0){
                result.addAll(temp);
            }else{
                result.retainAll(temp);
            }
            index++;
        }
        return result;
    }

    //=============================List（列表）=============================//

    /**
     * 返回列表 key 的长度。
     * @param key
     * @return
     */
    public Long ListSize(String key) {
        return jedisCluster.llen(key);
    }

    /**
     * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> ListRange(String key, long start, long end) {
        return jedisCluster.lrange(key, start, end);
    }

    /**
     * 将一个或多个值 value 插入到列表 key 的表头
     * @param key
     * @param value
     * @return
     */
    public Long pushToList(String key, String value) {
        return jedisCluster.lpush(key, value);
    }

    /**
     * 移除并返回列表 key 的尾元素。
     * @param key
     * @return
     */
    public String popFromList(String key) {
        return jedisCluster.rpop(key);
    }

    /**
     * 根据参数 count 的值，移除列表中与参数 value 相等的元素。
     * 默认count = 1
     * 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
     * @param key
     * @param value
     * @return
     */
    public Long popValueFromList(String key, String value) {
        return jedisCluster.lrem(key, 1, value);
    }


    //=============================SortedSet（有序集合）=============================//
    
    public Long zrem(String key, String... value) {
        return jedisCluster.zrem(key, value);
    }

    
    public Long zadd(String key, double score, String value) {
        return jedisCluster.zadd(key, score, value);
    }

    public Long zupdate(String key, double score, String value) {
        return jedisCluster.zadd(key, score, value,ZAddParams.zAddParams().xx());
    }

    
    public Long zadd(final String key, final Map<String, Double> scoreMembers) {
        return jedisCluster.zadd(key, scoreMembers);
    }

    
    public Set<String> zrevrange(String key, long start, long end) {
        return jedisCluster.zrevrange(key, start, end);
    }

    
    public Set<String> smembers(String key) {
        return jedisCluster.smembers(key);
    }

    
    public Double zscore(String key, String member) {
        return jedisCluster.zscore(key, member);
    }

    public Set<?> zrangeByScoreWithScores(final String key, final double min, final double max,
                                          final int offset, final int count) {
        return jedisCluster.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    
    public Set<?> zrangeByScoreWithScores(final String key, final double min, final double max) {
        return jedisCluster.zrangeByScoreWithScores(key, min, max);
    }

    
    public Set<String> zrangeByScore(final String key, final double min, final double max,
                                     final int offset, final int count) {
        return jedisCluster.zrangeByScore(key, min, max, offset, count);
    }

    
    public Set<String> zrangeByScore(final String key, final double min, final double max) {
        return jedisCluster.zrangeByScore(key, min, max);
    }

    
    public void subscribe(final JedisPubSub jedisPubSub, final String... channels) {
        jedisCluster.subscribe(jedisPubSub, channels);
    }

    
    public void psubscribe(final JedisPubSub jedisPubSub, final String... channels) {
        jedisCluster.psubscribe(jedisPubSub, channels);
    }

    
    public Long publish(final String channel, final String message) {
        return jedisCluster.publish(channel, message);
    }

    
    public void hsetForInsert(String key, String field, String value) {
        long flag=jedisCluster.hset(key, field, value);
        if(flag==0L){
            jedisCluster.hdel(key, field);
        }
    }

    
    public void hsetForUpdate(String key, String field, String value) {
        long flag=jedisCluster.hset(key, field, value);
        if(flag==1L){
            jedisCluster.hdel(key, field);
        }
    }

    
    public Boolean sismember(String key, String member) {
        return jedisCluster.sismember(key, member);
    }

    /**
     * 释放资源
     */
    public void releaseJedisCluster() {
        if (jedisCluster != null) {
            try {
                jedisCluster.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}



