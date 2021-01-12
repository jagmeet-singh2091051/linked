package com.example.linked;

public class ContactModel {

    private String userName;
    private String userId;
    private String imageUrl;
    private String lastMsg;
    private String lastMsgTime;
    private MessageModel[] msgs;

    public ContactModel(){

    }

    public ContactModel(String userId, String userName, String imageUrl){
        this.userId = userId;
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

    public ContactModel(String userId, String userName, String imageUrl, String lastMsg, String lastMsgTime){
        this.userId = userId;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.lastMsg = lastMsg;
        this.lastMsgTime = lastMsgTime;
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

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public MessageModel[] getMsgs() {
        return msgs;
    }

    public void setMsgs(MessageModel[] msgs) {
        this.msgs = msgs;
    }

    public String getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(String lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }
}
