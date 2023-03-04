package com.example.laborator6.domain;

public class Message {

    private final String message;

    private final Long sentBy;

    private final Long sentTo;

    public Message(String message, Long sentBy, Long sentTo) {
        this.message = message;
        this.sentBy = sentBy;
        this.sentTo = sentTo;
    }

    public String getMessage(){
        return this.message;
    }

    public Long getSentBy(){
        return this.sentBy;
    }

    public Long getSentTo(){
        return this.sentTo;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", sentBy=" + sentBy +
                ", sentTo=" + sentTo +
                '}';
    }
}
