package org.dufy.task;

import java.util.Date;
import java.util.Random;

import org.dufy.cache.IUserCache;
import org.dufy.log.DataLogger;
import org.dufy.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 生成模拟用户日志的task
 * （1）每两秒创建一个User写入缓存
 * （2）每五秒从缓存中获取User，写入文件
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/17
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */

@Component
public class UserLogTask {

    private static final Logger logger = LoggerFactory.getLogger(UserLogTask.class);

    private Random random = new Random(100);

    @Autowired
    private IUserCache userCache;
    /**
     * 每二秒执行一次
     */
    @Scheduled(cron = "0/2 * * * * ?")
    public void TaskCacheUserJob() {
        cacheUser();
        logger.info("------- user cache task----------" + new Date());
    }

    public void cacheUser(){
        User user = new User();
        int uid = random.nextInt(10) + 1;
        user.setId(uid);
        user.setUserName("user_" + uid);
        user.setPassword("password_" +uid );
        user.setAge(uid);
        userCache.setUser(user);

    }

    /**
     * 每五秒执行一次
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void TaskRecordUserJob() {
        recordUser();
        logger.info("-------user data log file  task----------" + new Date());
    }

    public void recordUser(){
        int uid = random.nextInt(10) + 1;
        User user = userCache.getUser(String.valueOf(uid));
        if(user != null){
            DataLogger.recordUser(user);
        }

    }

}