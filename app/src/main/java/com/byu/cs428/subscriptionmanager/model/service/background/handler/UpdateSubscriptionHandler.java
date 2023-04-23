package com.byu.cs428.subscriptionmanager.model.service.background.handler;

import android.os.Bundle;

import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.service.background.EditSubscriptionTask;
import com.byu.cs428.subscriptionmanager.model.service.observer.UpdateSubscriptionObserver;

public class UpdateSubscriptionHandler extends BackgroundTaskHandler<UpdateSubscriptionObserver> {

    public UpdateSubscriptionHandler(UpdateSubscriptionObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(UpdateSubscriptionObserver observer, Bundle data) {
        Subscription subscription = (Subscription) data.getSerializable(EditSubscriptionTask.SUBSCRIPTION_KEY);
        observer.handleSuccess(subscription);
    }
}
