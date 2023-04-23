package com.byu.cs428.subscriptionmanager.model.service.background;

import android.os.Bundle;
import android.os.Handler;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.service.net.ServerFacade;

public abstract class AuthenticateTask extends BackgroundTask{

    public static final String USER_KEY = "User";
    public static final String AUTHTOKEN_KEY = "AuthToken";

    private User authedUser;
    private AuthToken authToken;

    private String email;
    private String password;

    public AuthenticateTask(Handler handler, String email, String password) {
        super(handler);
    }

    @Override
    public void runBackgroundTask() {
        runAuthenticationTask();
    }

    protected abstract void runAuthenticationTask();

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        //TODO: fill this out
        msgBundle.putSerializable(USER_KEY, authedUser);
        msgBundle.putSerializable(AUTHTOKEN_KEY,authToken);
    }

    public void setAuthedUser(User authedUser) {
        this.authedUser = authedUser;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
