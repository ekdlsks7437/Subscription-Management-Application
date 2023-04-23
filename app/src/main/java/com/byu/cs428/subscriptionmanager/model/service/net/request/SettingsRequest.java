package com.byu.cs428.subscriptionmanager.model.service.net.request;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.service.net.ConvertJson;

import java.util.HashMap;
import java.util.Map;

public class SettingsRequest {

    private final String requestType = "Settings";
    public User user;
    public AuthToken authToken;

    public SettingsRequest(User user, AuthToken authToken) {
        this.user = user;
        this.authToken = authToken;
    }

    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("userId", user.getId());
        headers.put("authToken", ConvertJson.Serialize(authToken));
        return headers;
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
}