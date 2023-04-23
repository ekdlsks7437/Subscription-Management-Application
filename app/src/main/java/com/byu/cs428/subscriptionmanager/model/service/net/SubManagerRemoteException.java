package com.byu.cs428.subscriptionmanager.model.service.net;

import java.util.List;

public class SubManagerRemoteException extends Exception {
    private final String remoteExceptionType;

    protected SubManagerRemoteException(String message, String remoteExceptionType) {
        super(message);
        this.remoteExceptionType = remoteExceptionType;
    }

    public String getRemoteExceptionType() {
        return remoteExceptionType;
    }

}
