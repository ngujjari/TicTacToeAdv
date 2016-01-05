package com.mobileapps.ngujjari.ticatacapp;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by ngujjari on 2/21/15.
 */
public class TicTacToeApp extends Application {

    private Tracker mTracker;

    public TicTacAbstractController getMs() {
        return ms;
    }

    public void setMs(TicTacAbstractController ms) {
        this.ms = ms;
    }

    private TicTacAbstractController ms = null;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

}
