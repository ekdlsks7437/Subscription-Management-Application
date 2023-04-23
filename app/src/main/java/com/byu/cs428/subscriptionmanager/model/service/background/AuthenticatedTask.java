package com.byu.cs428.subscriptionmanager.model.service.background;

import android.os.Handler;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.service.net.ServerFacade;

public abstract class AuthenticatedTask extends BackgroundTask{
    private final AuthToken authToken;

    public AuthenticatedTask(AuthToken authToken, Handler handler) {
        super(handler);
        this.authToken = authToken;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }
}
