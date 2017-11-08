package com.ramotion.showroom;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class ShowroomApplication extends Application {
    private Tracker mTracker;

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // Чтобы включить ведение журнала отладки, используйте adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker("global_tracker");
        }
        return mTracker;
    }

}