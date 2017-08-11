package com.dufy.webscocket.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket建立连接的事件
 * @author huangxf
 * @date 2016年11月15日
 */
public class WebsocketConnectedEvent extends ApplicationEvent {

	private static final long serialVersionUID = 8925298824870091591L;
	
	private WebSocketSession session;

	public WebsocketConnectedEvent(Object source, WebSocketSession session) {
		super(source);
		this.session = session;
	}
	
	public WebSocketSession getSession() {
		return this.session;
	}

}
