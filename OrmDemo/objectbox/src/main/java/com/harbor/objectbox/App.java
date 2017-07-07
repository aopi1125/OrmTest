package com.harbor.objectbox;

import android.app.Application;
import android.os.Environment;

import java.io.File;

import io.objectbox.BoxStore;

public class App{
    public static final String TAG = "ObjectBoxExample";
    public static final boolean EXTERNAL_DIR = false;

    private static BoxStore boxStore;

    public static void onCreate(Application context) {

//        if (EXTERNAL_DIR) {
//            // Example how you could use a custom dir in "external storage"
//            // (Android 6+ note: give the app storage permission in app info settings)
//            File directory = new File(Environment.getExternalStorageDirectory(), "objectbox-notes");
//            boxStore = MyObjectBox.builder().androidContext(App.this).directory(directory).build();
//        } else {
        // This is the minimal setup required on Android
        boxStore = MyObjectBox.builder().androidContext(context).build();
//        }
    }

    public static BoxStore getBoxStore() {
        return boxStore;
    }
}
