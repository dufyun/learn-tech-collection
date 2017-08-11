package com.dufy.webscocket.stomp;


import com.dufy.webscocket.entity.Shot;
import com.dufy.webscocket.session.WebSocketSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;

/**
 * Controller for notify websocket to do something, 
 * such as receive new message, close connection, etc.
 */
@Controller
@RequestMapping("/websocket")
@SuppressWarnings({"rawtypes", "unchecked"})
public class WebSocketNotifyController {
	
	private static final Logger log = LoggerFactory.getLogger( WebSocketNotifyController.class );

	@Resource
	private SimpMessagingTemplate template;
	
	@Resource
	private WebSocketSessionManager webSocketSessionManager;


	@RequestMapping("/notifyMsg")
	@ResponseBody
	public String notifyMsg(@RequestParam String webSocketId,@RequestParam String message) {

		//校验是否有对应的WebSocket连接
		WebSocketSession wsSession = webSocketSessionManager.getSession( webSocketId );

		Shot shot = new Shot();
		shot.setMessage(message);
		//使用websocket消息模板，推送至前端
		template.convertAndSend(StompConstants.QUEUE_NEW_MSG + webSocketId, shot );

		return "new message";

	}

}
