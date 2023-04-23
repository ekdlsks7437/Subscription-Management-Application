package com.byu.cs428.subscriptionmanager.model.service.net.response;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.User;

public class LoginResponse extends Response{

    public AuthToken authToken;

    public User returnUser;

    public LoginResponse(AuthToken authToken, User user){
        super(true,null);
        this.authToken = authToken;
        this.returnUser = user;
    }

    public LoginResponse(String message){
        super(false,message);
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public User getUser() {
        return returnUser;
    }

    public void setUser(User user) {
        this.returnUser = user;
    }
}
