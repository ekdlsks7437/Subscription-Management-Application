package com.byu.cs428.subscriptionmanager.model.service.net.request;

import android.os.Bundle;
import android.os.Handler;

import com.byu.cs428.subscriptionmanager.Cache.Cache;
import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.domain.UserSettings;
import com.byu.cs428.subscriptionmanager.model.service.background.AuthenticatedTask;
import com.byu.cs428.subscriptionmanager.model.service.net.response.SettingsResponse;

public class UpdateSettingsTask extends AuthenticatedTask {
    public static final String URL_PATH = "/setting/update";
    public static final String SETTINGS_KEY = "settings";
    private User user;
    private UserSettings userSettings;

    public UpdateSettingsTask(AuthToken authToken, Handler handler, User user, UserSettings userSettings) {
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
        Cache.getInstance().setSettings(userSettings);
    }
}
