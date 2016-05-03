package com.excilys.android.formation.chatlite_rev.adapters;

import android.os.Looper;

import com.excilys.android.formation.chatlite_rev.BuildConfig;

/**
 * Created by excilys on 03/05/16.
 */
public class ThreadPreconditions {
    public static void checkOnMainThread() {
        if (BuildConfig.DEBUG) {
            if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
                throw new IllegalStateException("This method should be called from the Main Thread");
            }
        }
    }
}

