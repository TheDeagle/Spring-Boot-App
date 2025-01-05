package com.application.Application.Modules;

import com.application.Application.Serializers.MessageSerializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.MultiMap;
import org.apache.catalina.LifecycleState;
import org.hibernate.annotations.Type;

import java.util.*;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @OneToMany(fetch = FetchType.EAGER)
    List<MessageSerializer> messagesList = new ArrayList<MessageSerializer>();
    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;
    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;

    public Message(){

    }

    public Message(User user1, User user2){
        this.user1 = user1;
        this.user2 = user2;
    }

    public void addMessage(MessageSerializer message){
        this.messagesList.add(message);
        for (MessageSerializer message1: this.messagesList){
            System.out.println(message1.getMessage());
        }
    }

    public List<MessageSerializer> getMessages() {
        return messagesList;
    }

    public void setMessage(List<MessageSerializer> messagesList) {
        this.messagesList = messagesList;
    }
}
