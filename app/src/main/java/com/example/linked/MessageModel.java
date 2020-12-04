package com.example.linked;

import java.util.Date;

public class MessageModel {

    private String message;
    private Date timeSent;
    private Date timeReceived;

    public MessageModel(){

    }

    public MessageModel(String message, Date timeSent, Date timeReceived){

        this.message = message;
        this.timeSent = timeSent;
        this.timeReceived = timeReceived;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }

    public Date getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(Date timeReceived) {
        this.timeReceived = timeReceived;
    }
}
