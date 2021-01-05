package com.example.linked;

public final class UserModel {

    private String userId;
    private String email;
    private String username;

    private static UserModel userInstance;

    private UserModel(){

    }

    public UserModel(String userId, String email, String username){
        this.userId = userId;
        this.email = email;
        this.username = username;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
