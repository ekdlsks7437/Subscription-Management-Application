package com.byu.cs428.subscriptionmanager.model.service.net.request;

public class LoginRequest {

    private final String requestType = "Login";
    public String email;
    public String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
