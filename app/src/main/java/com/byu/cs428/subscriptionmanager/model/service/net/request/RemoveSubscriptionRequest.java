package com.byu.cs428.subscriptionmanager.model.service.net.request;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.domain.User;

public class RemoveSubscriptionRequest{
    private AuthToken authToken;

    private User user;

    private String id;

    public RemoveSubscriptionRequest(AuthToken authToken, User user, String subscriptionId) {
        this.authToken = authToken;
        this.user = user;
        this.id = subscriptionId;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSubscription() {
        return id;
    }

    public void setSubscription(String subscriptionId) {
        this.id = subscriptionId;
    }
}
