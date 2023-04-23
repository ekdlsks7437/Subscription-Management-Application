package com.byu.cs428.subscriptionmanager.model.domain;

import java.io.Serializable;

public class UserSettings implements Serializable {
    private String notificationTime;
    private String currency;


    public UserSettings(String notificationTime, String currency) {
        this.notificationTime = notificationTime;
        this.currency = currency;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
