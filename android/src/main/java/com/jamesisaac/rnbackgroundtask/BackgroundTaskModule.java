package com.jamesisaac.rnbackgroundtask;

import android.util.Log;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;

import java.util.concurrent.TimeUnit;

public class BackgroundTaskModule extends ReactContextBaseJavaModule {

    private static final String TAG = "BackgroundTask";
    private PeriodicWorkRequest backgroundTaskRequest = null;


    public BackgroundTaskModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    /**
     * Set the name of the native module which can be imported from JS
     */
    @Override
    public String getName() {
        return "BackgroundTask";
    }

    @Override
    public void initialize() {
        Log.d(TAG, "Initializing");
        super.initialize();

        backgroundTaskRequest = new PeriodicWorkRequest.Builder(RNJob.class, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * Main point of interaction from JS users - allows them to specify the scheduling etc for the
     * background task.
     * <p>
     * Default values are specified in JS (more accessible for the average user).
     *
     * @param config the config options passed in from JS:
     *               - period (required): how frequently to carry out the task in seconds
     *               - timeout (required): after how many seconds should the task be auto-killed
     */
    @ReactMethod
    public void schedule(final ReadableMap config) {
        Log.d(TAG, "@ReactMethod BackgroundTask.schedule");

        WorkManager.getInstance()
                .enqueue(backgroundTaskRequest);
    }

    /**
     * Allow the JS users to cancel the previously scheduled task.
     */
    @ReactMethod
    public void cancel() {
        Log.d(TAG, "@ReactMethod BackgroundTask.cancel");

        WorkManager.getInstance().cancelWorkById(backgroundTaskRequest.getId());
    }
}