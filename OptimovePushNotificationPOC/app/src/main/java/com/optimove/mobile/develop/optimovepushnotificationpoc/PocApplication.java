package com.optimove.mobile.develop.optimovepushnotificationpoc;

import android.util.Log;

import org.piwik.sdk.DownloadTracker;
import org.piwik.sdk.Piwik;
import org.piwik.sdk.PiwikApplication;
import org.piwik.sdk.TrackHelper;
import org.piwik.sdk.Tracker;

import java.net.MalformedURLException;

import timber.log.Timber;

/**
 * Created by yossi_c on 23/1/2017.
 */

public class PocApplication extends PiwikApplication {
    private static final String TAG = "opti-MsgService";
    private Tracker mPiwikTracker;
    public  PocApplication(){


    }
    @Override
    public String getTrackerUrl() {
        return "http://ec2-54-227-46-198.compute-1.amazonaws.com";
    }

    @Override
    public Integer getSiteId() {
        return 1;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        initPiwik();
    }

    public synchronized Tracker getTracker() {
        if (mPiwikTracker != null) {
            return mPiwikTracker;
        }

        try {
            mPiwikTracker = Piwik.getInstance(this).newTracker("http://ec2-54-227-46-198.compute-1.amazonaws.com", 1);
        } catch (MalformedURLException e) {
            Log.w(TAG, "url is malformed", e);
            return null;
        }

        return mPiwikTracker;
    }


    private void initPiwik() {
        // Print debug output when working on an app.
        Timber.plant(new Timber.DebugTree());

        // When working on an app we don't want to skew tracking results.
        //getPiwik().setDryRun(BuildConfig.DEBUG);

        // If you want to set a specific userID other than the random UUID token, do it NOW to ensure all future actions use that token.
        // Changing it later will track new events as belonging to a different user.
        // String userEmail = ....preferences....getString
         getTracker().setUserId("yossi.cohn@gmail.com");
         getTracker().setDispatchInterval(10);
        // Track this app install, this will only trigger once per app version.
        // i.e. "http://com.piwik.demo:1/185DECB5CFE28FDB2F45887022D668B4"
        TrackHelper.track().download().identifier(DownloadTracker.Extra.APK_CHECKSUM).with(getTracker());
        // Alternative:
        // i.e. "http://com.piwik.demo:1/com.android.vending"
        // getTracker().download();
    }

}
