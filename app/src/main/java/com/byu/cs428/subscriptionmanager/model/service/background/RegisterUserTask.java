package com.byu.cs428.subscriptionmanager.model.service.background;

import android.os.Handler;

import com.byu.cs428.subscriptionmanager.model.service.net.request.RegisterRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.response.RegisterResponse;

public class RegisterUserTask extends AuthenticateTask{
    private static final String URL_PATH = "/user/create";

    private final String emailAddress;
    private final String password;

    public RegisterUserTask(Handler handler, String emailAddress, String password) {
        super(handler, emailAddress, password);
        this.emailAddress = emailAddress;
        this.password = password;
    }

    @Override
    protected void runAuthenticationTask() {
        try {
            RegisterRequest registerRequest = new RegisterRequest(emailAddress,password);
            RegisterResponse response = getServerFacade().registerUser(registerRequest,URL_PATH);

            if(response.isSuccess()){
                setAuthedUser(response.getUser());
                setAuthToken(response.getAuthToken());
                sendSuccessMessage();
            }
            else {
                sendFailureMessage(response.getMessage());
            }
        }
        catch (Exception ex){
            sendExceptionMessage(ex);
        }

    }


}
