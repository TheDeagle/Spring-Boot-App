package com.application.Application.Services;

import com.application.Application.Modules.Message;
import com.application.Application.Modules.User;
import com.application.Application.Repositories.MessageSerializerRepository;
import com.application.Application.Repositories.MessagesRepository;
import com.application.Application.Serializers.MessageSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.antlr.v4.runtime.misc.MultiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

@Service
public class MessageServices {
    private final MessagesRepository messages;
    private final MessageSerializerRepository messagesSerializer;

    @Autowired
    public MessageServices(MessagesRepository messagesRepository, MessageSerializerRepository serializer){
        this.messages = messagesRepository;
        this.messagesSerializer = serializer;
    }

    public Message getMessage(User user1, User user2){
        Message _msg = this.messages.findByUser1AndUser2(user1, user2);
        if (_msg == null)
            _msg = this.messages.findByUser1AndUser2(user2, user1);
        if (_msg == null)
            _msg = new Message(user1, user2);
        return messages.save(_msg);
    }

    public Message addMessage(User sender, User receiver, String message){
        Message _msg = this.getMessage(sender, receiver);
        if (_msg == null || message.isEmpty() || message.strip().isBlank())
            return null;
        MessageSerializer _s = new MessageSerializer(receiver.getUsername(), message);
        this.messagesSerializer.save(_s);
        _msg.addMessage(_s);
        this.messages.save(_msg);
        return _msg;
    }
}
