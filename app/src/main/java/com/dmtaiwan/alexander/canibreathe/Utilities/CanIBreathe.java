package com.dmtaiwan.alexander.canibreathe.Utilities;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by lenovo on 11/15/2015.
 */
public class CanIBreathe extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "LjHPYqZjwGsySkallPNtszswbIgjgaWyk8U2qTAm", "PA8BJlK2HVFRDggiikzdow8uaGHOdBhArXBlHkiL");
    }
}
