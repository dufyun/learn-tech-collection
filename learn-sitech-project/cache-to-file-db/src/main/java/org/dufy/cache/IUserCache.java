package org.dufy.cache;

import org.dufy.model.User;

/**
 * 用户缓存接口类
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/16
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public interface IUserCache {

    /**
     * string set user
     * @param user
     */
    public void setUser(User user);

    /**
     * string get user
     * @param id
     * @return
     */
    public User getUser(String id);

    /**
     * hset user
     * @param user
     */
    public void hsetUser(User user);

    /**
     * hget user
     * @param id
     * @return
     */
    public User hgetUser(String id);

}
