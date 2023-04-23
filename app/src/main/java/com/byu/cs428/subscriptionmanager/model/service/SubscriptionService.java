package com.byu.cs428.subscriptionmanager.model.service;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.service.background.AddSubscriptionTask;
import com.byu.cs428.subscriptionmanager.model.service.background.BackgroundTask;
import com.byu.cs428.subscriptionmanager.model.service.background.BackgroundTaskUtils;
import com.byu.cs428.subscriptionmanager.model.service.background.EditSubscriptionTask;
import com.byu.cs428.subscriptionmanager.model.service.background.GetSubscriptionsTask;
import com.byu.cs428.subscriptionmanager.model.service.background.RemoveSubscriptionTask;
import com.byu.cs428.subscriptionmanager.model.service.background.handler.RemoveHandler;
import com.byu.cs428.subscriptionmanager.model.service.background.handler.SubscriptionHandler;
import com.byu.cs428.subscriptionmanager.model.service.background.handler.UpdateSubscriptionHandler;
import com.byu.cs428.subscriptionmanager.model.service.observer.SubscriptionsObserver;
import com.byu.cs428.subscriptionmanager.presenter.AddEditSubscriptionPresenter;
import com.byu.cs428.subscriptionmanager.presenter.SubscriptionPresenter;

public class SubscriptionService {

    public SubscriptionService(){}

    public void getSubscription(SubscriptionPresenter.SubscriptionObserver observer, User user, AuthToken authToken){
        BackgroundTaskUtils.runTask(new GetSubscriptionsTask(authToken,user,new SubscriptionHandler(observer)));
    }

    public void removeSubscription(SubscriptionPresenter.RemoveSubscriptionObserver observer, User user, AuthToken authToken, String subscriptionId){
        BackgroundTaskUtils.runTask(new RemoveSubscriptionTask(new RemoveHandler(observer),authToken,user,subscriptionId));
    }

    public void addSubscription(AddEditSubscriptionPresenter.EditSubscriptionObserver observer, User user, AuthToken authToken, Subscription subscription){
        BackgroundTaskUtils.runTask(new AddSubscriptionTask(new UpdateSubscriptionHandler(observer),authToken,user,subscription));
    }

    public void editSubscription(AddEditSubscriptionPresenter.EditSubscriptionObserver observer, User user, AuthToken authToken, Subscription subscription){
        BackgroundTaskUtils.runTask(new EditSubscriptionTask(new UpdateSubscriptionHandler(observer),authToken,user,subscription));
    }
}
