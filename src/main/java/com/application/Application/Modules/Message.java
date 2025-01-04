package com.application.Application.Modules;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    HashMap<String, String> messages;
    @ManyToOne
    @JoinColumn(name = "user1_id")
    User user1;
    @ManyToOne
    @JoinColumn(name = "user2_id")
    User user2;

    public Message(){

    }

    public Message(User user1, User user2){
        this.user1 = user1;
        this.user2 = user2;
    }

    public void addMessage(String senderUsername, String message){
        this.messages.put(senderUsername, message);
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public void setMessage(HashMap<String, String> messages) {
        this.messages = messages;
    }
}
