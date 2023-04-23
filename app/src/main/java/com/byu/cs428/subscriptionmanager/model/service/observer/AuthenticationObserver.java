package com.byu.cs428.subscriptionmanager.model.service.observer;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.User;

public interface AuthenticationObserver extends ServiceObserver{
    void handleSuccess(User user, AuthToken authToken);
}
