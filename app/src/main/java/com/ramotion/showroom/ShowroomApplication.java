package com.ramotion.showroom;

import android.app.Application;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.Tracker;
import com.squareup.leakcanary.LeakCanary;

public class ShowroomApplication extends Application {

//    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

    }

    /*synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // Чтобы включить ведение журнала отладки, используйте adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker("global_tracker");
        }
        return mTracker;
    }*/

}