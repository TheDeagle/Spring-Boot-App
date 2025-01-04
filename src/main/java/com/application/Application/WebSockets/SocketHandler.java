package com.application.Application.WebSockets;

import com.application.Application.Modules.User;
import com.application.Application.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class SocketHandler extends TextWebSocketHandler {
    private final UserServices Users;

    @Autowired
    public SocketHandler(UserServices userServices){
        this.Users = userServices;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        User _user = this.Users.isWebSocketLoggedIn(session);
        if (_user == null) {
            session.close();
            return;
        }
        System.out.println("A SOCKET CONNECTION HAS BEEN ESTABLISHED");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        User _user = this.Users.isWebSocketLoggedIn(session);
        if (_user == null) {
            session.close();
            return;
        }
        System.out.println("A SOCKET MESSAGE HAS BEEN RECEIVED");
        TextMessage _msg = new TextMessage("Hello " + _user.getUsername());
        session.sendMessage(_msg);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus){
        System.out.println("A SOCKET CONNECTION HAS BEEN CLOSED WITH CLOSE CODE: " + closeStatus.toString());
    }
}