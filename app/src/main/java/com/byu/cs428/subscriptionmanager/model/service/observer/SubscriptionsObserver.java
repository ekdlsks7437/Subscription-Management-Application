package com.byu.cs428.subscriptionmanager.model.service.observer;

import com.byu.cs428.subscriptionmanager.model.domain.Subscription;

import java.util.List;

public interface SubscriptionsObserver extends ServiceObserver{
    void handleSuccess(List<Subscription> subscriptions);
}
