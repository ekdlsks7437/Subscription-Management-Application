package com.byu.cs428.subscriptionmanager.presenter;

import com.byu.cs428.subscriptionmanager.Cache.Cache;
import com.byu.cs428.subscriptionmanager.model.domain.UserSettings;
import com.byu.cs428.subscriptionmanager.model.service.SettingsService;

public class SettingsPresenter extends BasePresenter<SettingsPresenter.SettingsView>{

    public static interface SettingsView extends BaseView{
        void settingsFinished();
    }

    public SettingsService settingsService(){
        return new SettingsService();
    }

    public SettingsPresenter(SettingsView view) {
        super(view);
    }

    public void CreateSettings(String notificationTime, String currency){
        settingsService().CreateSettings(new SettingsObserver(),new UserSettings(notificationTime,currency));
    }

    public void editSettings(String notificationTime, String currency){
        settingsService().UpdateUserSettings(new SettingsObserver(),new UserSettings(notificationTime,currency));
    }

    public void getUserSettings(){
        settingsService().getUserSettings(new SettingsObserver());
    }

    public class SettingsObserver extends Observer{

        public void handleSuccess(UserSettings settings) {
            Cache.getInstance().setSettings(settings);
            view.settingsFinished();
            //TODO: load the userSettings and set them when we set a notification or currency icon.
        }

        @Override
        protected String getDescription() {
            return "Settings";
        }
    }

    @Override
    protected void failMessage(String failureType, String message) {

    }

    @Override
    protected void exceptionMessage(String exceptionType, Exception ex) {

    }

}
