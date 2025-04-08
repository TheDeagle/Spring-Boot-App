package com.example.Backend.Services;

import com.example.Backend.Modules.Messages;
import com.example.Backend.Modules.User;
import com.example.Backend.Repositories.MessagesRepository;
import com.example.Backend.Serializers.MessagesSerializer;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagesServices {
    MessagesRepository messagesRepository;


    @Autowired
    public MessagesServices(MessagesRepository messagesRepository)
    {
        this.messagesRepository = messagesRepository;
    }

    @Transactional
    public void addMessage(User userA, User userB, MessagesSerializer message){
        Messages _message = this.getMessage(userA, userB);
        if (_message == null)
            _message = this.getMessage(userB, userA);
        if (_message == null)
            _message = new Messages(userA, userB);
        _message.getMessages().add(message);
        this.messagesRepository.save(_message);
    }
    public Messages getMessage(User userA, User userB){
        return this.messagesRepository.findByUserAAndUserB(userA, userB);
    }

    public Boolean saveMessages(Messages message){
        try {
            this.messagesRepository.save(message);
            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
