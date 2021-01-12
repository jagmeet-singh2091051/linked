package com.example.linked;

public final class UserModel {

    private String userId;
    private String email;
    private String userName;
    private String imageUrl;

    private static UserModel userInstance;

    private UserModel(){

    }

    public UserModel(String userId, String email, String userName){
        this.userId = userId;
        this.email = email;
        this.userName = userName;
    }

    public UserModel(String userId, String email, String userName, String imageUrl){
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

    public static UserModel getInstance(){
        if(userInstance == null){
            userInstance = new UserModel();
        }

        return userInstance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
