package com.riskwizard.incidentlogger.helper;

import android.app.Activity;
import android.util.Log;

/**
 * Created by mattg on 5/02/2015.
 */
public class LogHelper {

    public final static String LOG_TAG = "ACTIVITY_EVENT";

    public static void LogCallback(Activity activity, String callbackName)
    {
        String logMsg = String.format("Activity:%s | Callback:%s", activity.getClass().getSimpleName(), callbackName);
        Log.d(LOG_TAG, logMsg);
    }

}

