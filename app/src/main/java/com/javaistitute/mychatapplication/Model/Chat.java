package com.javaistitute.mychatapplication.Model;

public class Chat {

    String sender;
    String receiver;
    String message;
    String timestamp;

    public Chat() {
    }

    public Chat(String sender, String receiver, String message, String timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
