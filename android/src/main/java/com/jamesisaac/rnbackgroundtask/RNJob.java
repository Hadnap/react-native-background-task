package com.jamesisaac.rnbackgroundtask;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.facebook.react.HeadlessJsTaskService;

/**
 * The single task which this library is able to schedule.
 * <p>
 * The sole purpose here is to kick off the HeadlessTaskService, and pass along the config params
 * which were used when creating the job.
 */

public class RNJob extends Worker {
    private static final String TAG = "BackgroundTask";

    public RNJob(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        Log.d(TAG, "Job is running");
        Intent serviceIntent = new Intent(context, HeadlessTaskService.class);
        context.startService(serviceIntent);
        HeadlessJsTaskService.acquireWakeLockNow(context);
        return Result.success();
    }

}
