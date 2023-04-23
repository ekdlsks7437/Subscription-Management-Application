package com.byu.cs428.subscriptionmanager.model.service.background.handler;

import static com.byu.cs428.subscriptionmanager.model.service.background.GetSubscriptionsTask.SUBSCRIPTIONS_KEY;

import android.os.Bundle;

import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.presenter.SubscriptionPresenter;

import java.util.List;

public class SubscriptionHandler extends BackgroundTaskHandler<SubscriptionPresenter.SubscriptionObserver> {
    public SubscriptionHandler(SubscriptionPresenter.SubscriptionObserver observer) {
        super(observer);
    }



    @Override
    protected void handleSuccessMessage(SubscriptionPresenter.SubscriptionObserver observer, Bundle data) {
        List<Subscription> subscriptions = (List<Subscription>) data.getSerializable(SUBSCRIPTIONS_KEY);
        observer.handleSuccess(subscriptions);
    }
}
