package com.dufy.webscocket.session;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
/**
 * Spring WebsocketSession的容器
 * @author:dufy
 * @version:1.0.0
 * @date 2017/7/28
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public interface WebSocketSessionManager {
	
	/**
	 * taskScheduler执行任务的时间间距，5分钟
	 */
	public static int DEFAULT_CLEAN_PERIOD = 5 * 60 * 1000;
	
	/**
	 * 默认的关闭标识
	 */

	/**
	 * 以session的id作为key保存session
	 * @param session
	 * @return void
	 */
	void save(WebSocketSession session);

	/**
	 * 根据sessionId获取WebsocketSession，返回WebSocketSession的Decorator
	 * <p>返回的对象在执行close()方法时，会主动清理SessionManager容器内对应的session</p>
	 * @param id
	 * @return WebSocketSession
	 */
	WebSocketSession getSession(String id);

	/**
	 * 从管理容器中清除，并发布{@link WebSocketSessionRemovedEvent}事件
	 * @param id
	 * @return boolean
	 */
	boolean remove(String id);

	/**
	 * @author huangxf
	 * @return void
	 */
	void close(String id);
	
	/**
	 * 关闭Session，并指明关闭标识
	 * @param id
	 * @param status
	 * @return void
	 */
	void close(String id, CloseStatus status);

	String webSocketId();
	
}
