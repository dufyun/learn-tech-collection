package com.dufy.webscocket.listener;

import com.dufy.webscocket.event.WebsocketConnectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocketSession连接监听器，
 * @author:dufy
 * @version:1.0.0
 * @date 2017/7/28
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class SessionConnectedListener implements ApplicationListener<WebsocketConnectedEvent> {
	
	private static final Logger logger = LoggerFactory.getLogger( SessionConnectedListener.class );
	
	@Override
	public void onApplicationEvent(WebsocketConnectedEvent event) {
		
		WebSocketSession wsSession = event.getSession();
		System.out.println("SessionConnectedEventListener session:{}" + wsSession);
		
	}

}
