package com.application.Application.Config;

import com.application.Application.Modules.User;
import com.application.Application.Services.MessageServices;
import com.application.Application.Services.UserServices;
import com.application.Application.WebSockets.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final UserServices Users;
    private final MessageServices Messages;

    @Autowired
    public WebSocketConfig(UserServices userServices, MessageServices messageServices){
        this.Messages = messageServices;
        this.Users = userServices;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketHandler(Users, Messages), "/ws")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*");
    }
}
