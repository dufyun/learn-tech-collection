package com.dufy.webscocket.session;

import org.springframework.context.ApplicationEvent;

/**
 * WebSocketSession关闭事件
 * @author:dufy
 * @version:1.0.0
 * @date 2017/7/28
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
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
