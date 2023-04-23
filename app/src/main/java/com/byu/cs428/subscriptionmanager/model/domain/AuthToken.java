package com.byu.cs428.subscriptionmanager.model.domain;

import java.io.Serializable;

public class AuthToken implements Serializable {

    /**
     *  Authtoken UUID
     */
    public String token;

    /**
     * String representation epoch time when the token will expire.
     */
    public String expireDate;

    /**
     * User id used to keep track of who owns the authToken.
     */
    public String userId;

    public AuthToken(){}

    /***
     * Creates a authToken with only the token, hopefully only used for testing.
     * @param token unique token id
     */
    public AuthToken(String token){
        this.token = token;
    }

    /***
     * Creates the authToken
     *
     * @param token unique token id
     * @param dateTime date when the token was created as a string
     */
    public AuthToken(String token, String dateTime, String userId){
        this.token = token;
        this.expireDate = dateTime;
        this.userId = userId;
    }

    public String getToken(){
        return token;
    }

    public String getExpireDate(){
        return expireDate;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
