package com.lamine.go4lunch;



import androidx.multidex.MultiDexApplication;

import com.google.android.libraries.places.api.Places;

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Places.initialize(this, "AIzaSyB6npGzQpiEdM7mSaqSu_XUhFb-gh9EOeA");

    }
}