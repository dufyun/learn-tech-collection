package com.dufy.webscocket.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * 消息订阅监听器
 * @author:dufy
 * @version:1.0.0
 * @date 2017/7/28
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class SessionSubscribeListener implements ApplicationListener<SessionSubscribeEvent> {

	private static final Logger logger = LoggerFactory.getLogger( SessionConnectedListener.class );

	@Override
	public void onApplicationEvent(SessionSubscribeEvent event) {
		System.out.println("SessionSubscribeListener websocket session:{}" +event.getUser());
		Message<byte[]> message = event.getMessage();
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		StompCommand command = accessor.getCommand();
		if (command.equals(StompCommand.SUBSCRIBE)) {
			String sessionId = accessor.getSessionId();
			String subscriptionId = accessor.getSubscriptionId();
			String destination = accessor.getDestination();
			System.out.println("SessionSubscribe, sessionId:{} "+
					sessionId +"subscriptionId:{} : "+ subscriptionId+",destination:{} "+destination);
		}
	}

}
