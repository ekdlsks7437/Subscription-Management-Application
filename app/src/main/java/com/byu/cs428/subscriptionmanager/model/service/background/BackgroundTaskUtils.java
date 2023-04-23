package com.byu.cs428.subscriptionmanager.model.service.background;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundTaskUtils {
    public static void runTask(Runnable task){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(task);
    }
}
