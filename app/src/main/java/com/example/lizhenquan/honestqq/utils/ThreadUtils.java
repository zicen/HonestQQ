package com.example.lizhenquan.honestqq.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by lizhenquan on 2017/1/17.
 */

public class ThreadUtils {
    private static  Executor sExecutor = Executors.newSingleThreadExecutor();
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    public static void runOnSubThread(Runnable runnable) {
        sExecutor.execute(runnable);
    }
    public static  void runOnUIThread(Runnable runnable){
        sHandler.post(runnable);
    }
}
