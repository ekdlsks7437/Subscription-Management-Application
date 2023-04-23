package com.byu.cs428.subscriptionmanager.model.service.net.response;

import java.io.Serializable;

public class Response implements Serializable {
    private final boolean success;
    private final String message;

    Response(boolean success){
        this(success,null);
    }

    Response(boolean success, String message){
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
