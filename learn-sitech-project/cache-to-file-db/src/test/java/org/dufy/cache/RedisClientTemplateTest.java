package org.dufy.cache;

import javax.annotation.Resource;

import org.dufy.BaseCaseTest;
import org.junit.Test;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * 测试redis集群配置是否ok
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/16
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class RedisClientTemplateTest extends BaseCaseTest {

    /**
     * 使用 hutool 的日志记录
     */
    private static final Log log = LogFactory.get();

    @Resource
    private RedisClientTemplate redisClientTemplate;

    @Test
    public void testString() {
        redisClientTemplate.set("hello", "world");
        log.info("================> " + redisClientTemplate.get("hello"));
    }

    @Test
    public void testHash() {
        int id = 10;
        String key = "user:id:" + id;
        redisClientTemplate.hset(key, "name", "dufy");
        log.info("================> " + redisClientTemplate.hget(key,"name"));
    }
}
