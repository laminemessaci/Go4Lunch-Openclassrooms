package com.lamine.go4lunch;


import androidx.multidex.MultiDexApplication;

import com.google.android.libraries.places.api.Places;

import static com.lamine.go4lunch.Utils.Constants.APIKey;

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Places.initialize(this, APIKey);


    }
}