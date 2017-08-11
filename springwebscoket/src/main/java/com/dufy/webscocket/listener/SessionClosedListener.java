package com.dufy.webscocket.listener;


import com.dufy.webscocket.event.WebsocketClosedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocketSession断开连接的监听器
 * @author:dufy
 * @version:1.0.0
 * @date 2017/7/28
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class SessionClosedListener implements ApplicationListener<WebsocketClosedEvent> {
	
	private static final Logger logger = LoggerFactory.getLogger( SessionClosedListener.class );

	
	@Override
	public void onApplicationEvent(WebsocketClosedEvent event) {

		System.out.println("SessionClosedListener websocket session:{}" +event.getSession());
		
		try {
			doClosed( event.getSession(), event.getCloseStatus() );
		} catch (Exception e) {
			logger.error( "关闭ChatSession异常", e );
		}
		
	}
	
	/**
	 * 处理WeSocketSebssion关闭事件，如果是notify通知关闭，则不需要调用dubbo服务
	 * @author huangxf
	 * @param session
	 * @return void
	 */
	protected void doClosed(WebSocketSession session, CloseStatus status ) {

		System.out.println("--------处理WeSocketSebssion关闭事件----");
		
	}

}
