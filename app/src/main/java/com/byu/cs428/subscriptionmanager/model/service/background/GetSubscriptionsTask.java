package com.byu.cs428.subscriptionmanager.model.service.background;

import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.service.net.request.GetSubscriptionsRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.response.GetSubscriptionsResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetSubscriptionsTask extends AuthenticatedTask{

    public static final String URL_PATH = "/subscription/subscriptions";
    public static final String SUBSCRIPTIONS_KEY = "subscriptions";
    private User user;
    private AuthToken authToken;

    private List<Subscription> subscriptions;

    public GetSubscriptionsTask(AuthToken authToken,User user, Handler handler) {
        super(authToken, handler);
        this.authToken = authToken;
        this.user = user;
    }

    @Override
    public void runBackgroundTask() {
        GetSubscriptionsRequest request = new GetSubscriptionsRequest(user, authToken);
        GetSubscriptionsResponse response = getServerFacade().getSubscriptions(request,URL_PATH);
        if(response.isSuccess()){
            subscriptions = new ArrayList<>();
            subscriptions.addAll(response.getSubscriptions());
            sendSuccessMessage();
        }
        else if (response.getMessage().equals("no subscriptions")){
            sendSuccessMessage();
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        super.loadSuccessBundle(msgBundle);
        msgBundle.putSerializable(SUBSCRIPTIONS_KEY, (Serializable) subscriptions);
    }
}
