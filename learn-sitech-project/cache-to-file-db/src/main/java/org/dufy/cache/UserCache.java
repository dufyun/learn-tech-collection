package org.dufy.cache;

import org.dufy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;


/**
 * 用户的缓存实现类
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/16
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
@Component
public class UserCache implements IUserCache{

    @Autowired
    private RedisClientTemplate redisClientTemplate;

    @Override
    public void setUser(User user) {
        String key = CacheConstants.USER_KEY + user.getId();
        redisClientTemplate.set(key,JSON.toJSONString(user));
        redisClientTemplate.expire(key, CacheConstants.expire_day);
    }

    @Override
    public User getUser(String id) {
        String key = CacheConstants.USER_KEY + id;
        if(redisClientTemplate.exists(key)){
            String userStr = redisClientTemplate.get(key);
            return JSON.parseObject(userStr, User.class);
        }
        return null;
    }

    @Override
    public void hsetUser(User user) {
        String key = CacheConstants.USER_KEY + user.getId();
        redisClientTemplate.hset(key, "id", String.valueOf(user.getId() == null ? "" : user.getId()));
        redisClientTemplate.hset(key, "age", String.valueOf(user.getAge() == null ? "" : user.getAge()));
        redisClientTemplate.hset(key, "userName", String.valueOf(user.getUserName() == null ? "" : user.getUserName()));
        redisClientTemplate.hset(key, "password", String.valueOf(user.getPassword() == null ? "" : user.getPassword()));
        redisClientTemplate.expire(key, CacheConstants.expire_day);
    }

    @Override
    public User hgetUser(String id) {

        String key = CacheConstants.USER_KEY + id;
        if(redisClientTemplate.exists(key)){
            User user = new User();
            Integer uid = Integer.parseInt(redisClientTemplate.hget(key, "id"));
            Integer age = Integer.parseInt(redisClientTemplate.hget(key, "age"));
            String userName = redisClientTemplate.hget(key, "userName");
            String password = redisClientTemplate.hget(key, "password");
            user.setId(uid);
            user.setAge(age);
            user.setUserName(userName);
            user.setPassword(password);
            return user;
        }
        return null;
    }

}
