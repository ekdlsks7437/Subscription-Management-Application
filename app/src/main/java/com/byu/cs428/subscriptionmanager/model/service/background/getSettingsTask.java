package com.byu.cs428.subscriptionmanager.model.service.background;

import android.os.Bundle;
import android.os.Handler;

import com.byu.cs428.subscriptionmanager.model.domain.AuthToken;
import com.byu.cs428.subscriptionmanager.model.domain.User;
import com.byu.cs428.subscriptionmanager.model.domain.UserSettings;
import com.byu.cs428.subscriptionmanager.model.service.net.SendRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.request.SettingsRequest;
import com.byu.cs428.subscriptionmanager.model.service.net.response.SettingsResponse;

import java.io.Serializable;

public class getSettingsTask extends AuthenticatedTask {

    private static final String URL_PATH = "/setting/settings";

    public static final String SETTINGS_KEY = "settings";

    private User user;
    private AuthToken authToken;
    private UserSettings userSettings;

    public getSettingsTask(AuthToken authToken, User user, Handler handler) {
        super(authToken, handler);
        this.user = user;
        this.authToken = authToken;
    }

    @Override
    public void runBackgroundTask() {
        SettingsRequest request = new SettingsRequest(user,authToken);
        SettingsResponse response = getServerFacade().getSettings(request,URL_PATH);
        if(response.isSuccess()){
            userSettings = response.getUserSettings().getUserSettings();
            sendSuccessMessage();
        }
        else {
            sendFailureMessage(response.getMessage());
        }
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        super.loadSuccessBundle(msgBundle);
        msgBundle.putSerializable(SETTINGS_KEY, (Serializable) userSettings);
    }
}