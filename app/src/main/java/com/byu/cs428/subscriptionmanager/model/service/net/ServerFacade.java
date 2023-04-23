package com.byu.cs428.subscriptionmanager.model.service.net;

import com.byu.cs428.subscriptionmanager.model.domain.RenewalType;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.service.net.request.AddSettingsRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.AddSubscriptionRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.EditSubscriptionRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.GetSubscriptionsRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.LoginRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.LogoutUserRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.RegisterRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.RemoveSubscriptionRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.SettingsRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.response.AddSubscriptionResponse;
import com.byu.cs428.subscriptionmanager.model.service.net.response.EditSubscriptionResponse;
import com.byu.cs428.subscriptionmanager.model.service.net.response.GetSubscriptionsResponse;
import com.byu.cs428.subscriptionmanager.model.service.net.response.LoginResponse;
import com.byu.cs428.subscriptionmanager.model.service.net.response.LogoutUserResponse;
import com.byu.cs428.subscriptionmanager.model.service.net.response.RegisterResponse;
import com.byu.cs428.subscriptionmanager.model.service.net.response.RemoveSubscriptionResponse;
import com.byu.cs428.subscriptionmanager.model.service.net.response.SettingsResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServerFacade {
    private static final String URL = "http://10.0.2.2:3001";
    //TODO: if its localhost in android its supposed to be 10.0.2.2:3001, localhost:3001 otherwise

    private final SendRequest sendRequest = new SendRequest(URL);

    public RegisterResponse registerUser(RegisterRequest registerRequest, String urlPath) throws IOException, SubManagerRemoteException {
        return sendRequest.doPost(urlPath, registerRequest,RegisterResponse.class);
    }

    public LoginResponse loginUser(LoginRequest request, String urlPath) throws IOException, SubManagerRemoteException{
        return sendRequest.doPost(urlPath, request, LoginResponse.class);
    }

    public GetSubscriptionsResponse getSubscriptions(GetSubscriptionsRequest request, String urlPath){
        return sendRequest.doGet(urlPath, request.getHeaders(), GetSubscriptionsResponse.class);
    }

    public LogoutUserResponse logoutUser(LogoutUserRequest request, String url) throws SubManagerRemoteException {
        return sendRequest.doPost(url,request, LogoutUserResponse.class);
    }

    public AddSubscriptionResponse addSubscription(AddSubscriptionRequest request, String url) throws SubManagerRemoteException {
        return sendRequest.doPost(url,request,AddSubscriptionResponse.class);
    }

    public EditSubscriptionResponse editSubscription(EditSubscriptionRequest request, String url) throws SubManagerRemoteException {
        return sendRequest.doPost(url,request,EditSubscriptionResponse.class);
    }

    public RemoveSubscriptionResponse removeSubscription(RemoveSubscriptionRequest request, String url) throws SubManagerRemoteException {
        return sendRequest.doPost(url,request,RemoveSubscriptionResponse.class);
    }

    public SettingsResponse addSettings(AddSettingsRequest request, String url) throws SubManagerRemoteException {
        return sendRequest.doPost(url,request,SettingsResponse.class);
    }

    public SettingsResponse updateSettings(AddSettingsRequest request, String url) throws SubManagerRemoteException {
        return  sendRequest.doPost(url,request,SettingsResponse.class);
    }

    public SettingsResponse getSettings(SettingsRequest request, String url){
        return sendRequest.doGet(url,request.getHeaders(),SettingsResponse.class);
    }
}
