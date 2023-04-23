package com.byu.cs428.subscriptionmanager.Cache;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.domain.UserSettings;

import java.io.Serializable;
import java.util.List;

public class Cache implements Serializable {

    private static final Cache instance = new Cache();

    public static Cache getInstance(){
        return instance;
    }

    private User user;
    private AuthToken authToken;

    private List<Subscription> subscriptionList;

    private UserSettings settings;

    private Cache(){
        initialize();
    }

    private void initialize(){
        user = new User(null,null);
        authToken = null;
        settings = null;
        subscriptionList = null;
    }

    public void clearCache(){
        initialize();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public List<Subscription> getSubscriptionList() {
        return subscriptionList;
    }

    public void setSubscriptionList(List<Subscription> subscriptionList) {
        this.subscriptionList = subscriptionList;
    }

    public UserSettings getSettings() {
        return settings;
    }

    public void setSettings(UserSettings settings) {
        this.settings = settings;
    }
}
