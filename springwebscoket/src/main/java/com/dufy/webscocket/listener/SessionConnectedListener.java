package com.dufy.webscocket.listener;

import com.dufy.webscocket.event.WebsocketConnectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocketSession连接监听器，
 * 创建连接时将WebSocket的相关信息关联到ChatSession中
 * @author huangxf
 * @date 2016年11月17日
 */
public class SessionConnectedListener implements ApplicationListener<WebsocketConnectedEvent> {
	
	private static final Logger logger = LoggerFactory.getLogger( SessionConnectedListener.class );
	
	@Override
	public void onApplicationEvent(WebsocketConnectedEvent event) {
		
		WebSocketSession wsSession = event.getSession();
		System.out.println("SessionConnectedEventListener session:{}" + wsSession);
		
	}

}
