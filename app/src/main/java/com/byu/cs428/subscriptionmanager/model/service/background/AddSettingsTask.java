package com.byu.cs428.subscriptionmanager.model.service.background;

import android.os.Bundle;
import android.os.Handler;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.Subscription;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.domain.UserSettings;
import com.byu.cs428.subscriptionmanager.model.service.net.request.AddSettingsRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.response.SettingsResponse;

public class AddSettingsTask extends AuthenticatedTask{

    //TODO: how do I create the initial settings?
    public static final String URL_PATH = "/setting/create";
    public static final String SETTINGS_KEY = "settings";
    private User user;
    private UserSettings userSettings;

    public AddSettingsTask(AuthToken authToken, Handler handler, User user, UserSettings userSettings) {
        super(authToken, handler);
        this.userSettings = userSettings;
        this.user = user;
    }

    @Override
    public void runBackgroundTask() {
        try {
            AddSettingsRequest request = new AddSettingsRequest(user,getAuthToken(),userSettings);
            SettingsResponse response = getServerFacade().addSettings(request,URL_PATH);

            if(response.isSuccess()){
                sendSuccessMessage();
            }
            else {
                sendFailureMessage(response.getMessage());
            }

        }catch (Exception ex){
            sendExceptionMessage(ex);
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(SETTINGS_KEY,userSettings);
    }
}
