package org.dufy.dao;

import java.util.List;

import org.dufy.model.User;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public interface UserDao {

    /**
     * 获取所有用户信息
     * @return
     */
    List<User> getAllUser();


    /**
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @param record
     * @return
     */
    int insert(User record);

    /**
     *
     * @param record
     * @return
     */
    int insertSelective(User record);

    /**
     *
     * @param id
     * @return
     */
    User selectByPrimaryKey(Integer id);

    /**
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(User record);

    /**
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(User record);
}