package com.harbor.db;

import com.harbor.greendao.App;

import io.realm.Realm;

/**
 * Created by Harbor on 2017/7/6.
 */

public class MyApplication extends App {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        com.harbor.objectbox.App.onCreate(this);
    }
}
