package com.dufy.webscocket.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket建立连接的事件
 *
 * @author:dufy
 * @version:1.0.0
 * @date 2017/7/28
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
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
