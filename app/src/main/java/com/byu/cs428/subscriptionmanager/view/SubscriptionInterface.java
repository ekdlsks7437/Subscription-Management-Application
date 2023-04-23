package com.byu.cs428.subscriptionmanager.view;

import com.byu.cs428.subscriptionmanager.model.domain.Subscription;

public interface SubscriptionInterface {
    public void getSubscription(Subscription subscription);
    public void deleteSubscription(String subscriptionId);
}
