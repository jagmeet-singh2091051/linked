package com.example.linked;

import com.google.firebase.Timestamp;

import java.util.Date;

public class MessageModel {

    private String message;
    private String timeSent;
    private String timeReceived;
    private boolean sent;
    private Timestamp timestamp;

    public MessageModel(){

    }

    public MessageModel(String message, String timeSent){
        this.message = message;
        this.timeSent = timeSent;
    }

    public MessageModel(String message, String timeSent, String timeReceived, boolean sent, Timestamp timestamp){

        this.message = message;
        this.timeSent = timeSent;
        this.timeReceived = timeReceived;
        this.sent = sent;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public String getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(String timeReceived) {
        this.timeReceived = timeReceived;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
