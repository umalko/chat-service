package com.mavs.chatservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${spring.rabbitmq.host}")
    private String rabbitMqHost;
    @Value("${spring.rabbitmq.tcpPort}")
    private Integer rabbitMqTcpPort;
    @Value("${spring.rabbitmq.username}")
    private String rabbitMqLogin;
    @Value("${spring.rabbitmq.password}")
    private String rabbitMqPassword;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/api/v1/chat");
        registry.enableStompBrokerRelay("/topic")
                .setRelayHost(rabbitMqHost)
                .setRelayPort(rabbitMqTcpPort)
                .setClientLogin(rabbitMqLogin)
                .setClientPasscode(rabbitMqPassword);
    }
}