package com.byu.cs428.subscriptionmanager.model.service.net;

import com.byu.cs428.subscriptionmanager.model.domain.UserSettings;

import java.io.Serializable;

public class ServerResponseSettings  implements Serializable{
        private String notification_time;
        private String currency;


        public ServerResponseSettings(String notificationTime, String currency) {
            this.notification_time = notificationTime;
            this.currency = currency;
        }

        public String getNotificationTime() {
            return notification_time;
        }

        public void setNotificationTime(String notificationTime) {
            this.notification_time = notificationTime;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public UserSettings getUserSettings(){
            return new UserSettings(notification_time,currency);
        }
    }
