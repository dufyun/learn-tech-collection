package com.dufy.webscocket.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocketSession断开连接的事件
 * @author huangxf
 * @date 2016年11月15日
 */
public class WebsocketClosedEvent extends ApplicationEvent {

	private static final long serialVersionUID = 8925298824870091591L;
	
	/**
	 * Spring WebSocketSession
	 */
	private WebSocketSession session;
	
	/**
	 * 关闭标识符
	 */
	private CloseStatus closeStatus;

	public WebsocketClosedEvent(Object source, WebSocketSession session) {
		this( source, session,CloseStatus.SERVER_ERROR  );
	}
	
	public WebsocketClosedEvent(Object source, WebSocketSession session, CloseStatus status) {
		super(source);
		this.session = session;
		this.closeStatus = status;
	}
	
	public WebSocketSession getSession() {
		return this.session;
	}

	/**
	 * 由系统主动关闭返回true 
	 * @return boolean
	 */
	public boolean isClosedBySystem() {
		return true;
	}

	public CloseStatus getCloseStatus() {
		return closeStatus;
	}

}
