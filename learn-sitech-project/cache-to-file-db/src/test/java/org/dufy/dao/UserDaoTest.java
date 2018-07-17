package org.dufy.dao;

import java.util.List;

import javax.annotation.Resource;

import org.dufy.BaseCaseTest;
import org.dufy.model.User;
import org.junit.Test;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * 测试类
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/13
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */


public class UserDaoTest extends BaseCaseTest {


    /**
     * 使用 hutool 的日志记录
     */
    private static final Log log = LogFactory.get();

    @Resource
    private UserDao userDao;

    @Test
    public void testInsert() {

        User user = new User();
        user.setUserName("github");
        user.setPassword("123456");
        user.setAge(10);

        int insert = userDao.insert(user);

        log.info("insert :  " + insert);
    }

    @Test
    public void testGetAllUser(){
        List<User> allUser = userDao.getAllUser();
        for (User user: allUser) {
            log.info("user :  " + user);
        }
    }
}
