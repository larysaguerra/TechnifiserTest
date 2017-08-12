package com.technifiser;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.Locale;

/**
 * Created by guerra on 10/08/17.
 * Util base application
 */

public class TecnifiserApplication extends Application {


    private static TecnifiserApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mContext = this;
    }

    public static TecnifiserApplication getInstance() {
        return mContext;
    }

    /**
     * Return device language
     */
    public static String getLanguage() {
        String language = "en";
        if (Locale.getDefault().getLanguage().equals("es")) {
            language = "es";
        }
        return language;
    }

}
