package com.byu.cs428.subscriptionmanager.model.service.net.request;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.domain.UserSettings;

public class AddSettingsRequest {

    private final String requestType = "Settings";
    public User user;
    public AuthToken authToken;

    public UserSettings userSettings;


    public AddSettingsRequest(User user, AuthToken authToken, UserSettings userSettings) {
        this.user = user;
        this.authToken = authToken;
        this.userSettings = userSettings;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
    }
}
