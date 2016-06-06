package com.id11236662.gokeigo.util;

import android.app.Application;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * This class initialises the FlowManager for FBFlow so it can read assets, observe content
 * and generate ContentProvider.
 */
public class GoKeigoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // DB is initialised immediately, not when
        // FlowManager.getDatabase(...).getWritableDatabase() is called.

        FlowManager.init(new FlowConfig.Builder(this)
                .openDatabasesOnInit(true).build());

        Log.d(Constants.TAG, "DB Version: " + FlowManager.getWritableDatabase(GoKeigoDatabase.class).getVersion());
    }
}
