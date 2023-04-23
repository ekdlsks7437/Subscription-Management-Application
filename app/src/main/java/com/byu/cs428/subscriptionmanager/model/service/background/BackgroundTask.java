package com.byu.cs428.subscriptionmanager.model.service.background;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.byu.cs428.subscriptionmanager.model.service.net.ServerFacade;

public abstract class BackgroundTask implements Runnable{

    private static final String LOG_TAG = "BackgroundTask";
    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";
    private ServerFacade serverFacade;
    protected final Handler handler;

    public BackgroundTask(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            runBackgroundTask();
        }catch (Exception ex){
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    protected void sendExceptionMessage(Exception ex){
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, false);
        msgBundle.putSerializable(EXCEPTION_KEY, ex);
        sendMessage(msgBundle);
    }

    protected void sendSuccessMessage(){
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        loadSuccessBundle(msgBundle);
        sendMessage(msgBundle);
    }

    /**
     * Override this inside the tasks.
     * @param msgBundle the bundle holding the key, and the value.
     */
    protected void loadSuccessBundle(Bundle msgBundle) {
    }

    protected void sendFailureMessage(String message){
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY,false);
        msgBundle.putString(MESSAGE_KEY, message);
        sendMessage(msgBundle);
    }

    private void sendMessage(Bundle msgBundle){
        Message msg = Message.obtain();
        msg.setData(msgBundle);
        handler.sendMessage(msg);
    }

    public abstract void runBackgroundTask();

    public ServerFacade getServerFacade(){
        if(serverFacade == null){
            serverFacade = new ServerFacade();
        }
        return new ServerFacade();
    }
}
