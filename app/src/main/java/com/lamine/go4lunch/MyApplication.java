package com.lamine.go4lunch;


import androidx.multidex.MultiDexApplication;

import com.batch.android.Batch;
import com.batch.android.BatchActivityLifecycleHelper;
import com.batch.android.Config;
import com.google.android.libraries.places.api.Places;

import static com.lamine.go4lunch.Utils.Constants.APIKey;

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Places.initialize(this, APIKey);

        // Batch.setConfig(new Config("5EEE4AA262F4CFA78746AF71143F66")); // live
        Batch.setConfig(new Config("DEV5EEE4AA2637B19FD45FF7F5B4A8")); // development
        registerActivityLifecycleCallbacks(new BatchActivityLifecycleHelper());
        // You should configure your notification's customization options here.
        // Not setting up a small icon could cause a crash in applications created with Android Studio 3.0 or higher.
        // More info in our "Customizing Notifications" documentation
        // Batch.Push.setSmallIconResourceId(R.drawable.ic_notification_icon);
    }
}