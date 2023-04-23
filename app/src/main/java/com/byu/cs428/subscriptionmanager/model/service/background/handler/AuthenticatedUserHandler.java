package com.byu.cs428.subscriptionmanager.model.service.background.handler;

import android.os.Bundle;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.service.background.LoginUserTask;
import com.byu.cs428.subscriptionmanager.model.service.background.RegisterUserTask;
import com.byu.cs428.subscriptionmanager.model.service.observer.AuthenticationObserver;
import com.byu.cs428.subscriptionmanager.presenter.AuthenticationPresenter;

public class AuthenticatedUserHandler extends BackgroundTaskHandler<AuthenticationObserver> {

    public AuthenticatedUserHandler(AuthenticationPresenter.AuthenticatedObserver observer){
        super(observer);
    }

    public AuthenticatedUserHandler(AuthenticationPresenter.AuthenticatedObserver observer,Bundle data){
        super(observer);
        User user = (User) data.getSerializable(RegisterUserTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(LoginUserTask.AUTHTOKEN_KEY);
        observer.handleSuccess(user,authToken);
    }

    @Override
    protected void handleSuccessMessage(AuthenticationObserver observer, Bundle data) {
        User user = (User) data.getSerializable(LoginUserTask.USER_KEY);
        AuthToken authToken = (AuthToken) data.getSerializable(LoginUserTask.AUTHTOKEN_KEY);
        observer.handleSuccess(user,authToken);
    }
}
