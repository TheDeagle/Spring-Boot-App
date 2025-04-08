package com.example.Backend.Modules;

import com.example.Backend.Serializers.MessagesSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "messages")
public class Messages {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "user_a_id")
    User userA;
    @ManyToOne
    @JoinColumn(name = "user_b_id")
    User userB;
    @ElementCollection
    List<MessagesSerializer> messages;


    public Messages(){

    }

    public Messages(User userA, User userB){
        this.userA = userA;
        this.userB = userB;
        this.messages = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserA() {
        return userA;
    }

    public void setUserA(User userA) {
        this.userA = userA;
    }

    public User getUserB() {
        return userB;
    }

    public void setUserB(User userB) {
        this.userB = userB;
    }

    public List<MessagesSerializer> getMessages() {
        return messages;
    }

    public void setMessages(List<MessagesSerializer> messages) {
        this.messages = messages;
    }
}
