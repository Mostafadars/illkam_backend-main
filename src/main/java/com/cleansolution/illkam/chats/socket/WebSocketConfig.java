package com.cleansolution.illkam.chats.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // socketJs 클라이언트가 WebSocket 핸드셰이크를 하기 위해 연결할 endpoint를 지정할 수 있다.
        registry.addEndpoint("/chat/inbox")
                .setAllowedOriginPatterns("*") ;// cors 허용을 위해 꼭 설정해주어야 한다.
//                .withSockJS(); //웹소켓을 지원하지 않는 브라우저는 sockJS를 사용하도록 한다.
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker("/sub");
        // 클라이언트로부터 메시지를 받을 api의 prefix를 설정한다.
        // publish
        registry.setApplicationDestinationPrefixes("/pub");
    }
}
