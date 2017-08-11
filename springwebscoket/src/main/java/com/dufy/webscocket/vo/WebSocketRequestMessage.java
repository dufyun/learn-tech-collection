package com.dufy.webscocket.vo;

/**
 * 描述该类概要功能介绍
 *
 * @author:dufy
 * @version:1.0.0
 * @date 2017/7/31
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class WebSocketRequestMessage{

    private String name;
    private String requestId;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }
}
