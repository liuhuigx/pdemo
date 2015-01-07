
package com.example.pdemo;

import com.example.pdemo.util.Utils;

import android.app.Application;

public class DemoApp extends Application {

    public static CacheManager mcm;

    @Override
    public void onCreate() {
        // load caches from files.
        long start = System.currentTimeMillis();
        mcm = new CacheManager(this);
        Utils.log("DemoApp onCreate cost: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DemoApp.mcm.clear();
    }
}
