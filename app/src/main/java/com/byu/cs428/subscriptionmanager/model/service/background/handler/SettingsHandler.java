package com.byu.cs428.subscriptionmanager.model.service.background.handler;

import static com.byu.cs428.subscriptionmanager.model.service.background.getSettingsTask.SETTINGS_KEY;

import android.os.Bundle;

import com.byu.cs428.subscriptionmanager.Cache.Cache;
import com.byu.cs428.subscriptionmanager.model.domain.UserSettings;
import com.byu.cs428.subscriptionmanager.presenter.SettingsPresenter;

public class SettingsHandler extends BackgroundTaskHandler<SettingsPresenter.SettingsObserver> {
    public SettingsHandler(SettingsPresenter.SettingsObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(SettingsPresenter.SettingsObserver observer, Bundle data) {
        UserSettings settings = (UserSettings) data.getSerializable(SETTINGS_KEY);
        observer.handleSuccess(settings);
    }
}
