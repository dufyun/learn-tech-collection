package com.dufy.webscocket.stomp;

import com.dufy.webscocket.entity.Shot;
import com.dufy.webscocket.vo.WebSocketMessageResponse;
import com.dufy.webscocket.vo.WebSocketRequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WebSocketMessageController{


	private static final Logger log = LoggerFactory.getLogger( WebSocketMessageController.class );

	private List<Map<String,Object>> list  = new ArrayList<Map<String, Object>>();
	/**
	 * Websocket客户端发送消息，异步通知/queue/chat/msgResponse
	 * @param msg
	 * @param accessor
	 * @return LivechatResponse
	 */
	@MessageMapping("/sendMessage")
	@SendToUser("/queue/chat/msgResponse")
	public WebSocketMessageResponse sendMessage(WebSocketRequestMessage msg, SimpMessageHeaderAccessor accessor ) {
		System.out.println("----------------sendMessage-------------");
		String requestId = msg.getRequestId();
		Map<String,Object> map  = new HashMap<String,Object>();

		try {
			map.put(requestId,requestId);
			list.add(map);

			//返回消息给websocket
			return new WebSocketMessageResponse("success", requestId );
		}catch (Exception e) {
			log.error( "WebSocket消息接收异常", e );
			return null;
		}
	}
	@MessageMapping("/marco")
	public void sendMessage(Shot incoing) {
		System.out.println(incoing);
	}


	
	/**
	 * 订阅新消息 
	 * @param accessor
	 * @return LivechatResponse
	 */
	@SubscribeMapping("/queue/system/newMsg")
	public WebSocketMessageResponse subcribeNewMsg( SimpMessageHeaderAccessor accessor ) {

		//获取未读消息，这个时候主要是系统提示消息
		try {
			return new WebSocketMessageResponse( "sucess" );
		} catch (Exception e) {
			log.error( "WebSocket获取系统消息异常", e );
			return  null;
		}
		
	}

	
}
