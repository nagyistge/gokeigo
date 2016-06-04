package com.id11236662.gokeigo.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * // TODO:
 */
public class ActivityConfigurator {

    /**
     * Check if the device has any network connectivity
     * @param activity
     * @return true is there is network connectivity. False if there is none.
     */
    public static boolean isDeviceOnline(Activity activity) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
