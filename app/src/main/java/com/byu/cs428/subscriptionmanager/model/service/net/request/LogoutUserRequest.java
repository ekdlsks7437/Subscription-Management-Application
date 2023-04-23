package com.byu.cs428.subscriptionmanager.model.service.net.request;

public class LogoutUserRequest {

    private String userId;

    public LogoutUserRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
