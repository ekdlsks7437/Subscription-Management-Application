package com.byu.cs428.subscriptionmanager.model.service.net.response;

import com.byu.cs428.subscriptionmanager.model.service.net.ServerResponseSubscription;

public class EditSubscriptionResponse extends Response{

    private ServerResponseSubscription updatedSubscription;

    EditSubscriptionResponse(ServerResponseSubscription subscription) {
        super(true, null);
        this.updatedSubscription = subscription;
    }

    public ServerResponseSubscription getSubscription() {
        return updatedSubscription;
    }

    public void setSubscription(ServerResponseSubscription subscription) {
        this.updatedSubscription = subscription;
    }
}
