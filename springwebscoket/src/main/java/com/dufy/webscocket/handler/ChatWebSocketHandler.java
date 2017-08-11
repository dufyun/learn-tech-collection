package com.dufy.webscocket.handler;


import com.dufy.webscocket.session.WebSocketSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 * WebSocketHandler用于处理WebSocket连接、断开连接等事件，
 * 以及保存WebSocket会话
 * @author:dufy
 * @version:1.0.0
 * @date 2017/7/28
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class ChatWebSocketHandler extends WebSocketHandlerDecorator {
	
	private final ApplicationEventPublisher eventPublisher;
	
	private final WebSocketSessionManager webSocketSessionManager;
	
	private static final Logger logger = LoggerFactory.getLogger( ChatWebSocketHandler.class );

	public ChatWebSocketHandler(WebSocketHandler delegate, ApplicationEventPublisher eventPublisher,
                                WebSocketSessionManager webSocketSessionManager) {
		super(delegate);
		this.eventPublisher = eventPublisher;
		this.webSocketSessionManager = webSocketSessionManager;
		System.out.println("------------------ChatWebSocketHandler--------------");
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {

		System.out.println("WebSocket连接已建立，websocketId:{}, userId:{}" + session.getId());
		webSocketSessionManager.save(session);
		super.afterConnectionEstablished(session);
		
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus closeStatus) throws Exception {

		System.out.println("WebSocket连接已关闭，websocketId:{}, userId:{}" + session.getId());


	}
	
	/*@Override
	public void handleTransportError(WebSocketSession session,
			Throwable exception) throws Exception {
		super.handleTransportError(session, exception);
	}*/

	private void publishEvent(ApplicationEvent event) {
		try {
			eventPublisher.publishEvent(event);
		}
		catch (Throwable ex) {
			//忽略异常
			logger.error( "Failed to publisher event.", ex );
		}
	}
	
}
