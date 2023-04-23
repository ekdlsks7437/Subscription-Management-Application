package com.byu.cs428.subscriptionmanager.model.service.net.response;

import com.byu.cs428.subscriptionmanager.model.domain.RenewalType;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.service.net.ServerResponseSubscription;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetSubscriptionsResponse extends Response {
    private ServerResponseSubscription[] subscriptions;

    public GetSubscriptionsResponse(ServerResponseSubscription[] subscriptions) {
        super(true,null);
        this.subscriptions = subscriptions;
    }

    public List<Subscription> getSubscriptions() {
        List<Subscription> subscriptionList = new ArrayList<>();
        for (ServerResponseSubscription subscription : subscriptions) {
            subscriptionList.add(subscription.getSubscription());
        }


        return subscriptionList;
    }

    // this is for debugging.
    public void setSubscriptions(ServerResponseSubscription[] subscriptions) {
        this.subscriptions = subscriptions;
    }
}
