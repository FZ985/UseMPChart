package com.usempchart;

import android.app.Application;

/**
 * Create by JFZ
 * date: 2020-05-29 10:21
 **/
public class BaseApp extends Application {

    private static BaseApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static BaseApp getInstance() {
        return app;
    }
}
