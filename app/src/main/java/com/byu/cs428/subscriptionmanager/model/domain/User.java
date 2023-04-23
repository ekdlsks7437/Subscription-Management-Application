package com.byu.cs428.subscriptionmanager.model.domain;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    public String email;

    public String userId;

    public User(){}

    public User(String email, String id) {
        this.email = email;
        this.userId = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return userId;
    }

    public void setId(String id) {
        this.userId = id;
    }
}
