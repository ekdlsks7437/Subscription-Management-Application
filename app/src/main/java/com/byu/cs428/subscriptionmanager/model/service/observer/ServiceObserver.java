package com.byu.cs428.subscriptionmanager.model.service.observer;

public interface ServiceObserver {

    void handleFailure(String message);
    void handleException(Exception ex);
}
