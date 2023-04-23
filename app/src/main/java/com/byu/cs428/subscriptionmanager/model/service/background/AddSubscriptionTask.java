package com.byu.cs428.subscriptionmanager.model.service.background;

import android.os.Bundle;
import android.os.Handler;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.service.net.request.AddSubscriptionRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.response.AddSubscriptionResponse;

public class AddSubscriptionTask extends AuthenticatedTask {
    public static final String URL_PATH = "/subscription/create";
    public static final String SUBSCRIPTION_KEY = "subscription";
    private User user;
    private Subscription subscription;

    public AddSubscriptionTask(Handler handler,AuthToken authToken, User user, Subscription subscription) {
        super(authToken, handler);
        this.user = user;
        this.subscription = subscription;
    }

    @Override
    public void runBackgroundTask() {
        try {
            AddSubscriptionRequest request = new AddSubscriptionRequest(getAuthToken(), user, subscription);
            AddSubscriptionResponse response = getServerFacade().addSubscription(request,URL_PATH);

            if(response.isSuccess()){
                sendSuccessMessage();
            }
            else {
                sendFailureMessage(response.getMessage());
            }

        }catch (Exception ex){
            sendExceptionMessage(ex);
        }

    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(SUBSCRIPTION_KEY,subscription);
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
