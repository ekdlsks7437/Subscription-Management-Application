package com.byu.cs428.subscriptionmanager.model.service;

import com.byu.cs428.subscriptionmanager.Cache.Cache;
import com.byu.cs428.subscriptionmanager.model.domain.UserSettings;
import com.byu.cs428.subscriptionmanager.model.service.background.AddSettingsTask;
import com.byu.cs428.subscriptionmanager.model.service.background.BackgroundTaskUtils;
import com.byu.cs428.subscriptionmanager.model.service.background.getSettingsTask;
import com.byu.cs428.subscriptionmanager.model.service.background.handler.AuthenticatedUserHandler;
import com.byu.cs428.subscriptionmanager.model.service.background.handler.SettingsHandler;
import com.byu.cs428.subscriptionmanager.model.service.net.request.UpdateSettingsTask;
import com.byu.cs428.subscriptionmanager.presenter.SettingsPresenter;

public class SettingsService {
    public SettingsService(){
    }

    public void CreateSettings(SettingsPresenter.SettingsObserver observer, UserSettings userSettings){
        BackgroundTaskUtils.runTask(new AddSettingsTask(Cache.getInstance().getAuthToken(), new SettingsHandler(observer), Cache.getInstance().getUser(), userSettings));
    }

    public void UpdateUserSettings(SettingsPresenter.SettingsObserver observer, UserSettings userSettings){
        BackgroundTaskUtils.runTask(new UpdateSettingsTask(Cache.getInstance().getAuthToken(), new SettingsHandler(observer), Cache.getInstance().getUser(), userSettings));
    }

    public void getUserSettings(SettingsPresenter.SettingsObserver observer){
        BackgroundTaskUtils.runTask(new getSettingsTask(Cache.getInstance().getAuthToken(), Cache.getInstance().getUser(), new SettingsHandler(observer)));
    }
}
