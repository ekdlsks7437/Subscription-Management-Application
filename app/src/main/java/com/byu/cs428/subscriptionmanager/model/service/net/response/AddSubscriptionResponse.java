package com.byu.cs428.subscriptionmanager.model.service.net.response;


import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.service.net.ServerResponseSubscription;

public class AddSubscriptionResponse extends Response{

    private ServerResponseSubscription returnSubscription;

    AddSubscriptionResponse(ServerResponseSubscription subscription) {
        super(true, null);
        this.returnSubscription = subscription;
    }

    public Subscription getSubscription() {
        return returnSubscription.getSubscription();
    }

    public void setSubscription(ServerResponseSubscription subscription) {
        this.returnSubscription = subscription;
    }


}
