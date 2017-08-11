package com.dufy.webscocket.vo;

/**
 * 描述该类概要功能介绍
 *
 * @author:dufy
 * @version:1.0.0
 * @date 2017/7/31
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class WebSocketMessageResponse {
    private Object content;

    private String requestID;

    private String retCode = "000000";

    public WebSocketMessageResponse(Object content){
        this.content=content;
    }
    public WebSocketMessageResponse(Object content,String requestID){
        this.content=content;
        this.requestID=requestID;
    }
    public void setContent(Object content) {
        this.content = content;
    }

    public Object getContent() {
        return content;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getRequestID() {
        return requestID;
    }
}
