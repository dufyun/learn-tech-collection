package com.dufy.webscocket.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * 描述该类概要功能介绍
 *
 * @author:dufy
 * @version:1.0.0
 * @date 2017/7/28
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class ChatHandInteceptor extends HttpSessionHandshakeInterceptor{

    private  static  Logger logger   = LoggerFactory.getLogger(ChatHandInteceptor.class);
    private final String devMode = System.getProperty("Websocket.DevMode");
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("--------ChatHandInteceptor.beforeHandshake------------");
        System.out.println("=================devMode=============" + devMode);
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        System.out.println("---------ChatHandInteceptor.afterHandshake-------------");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
