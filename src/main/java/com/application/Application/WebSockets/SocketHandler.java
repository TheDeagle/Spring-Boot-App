package com.application.Application.WebSockets;

import com.application.Application.Modules.Message;
import com.application.Application.Modules.User;
import com.application.Application.Serializers.MessageSerializer;
import com.application.Application.Services.MessageServices;
import com.application.Application.Services.UserServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class SocketHandler extends TextWebSocketHandler {
    private final UserServices Users;
    private final MessageServices Messages;
    Map<String, WebSocketSession> UsersSessions = new HashMap<>();
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public SocketHandler(UserServices userServices, MessageServices messageServices){
        this.Messages = messageServices;
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
        this.UsersSessions.put(_user.getUsername(), session);
        System.out.println("USER WITH USERNAME " + _user.getUsername() + " HAS BEEN ADDED TO THE SESSIONS MAP");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        User _user = this.Users.isWebSocketLoggedIn(session);
        System.out.println(message.getPayload());
        if (_user == null) {
            System.out.println("USER NOT LOGGED IN");
            session.close();
            return;
        }
        MessageSerializer _serializer;
        try {
            _serializer = this.objectMapper.readValue(message.getPayload(), MessageSerializer.class);
            User _receiver = this.Users.getUser(_serializer.getReceiverUsername());
            if (_receiver == null)
                throw new Exception("User not found");
            this.Messages.addMessage(_user, _receiver, _serializer.getMessage());
            if (UsersSessions.get(_receiver.getUsername()) != null){
                Map<String, String> _json = new HashMap<>();
                _json.put("senderUsername", _user.getUsername());
                _json.put("message", _serializer.getMessage());
                UsersSessions.get(_receiver.getUsername()).sendMessage(new TextMessage("{" +
                        "\"senderUsername\": \"" + _user.getUsername() + "\"," +
                        "\"message\": \"" + _serializer.getMessage() + "\"}"));
            }
            System.out.println("A MESSAGE HAS BEEN SENT TO " + _serializer.getReceiverUsername());
            System.out.println("A SOCKET MESSAGE HAS BEEN RECEIVED");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus){
        User _user = this.Users.isWebSocketLoggedIn(session);
        if (_user != null){
            this.UsersSessions.remove(_user.getUsername());
            System.out.println("USER WITH USERNAME " + _user.getUsername() + " HAS BEEN REMOVED FROM THE SESSIONS MAP");
        }
        System.out.println("A SOCKET CONNECTION HAS BEEN CLOSED WITH CLOSE CODE: " + closeStatus);
    }
}