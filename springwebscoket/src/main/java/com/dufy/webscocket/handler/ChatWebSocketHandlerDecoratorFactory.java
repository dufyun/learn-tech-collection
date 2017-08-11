package com.dufy.webscocket.handler;

import com.dufy.webscocket.session.WebSocketSessionManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

/**
 * WebSocketHandler装饰工厂
 * @author huangxf
 * @date 2016年11月17日
 */
public class ChatWebSocketHandlerDecoratorFactory implements WebSocketHandlerDecoratorFactory {

	private final ApplicationEventPublisher eventPublisher;
	
	private final WebSocketSessionManager webSocketSessionManager;
	
	public ChatWebSocketHandlerDecoratorFactory( ApplicationEventPublisher eventPublisher,
			WebSocketSessionManager webSocketSessionManager ) {
		this.eventPublisher = eventPublisher;
		this.webSocketSessionManager = webSocketSessionManager;
	}
	
	@Override
	public WebSocketHandler decorate(WebSocketHandler handler) {
		System.out.println("---------decorate-------------");
		return new ChatWebSocketHandler( handler, eventPublisher, webSocketSessionManager );
	}

}
