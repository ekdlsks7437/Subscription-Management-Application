package com.byu.cs428.subscriptionmanager.model.service.background;

import android.os.Handler;

import com.byu.cs428.subscriptionmanager.model.service.net.request.LogoutUserRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.response.LoginResponse;
import com.byu.cs428.subscriptionmanager.model.service.net.response.LogoutUserResponse;

public class LogoutUserTask extends BackgroundTask{

    public static final String URL_PATH = "/user/signout";

    private String userId;

    public LogoutUserTask(Handler handler, String userId) {
        //TODO: maybe we should use the AuthenticatedTask for everything besides this one since it doesn't need a authToken.
        super(handler);
        this.userId = userId;
    }

    @Override
    public void runBackgroundTask() {
        try {
            LogoutUserRequest request = new LogoutUserRequest(userId);
            LogoutUserResponse response = getServerFacade().logoutUser(request,URL_PATH);
            if(response.isSuccess()){
                //TODO: Maybe I just send a success message and logout no-matter what?
            }
            sendSuccessMessage();
        }catch (Exception ex){
            sendExceptionMessage(ex);
        }

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
