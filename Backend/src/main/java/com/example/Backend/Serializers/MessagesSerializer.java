package com.example.Backend.Serializers;

import jakarta.persistence.Embeddable;
import org.springframework.stereotype.Component;

@Component
@Embeddable
public class MessagesSerializer {
    String receiver;
    String message;

    public MessagesSerializer(){

    }

    public MessagesSerializer(String receiver, String message)
    {
        this.receiver = receiver;
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
