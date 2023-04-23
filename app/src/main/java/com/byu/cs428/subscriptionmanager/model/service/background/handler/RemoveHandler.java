package com.byu.cs428.subscriptionmanager.model.service.background.handler;

import static com.byu.cs428.subscriptionmanager.model.service.background.RemoveSubscriptionTask.NOTIFICATION_ID_KEY;

import android.os.Bundle;

import com.byu.cs428.subscriptionmanager.model.service.observer.ServiceObserver;
import com.byu.cs428.subscriptionmanager.presenter.SubscriptionPresenter;

public class RemoveHandler extends BackgroundTaskHandler<SubscriptionPresenter.RemoveSubscriptionObserver>{

    public RemoveHandler(SubscriptionPresenter.RemoveSubscriptionObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(SubscriptionPresenter.RemoveSubscriptionObserver observer, Bundle data) {
        String notificationId = (String) data.getSerializable(NOTIFICATION_ID_KEY);
        observer.handleSuccess(notificationId);
    }
}
