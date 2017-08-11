package com.dufy.webscocket.stomp;

/**
 * "queue"代表一对一消息，"topic"代表广播消息
 * @author huangxf
 * @date 2016年11月21日
 */
public interface StompConstants {
	
	/**
	 * 新消息订阅地址
	 */
	String QUEUE_NEW_MSG = "/queue/chat/newMsg-user";
	
	/**
	 * stomp客户端消息发送之后的回调地址
	 */
	String QUEUE_MSG_RESPONSE = "/queue/chat/msgResponse-user";
	
	/**
	 * 系统主动关闭会话的订阅地址
	 */
	String QUEUE_SYSTEM_CLOSE = "/queue/system/close-user";
	
	/**
	 * 系统消息订阅地址
	 */
	String QUEUE_SYSTEM_MSG = "/queue/system/newMsg-user";
	

}
