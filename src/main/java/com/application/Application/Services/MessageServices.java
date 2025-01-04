package com.application.Application.Services;

import com.application.Application.Modules.Message;
import com.application.Application.Modules.User;
import com.application.Application.Repositories.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServices {
    private final MessagesRepository messages;

    @Autowired
    public MessageServices(MessagesRepository messagesRepository){
        this.messages = messagesRepository;
    }

    public Message getMessage(User user1, User user2){
        Message _msg = this.messages.findByUser1AndUser2(user1, user2);
        if (_msg == null)
            _msg = this.messages.findByUser1AndUser2(user2, user1);
        return _msg;
    }
}
