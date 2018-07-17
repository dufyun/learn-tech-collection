package org.dufy.cache;

import javax.annotation.Resource;

import org.dufy.BaseCaseTest;
import org.dufy.dao.UserDao;
import org.dufy.model.User;
import org.junit.Test;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * 描述该类概要功能介绍
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/16
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class UserCacheTest extends BaseCaseTest {



    /**
     * 使用 hutool 的日志记录
     */
    private static final Log log = LogFactory.get();

    @Resource
    private UserDao userDao;

    @Resource
    private IUserCache userCache;


    @Test
    public void setUserTest(){
        User user = userDao.selectByPrimaryKey(1);
        log.info("select from db uid = 1" + user);

        userCache.setUser(user);

    }

    @Test
    public void getUserTest(){
        String id = "1";
        User user = userCache.getUser(id);

        log.info("select from cache uid = 1" + user);
    }

    @Test
    public void hsetUserTest(){
        User user = userDao.selectByPrimaryKey(2);
        log.info("select from db uid = 2" + user);
        userCache.hsetUser(user);
    }

    @Test
    public void hgetUserTest(){
        String id = "2";
        User user = userCache.hgetUser(id);

        log.info("select from cache uid = 2" + user);
    }


}
