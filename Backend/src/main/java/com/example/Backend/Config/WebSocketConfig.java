package com.example.Backend.Config;

import com.example.Backend.Modules.Messages;
import com.example.Backend.Modules.User;
import com.example.Backend.Serializers.MessagesSerializer;
import com.example.Backend.Services.JWTServices;
import com.example.Backend.Services.MessagesServices;
import com.example.Backend.Services.UserServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.*;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final UserServices userServices;
    private final JWTServices jwtServices;
    private final MessagesServices messagesServices;

    @Autowired
    public WebSocketConfig(UserServices userServices, JWTServices jwtServices, MessagesServices messagesServices){
        this.userServices = userServices;
        this.jwtServices = jwtServices;
        this.messagesServices = messagesServices;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(new MyWebSocketHandler(this.userServices, this.messagesServices), "/ws")
                .addInterceptors(new MyHandshakeInterceptor(userServices, jwtServices))
                .setAllowedOrigins("*");
    }
}

class MyWebSocketHandler implements WebSocketHandler {
    private Map<String, Object> WebSessions;
    private final UserServices userServices;
    private final MessagesServices messagesServices;

    public MyWebSocketHandler(UserServices userServices, MessagesServices messagesServices){
        this.userServices = userServices;
        this.messagesServices = messagesServices;
        this.WebSessions = new HashMap<>();
    }

    private User getUser(WebSocketSession session){
        Map<String, Object> _attributes = session.getAttributes();
        User _user = (User) _attributes.get("user");
        if (_user == null)
            return null;
        return _user;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        User _user = this.getUser(session);
        if (_user == null){
            session.close();
            System.out.println("A WEBSOCKET SESSION HAS BEEN CLOSED!");
            return;
        }
        this.WebSessions.put(_user.getUsername(), session);
        System.out.println("A SOCKET CONNECTION HAS BEEN ESTABLISHED!");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        User _currentUser = this.getUser(session);
        if (_currentUser == null){
            session.close();
            this.WebSessions.remove(_currentUser.getUsername());
            System.out.println("A WEBSOCKET SESSION HAS BEEN CLOSED!");
            return;
        }

        ObjectMapper _mapper = new ObjectMapper();
        MessagesSerializer _serializedMessage = _mapper.readValue((String)message.getPayload(), MessagesSerializer.class);
        if (_serializedMessage.getMessage() == null || _serializedMessage.getMessage().isEmpty())
            return;
        User _receiver = this.userServices.getUser(_serializedMessage.getReceiver());

        if (_receiver == null)
            return;

        this.messagesServices.addMessage(_currentUser, _receiver, _serializedMessage);

        WebSocketSession _receiverSession = (WebSocketSession) this.WebSessions.get(_receiver.getUsername());
        if (_receiverSession == null)
            return;

        _receiverSession.sendMessage(new TextMessage(_mapper.writeValueAsString(_serializedMessage)));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}

class MyHandshakeInterceptor implements HandshakeInterceptor {
    private final UserServices userServices;
    private final JWTServices jwtServices;

    MyHandshakeInterceptor(UserServices userServices, JWTServices jwtServices) {
        this.userServices = userServices;
        this.jwtServices = jwtServices;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        List<String> _cookies = request.getHeaders().get("Cookie");
        String _token = _cookies.get(0);
        if (_token == null || _token.strip().isEmpty())
            attributes.put("user", null);
        User _user = this.userServices.getUser(this.jwtServices.extractJwtUsername(_token.replace("AccessToken=", "")));
        if (_user == null)
            attributes.put("user", null);
        attributes.put("user", _user);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}