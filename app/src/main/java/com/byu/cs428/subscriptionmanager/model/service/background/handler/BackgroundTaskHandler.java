package com.byu.cs428.subscriptionmanager.model.service.background.handler;

import android.os.Bundle;
import android.os.Message;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.byu.cs428.subscriptionmanager.model.service.background.BackgroundTask;
import com.byu.cs428.subscriptionmanager.model.service.observer.ServiceObserver;

public abstract class BackgroundTaskHandler<T extends ServiceObserver> extends Handler {

    private final T observer;

    protected BackgroundTaskHandler(T observer) {
        this.observer = observer;
    }

    protected abstract void handleSuccessMessage(T observer, Bundle data);

    @Override
    public void handleMessage(@NonNull Message msg){
         boolean success = msg.getData().getBoolean(BackgroundTask.SUCCESS_KEY);
         if(success){
             handleSuccessMessage(observer,msg.getData());
         } else if (msg.getData().containsKey(BackgroundTask.MESSAGE_KEY)){
             String message = msg.getData().getString(BackgroundTask.MESSAGE_KEY);
             observer.handleFailure(message);
         } else if (msg.getData().containsKey(BackgroundTask.EXCEPTION_KEY)){
             Exception exception = (Exception) msg.getData().getSerializable(BackgroundTask.EXCEPTION_KEY);
             observer.handleException(exception);
         }
    }
}
