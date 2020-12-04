package com.example.linked;

public class ContactModel {

    private String userName;
    private String userId;
    private String imageUrl;
    private MessageModel lastMsg;
    private MessageModel[] msgs;

    public ContactModel(){

    }

    public ContactModel(String userId, String userName, String imageUrl, MessageModel lastMsg, MessageModel[] msgs){
        this.userId = userId;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.lastMsg = lastMsg;
        this.msgs = msgs;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MessageModel getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(MessageModel lastMsg) {
        this.lastMsg = lastMsg;
    }

    public MessageModel[] getMsgs() {
        return msgs;
    }

    public void setMsgs(MessageModel[] msgs) {
        this.msgs = msgs;
    }
}
