package com.byu.cs428.subscriptionmanager.model.service.net;


import com.byu.cs428.subscriptionmanager.model.domain.RenewalType;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.service.net.response.GetSubscriptionsResponse;

import java.io.Serializable;

public class ServerResponseSubscription implements Serializable {

    /**
     * Unique User id to relate to User
     */
    private String userId;

    /**
     * The name of the subscription
     */
    private String name;
    /**
     * how much the subscription costs for the given renewal period
     */
    private double cost;
    /**
     *  how much has been spent from renewing
     */
    private double total_spent;
    /**
     * String representation of the starting date of the subscription
     *
     */
    private String starting_date;
    /**
     * number of days/months/years between renewal on the subscription
     * Stored as int then letter, ex. "1D2M3Y" would be 1 day, 2 months, 3 years.
     */
    private String renewal_period;
    /**
     * type of renewal as a MONTHLY, YEARLY, or CUSTOM
     */
    private RenewalType renewalType;

    private String renewal_type;

    /**
     * String representation of the date and time to send the notification.
     */
    private String renewal_date;
    /**
     * The Unique identifier used to schedule the notification with the device
     */
    private String notificationID;
    private Double spentYTD;

    public ServerResponseSubscription(){}

    /***
     *
     * @param name name of the subscription
     * @param cost cost of the subscription
     * @param totalSpent amount spent on the subscription
     * @param startingDate datetime as a string for the initial starting date of the subscription
     * @param renewalPeriod String representing days/months/years till renewal as "0D1M0Y" being 0 days, 1 month, o years.
     * @param renewalDate date for the notification with the specified notification time added to it
     * @param notificationID Unique id for the notification so it can be edited/canceled
     */
    public ServerResponseSubscription(String name, double cost, double totalSpent, String startingDate,
                        String renewalPeriod, String renewal_type, String renewalDate, String notificationID, String userId, double spentYTD) {
        this.name = name;
        this.cost = cost;
        this.total_spent = totalSpent;
        this.starting_date = startingDate;
        this.renewal_period = renewalPeriod;
        this.renewalType = new RenewalType(renewal_type);
        this.renewal_date = renewalDate;
        this.notificationID = notificationID;
        this.userId = userId;
        this.spentYTD = spentYTD;
    }

    public Subscription getSubscription(){
        this.renewalType = new RenewalType(renewal_type);
        return new Subscription(name,cost,total_spent,starting_date,renewal_period,renewalType,renewal_date,notificationID,userId,spentYTD);
    }

    public static ServerResponseSubscription subToServerResSub(Subscription subscription){
        return new ServerResponseSubscription(subscription.name,subscription.cost,subscription.totalSpent,
                subscription.startingDate,subscription.renewalPeriod,subscription.renewalType.period.toString(),subscription.renewalDate,
                subscription.notificationID,subscription.userId,subscription.spentYTD);
    }
}

