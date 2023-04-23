package com.byu.cs428.subscriptionmanager.model.service.net.request;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.domain.User;

public class AddSubscriptionRequest {

    private AuthToken authToken;

    private User user;

    private Subscription subscription;

    public AddSubscriptionRequest(AuthToken authToken, User user, Subscription subscription){
        this.authToken = authToken;
        this.user = user;
        this.subscription = subscription;
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

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
