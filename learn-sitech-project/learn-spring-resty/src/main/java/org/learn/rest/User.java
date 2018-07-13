package org.learn.rest;

import java.io.Serializable;

/**
 * 描述该类概要功能介绍
 *
 * @author:dufyun
 * @version:1.0.0
 * @date 2018/7/13
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class User implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
