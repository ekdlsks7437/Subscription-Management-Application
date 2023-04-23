package com.byu.cs428.subscriptionmanager.model.service.background;


import android.os.Bundle;
import android.os.Handler;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.service.net.request.EditSubscriptionRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.response.EditSubscriptionResponse;

public class EditSubscriptionTask extends AuthenticatedTask{

    public static final String URL_PATH = "/subscription/update";
    public static final String SUBSCRIPTION_KEY = "subscription";
    private Subscription subscription;
    private User user;

    public EditSubscriptionTask(Handler handler, AuthToken authToken,  User user, Subscription subscription) {
        super(authToken, handler);
        this.subscription = subscription;
        this.user = user;
    }

    @Override
    public void runBackgroundTask() {
        try {
            EditSubscriptionRequest request = new EditSubscriptionRequest(getAuthToken(),user,subscription);
            EditSubscriptionResponse response = getServerFacade().editSubscription(request,URL_PATH);

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
        //TODO: fill this out
        msgBundle.putSerializable(SUBSCRIPTION_KEY, subscription);
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
