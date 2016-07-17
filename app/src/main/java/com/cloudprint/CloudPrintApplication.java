package com.cloudprint;

import android.app.Application;

public class CloudPrintApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CloudPrint.appInit(this);
    }
}
