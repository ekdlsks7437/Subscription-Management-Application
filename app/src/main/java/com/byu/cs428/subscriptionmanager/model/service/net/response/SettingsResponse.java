package com.byu.cs428.subscriptionmanager.model.service.net.response;

import com.byu.cs428.subscriptionmanager.model.domain.UserSettings;
import com.byu.cs428.subscriptionmanager.model.service.net.ServerResponseSettings;

public class SettingsResponse extends Response{

        private ServerResponseSettings targetSetting;

        public SettingsResponse(boolean success, ServerResponseSettings userSettings) {
            super(success, null);
            this.targetSetting = userSettings;
        }

        public ServerResponseSettings getUserSettings() {
            return targetSetting;
        }
    }
