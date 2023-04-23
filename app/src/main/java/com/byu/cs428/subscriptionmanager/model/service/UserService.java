package com.byu.cs428.subscriptionmanager.model.service;

import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.service.background.BackgroundTask;
import com.byu.cs428.subscriptionmanager.model.service.background.BackgroundTaskUtils;
import com.byu.cs428.subscriptionmanager.model.service.background.LoginUserTask;
import com.byu.cs428.subscriptionmanager.model.service.background.LogoutUserTask;
import com.byu.cs428.subscriptionmanager.model.service.background.RegisterUserTask;
import com.byu.cs428.subscriptionmanager.model.service.background.handler.AuthenticatedUserHandler;
import com.byu.cs428.subscriptionmanager.presenter.AuthenticationPresenter;

public class UserService {

    public UserService(){}

    public void RegisterUser(AuthenticationPresenter.AuthenticatedObserver observer, String email, String password){
        BackgroundTaskUtils.runTask(new RegisterUserTask(new AuthenticatedUserHandler(observer),email,password));
    }

    public void LoginUser(AuthenticationPresenter.AuthenticatedObserver observer, String email, String password){
        BackgroundTaskUtils.runTask(new LoginUserTask(new AuthenticatedUserHandler(observer),email,password));
    }

    public void LogoutUser(AuthenticationPresenter.AuthenticatedObserver observer, User user){
        BackgroundTaskUtils.runTask(new LogoutUserTask(new AuthenticatedUserHandler(observer),user.userId));
    }
}
