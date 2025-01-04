package com.application.Application.Serializers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageSerializer {
    @JsonProperty("receiverUsername")
    String receiverUsername;
    @JsonProperty("message")
    String message;

    public MessageSerializer(String receiverUsername, String message) {
        this.receiverUsername = receiverUsername;
        this.message = message;
    }

    public MessageSerializer(){

    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
