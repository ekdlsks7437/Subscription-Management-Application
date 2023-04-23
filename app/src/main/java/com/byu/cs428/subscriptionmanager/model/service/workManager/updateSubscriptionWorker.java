package com.byu.cs428.subscriptionmanager.model.service.workManager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class updateSubscriptionWorker extends Worker {
    public updateSubscriptionWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // TODO: Implement it so we have a way to call this class to update the cost and notification
        //  date of the subscription in the background, then send it to the database to update it there.

        //TODO: this might need to be changed out to a AlarmManager, or even a Service, but I'm not
        // sure exactly which would be the best for our use-case yet. I'm going to look at the tutorial
        // from google on the android-work-manager. https://developer.android.com/codelabs/android-workmanager-java#0


        // called on the day of the subscriptions renewal
        // updates the subscription on device
        // calls update service to update info in db

        return null;
    }

    // create notification and push notification to device of subscription renewing
}
