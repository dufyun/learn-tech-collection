package com.dufy.webscocket.config;

import com.dufy.webscocket.handler.ChatWebSocketHandlerDecoratorFactory;
import com.dufy.webscocket.session.WebSocketSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;

/**
 * 描述该类概要功能介绍
 *
 * @author:dufy
 * @version:1.0.0
 * @date 2017/7/28
 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
 */
@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
public class WebSocketBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Resource
    private HandshakeInterceptor handshakeInterceptor;

    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Resource
    private WebSocketSessionManager webSocketSessionManager;



    private static final Logger logger = LoggerFactory.getLogger( WebSocketBrokerConfig.class );


    /**
     * 将"/chat" 路径注册为STOMP端点，这个路径与发送和接收消息的目的路径有所不同。
     * 这是一个端点，客户端在订阅或发布消息到目的地址前，需要连接改端点、
     *
     * 即用户发送请求 url="appName/chat" 与STOMP Server进行连接，之后在转发到订阅的url
     *
     * 提示：端点的作用 —— 客户端订阅或发布消息到目的地址前，需要连接改端点！
     * 所以可以进行一些拦截Intercept操作
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //在页面js上可以通过"/applicationName/chat"来和服务器的WebSocket连接

        System.out.println("------第三步----registerStompEndpoints-------------");
        registry.addEndpoint("/chat").setAllowedOrigins("*").addInterceptors(handshakeInterceptor).withSockJS();

        //.addInterceptors() 可以在访问端点的时候进行相应的拦截操作

        //allowedOrigins配置可以拦截跨站域名请求
        //withSockJS  请求地址支持SockJs
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //应用程序以/app /user为前缀，代理目的地以/topic、/user为前缀

        System.out.println("------第四步----registerStompEndpoints-------------");
        registry.enableSimpleBroker("/topic","/queue");
        //加入/app、/user的请求地址处理，/app-->发送消息@MessageMapping,
        // /user-->订阅@SubscribeMapping，见Controller
        registry.setApplicationDestinationPrefixes("/app","/user");

    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {

        System.out.println("---第一步-configureWebSocketTransport------------");
        //设置发送超时时间
        registration.setSendTimeLimit( 5000 );

        //添加WebSocket的装饰工厂，便于监听Session状态
        registration.addDecoratorFactory( wsHandlerDecoratorFactory() );

    }

    @Bean
    public ChatWebSocketHandlerDecoratorFactory wsHandlerDecoratorFactory() {

        System.out.println("---第二步----ChatWebSocketHandlerDecoratorFactory----------");
        return new ChatWebSocketHandlerDecoratorFactory( eventPublisher, webSocketSessionManager );
    }



    /***
     *registry.enableSimpleBroker("/topic", "/user");这句话表示在topic和user这两个域上可以向客户端发消息。
     *
     *registry.setUserDestinationPrefix("/user");这句话表示给指定用户发送一对一的主题前缀是"/user"。
     *
     *registry.setApplicationDestinationPrefixes("/app");这句话表示客户单向服务器端发送时的主题上面需要加"/app"作为前缀。
     *
     *stompEndpointRegistry.addEndpoint("/hello").setAllowedOrigins("*").withSokJS();这个和客户端创建连接时的url有关，
     *其中setAllowedOrigins()方法表示允许连接的域名，withSockJS()方法表示支持以SockJS方式连接服务器。
     */
}
