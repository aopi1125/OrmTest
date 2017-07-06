package com.harbor.db;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by fish on 2017/7/6.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
