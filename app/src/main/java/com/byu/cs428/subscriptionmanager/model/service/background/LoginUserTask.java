package com.byu.cs428.subscriptionmanager.model.service.background;

import android.os.Handler;

import com.byu.cs428.subscriptionmanager.model.service.net.request.LoginRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.response.LoginResponse;

public class LoginUserTask extends AuthenticateTask {

    private static final String URL_PATH = "/user/signin";
    private final String emailAddress;
    private final String password;

    public LoginUserTask(Handler messageHandler,String email, String password) {
    super(messageHandler,email,password);
    this.emailAddress = email;
    this.password = password;
    }

    @Override
    protected void runAuthenticationTask() {
        try {
            LoginRequest request = new LoginRequest(emailAddress, password);
            LoginResponse response = getServerFacade().loginUser(request,URL_PATH);

            if(response.isSuccess()){
                setAuthedUser(response.getUser());
                setAuthToken(response.getAuthToken());
                sendSuccessMessage();
            }
            else {
                sendFailureMessage(response.getMessage());
            }
        }
        catch ( Exception ex){
            sendExceptionMessage(ex);
        }
    }
}
