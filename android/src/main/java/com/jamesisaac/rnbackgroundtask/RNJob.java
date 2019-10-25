package com.jamesisaac.rnbackgroundtask;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.facebook.react.HeadlessJsTaskService;


import java.util.List;

/**
 * The single task which this library is able to schedule.
 *
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
        if (isAppOnForeground(context)) {
            Log.d(TAG, "Job is running");
            Intent serviceIntent = new Intent(context, HeadlessTaskService.class);
            context.startService(serviceIntent);
            HeadlessJsTaskService.acquireWakeLockNow(context);
        }
        Log.d(TAG, "Job running aborted as app is in foreground");
        return Result.success();
    }

    private boolean isAppOnForeground(Context context) {
        /**
         We need to check if app is in foreground otherwise the app will crash.
         http://stackoverflow.com/questions/8489993/check-android-application-is-in-foreground-or-not
         **/
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses =
                activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance ==
                    ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                    appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

}
