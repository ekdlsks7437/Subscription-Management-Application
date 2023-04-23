package com.byu.cs428.subscriptionmanager.model.service.observer;

import com.byu.cs428.subscriptionmanager.model.domain.Subscription;

public interface UpdateSubscriptionObserver extends ServiceObserver {
    void handleSuccess(Subscription subscription);
}
