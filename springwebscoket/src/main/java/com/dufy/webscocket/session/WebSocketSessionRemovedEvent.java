package com.dufy.webscocket.session;

import org.springframework.context.ApplicationEvent;

/**
 * WebSocketSession关闭事件 
 * @author huangxf
 * @date 2016年11月16日
 */
public class WebSocketSessionRemovedEvent extends ApplicationEvent {

	private static final long serialVersionUID = 7356159550631611965L;
	
	/**
	 * WebSocket的sessionId
	 */
	private String sessionId;

	public WebSocketSessionRemovedEvent(Object source, String sessionId) {
		super( source );
		this.sessionId = sessionId;
	}
	
	public String getSessionId() {
		return this.sessionId;
	}

}
