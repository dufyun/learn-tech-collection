package com.dufy.webscocket.session.data;

import com.dufy.webscocket.session.WebSocketSessionManager;
import com.dufy.webscocket.session.WebSocketSessionRemovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.Assert;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketSessionDecorator;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring WebSocket Session管理类，负责保存会话id，主动关闭会话，定期清理超时会话
 * @author:dufy
 * @version:1.0.0
 * @date 2017/7/28
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
public class DefaultWebSocketSessionManager implements WebSocketSessionManager{

	private final static  int  DEFAULT_CLEAN_PERIOD = 5*1000;
	/**
	 * 负责清理websocket的任务
	 */
	private final TaskScheduler taskScheduler;
	
	/**
	 * 事件发布
	 */
	private ApplicationEventPublisher eventPublisher;
	
	/**
	 * 存储session的并发Map，初始化128个容量以提高读写性能
	 */
	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<String, WebSocketSession>( 128 );
	
	private static final Logger logger = LoggerFactory.getLogger( DefaultWebSocketSessionManager.class );
	
	public DefaultWebSocketSessionManager() {
		this.taskScheduler = createDefaultTaskScheduler();
		postInit();
	}
	
	public DefaultWebSocketSessionManager( TaskScheduler taskScheduler ) {
		Assert.notNull( taskScheduler );
		this.taskScheduler = taskScheduler;
		postInit();
	}
	
	public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	/**
	 * 清理Session的时间间隔
	 */
	protected int getCleanPeriod() {
		return DEFAULT_CLEAN_PERIOD;
	}

	/**
	 * 启用任务调度每隔5分钟清理一次session
	 * 目前只是简单的判断下session是否open，如果不是则移除，避免内存泄露
	 * @return void
	 */
	protected void postInit() {
		taskScheduler.scheduleAtFixedRate( new Runnable() {
			@Override
			public void run() {
				if ( logger.isDebugEnabled() ) {
					logger.debug( "WebSocketSessionManager Cleaning up sessions expiring at {}", new Date() );
				}
				List<String> removedIds = new ArrayList<String>();
				for (WebSocketSession session : sessions.values()) {
					try {
						if ( !session.isOpen() ) {
							remove( session.getId() );
						}
					}
					catch (Throwable ex) {
						logger.warn("Failed to remove session:" + session, ex);
					}
				}
				if (logger.isInfoEnabled() ) {
					logger.info( "WebSocketSession removed: {}, active:{}", removedIds, sessions.size() );
				}
			}
		}, getCleanPeriod() );
	}
	
	/**
	 * 创建默认的任务调度，线程池大小为CPU核数，线程名字以WebSocketSessionManager-开头
	 * @return TaskScheduler
	 */
	protected TaskScheduler createDefaultTaskScheduler() {
		ThreadPoolTaskScheduler poolScheduler = new ThreadPoolTaskScheduler();
		int threads = Runtime.getRuntime().availableProcessors();
		if ( threads > 8 ) {
			threads = 8;
		}
		poolScheduler.setPoolSize( threads );
		poolScheduler.setThreadNamePrefix( "LiveChat-WebSocketSessionManagerThread-" );
		poolScheduler.initialize();
		return poolScheduler;
	}
	
	@Override
	public void save(WebSocketSession session) {
		Assert.notNull( session );
		this.sessions.put( session.getId(), session );
	}

	@Override
	public WebSocketSession getSession(String id) {
		WebSocketSession session = this.sessions.get( id );
		if ( session != null ) {
			return new LivechatWebsocketSessionDecorator( session );
		}
		return null;
	}

	@Override
	public boolean remove(String id) {
		WebSocketSession session = this.sessions.remove( id );
		if ( session != null ) {
			this.afterSessionRemoved( id );
			return true;
		}
		return false;
	}
	
	/**
	 * 推送Session清理的事件
	 * @param id
	 * @return void
	 */
	protected void afterSessionRemoved(String id) {
		//推送关闭事件
		if ( eventPublisher != null ) {
			try {
				eventPublisher.publishEvent( new WebSocketSessionRemovedEvent( this, id ) );
				logger.debug( "Publish websocket session closed event, sessionId:" + id );
			} catch (Exception e) {
				logger.debug("Failed to publish close event, sessionId:" + id, e);
			}
		}
	}

	@Override
	public void close(String id) {
		this.close( id, new CloseStatus( 3000, "System auto close" ));
	}
	
	@Override
	public void close(String id, CloseStatus status) {
		
		WebSocketSession session = this.sessions.get( id );
		
		if ( session == null ) {
			logger.debug("no websocket session to close: " + id );
			return;
		}
		
		//关闭Session
		try {
			session.close( status );
			logger.info("Websocket session was closed: " + id );
		} catch (IOException e) {
			logger.debug("Failed to close " + session, e);
		} finally {
			// 在finally移除，避免close出现异常的时候泄露内存
			this.remove( id );
		}
		
	}
	
	/**
	 * WebSocketSession装饰，加入close()方法的逻辑，便于清理session
	 * @author huangxf
	 * @date 2016年11月16日
	 */
	private class LivechatWebsocketSessionDecorator extends WebSocketSessionDecorator {

		public LivechatWebsocketSessionDecorator(WebSocketSession session) {
			super(session);
		}

		@Override
		public final void close() throws IOException {
			DefaultWebSocketSessionManager.this.close( getDelegate().getId() );
			super.close();
		}

		@Override
		public final void close(CloseStatus status) throws IOException {
			DefaultWebSocketSessionManager.this.close( getDelegate().getId(), status );
			super.close(status);
		}
		
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [taskScheduler=" 
				+ this.taskScheduler + ", eventPublisher=]" + this.eventPublisher;
	}

	@Override
	public String webSocketId() {
        Set<String> strings = this.sessions.keySet();
        return strings.size() > 0 ?(String)strings.toArray()[0]:"";
    }
}
