package com.dufy.webscocket.handler;

import com.dufy.webscocket.session.WebSocketSessionManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
/**
 * WebSocketHandler装饰工厂
 * @author:dufy
 * @version:1.0.0
 * @date 2017/7/28
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
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
