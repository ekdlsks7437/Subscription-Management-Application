package com.byu.cs428.subscriptionmanager.model.service.background;

import android.os.Bundle;
import android.os.Handler;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.service.net.request.RemoveSubscriptionRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.response.RemoveSubscriptionResponse;

import java.io.Serializable;

public class RemoveSubscriptionTask extends AuthenticatedTask {
    public static final String URL_PATH = "/subscription/delete";
    private String subscriptionId;
    public static final String NOTIFICATION_ID_KEY = "notificationId";
    private User user;

    public RemoveSubscriptionTask(Handler handler, AuthToken authToken, User user, String subscriptionID) {
        super(authToken, handler);
        this.subscriptionId = subscriptionID;
        this.user = user;
    }

    @Override
    public void runBackgroundTask() {

        try {
            RemoveSubscriptionRequest request = new RemoveSubscriptionRequest(getAuthToken(), user, subscriptionId);
            RemoveSubscriptionResponse response = getServerFacade().removeSubscription(request,URL_PATH);
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

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        super.loadSuccessBundle(msgBundle);
        msgBundle.putSerializable(NOTIFICATION_ID_KEY, (Serializable) subscriptionId);
    }

    //TODO: on the server side, make sure that the user's id matches the authtokens id, so that people
    // cannot just delete a users subscription without having a valid authtoken matching the users id.
}
