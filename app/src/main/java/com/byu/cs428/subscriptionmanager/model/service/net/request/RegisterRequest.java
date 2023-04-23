package com.byu.cs428.subscriptionmanager.model.service.net.request;

import com.byu.cs428.subscriptionmanager.model.domain.User;

public class RegisterRequest {

    private final String requestType = "Register";
    public String email;

    public String password;


    public RegisterRequest(String email, String password) {
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
