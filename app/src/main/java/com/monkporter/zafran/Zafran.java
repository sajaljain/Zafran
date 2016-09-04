package com.monkporter.zafran;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;
/**
 * Created by Sajal on 25-Mar-16.
 */
public class Zafran extends Application {
    private static final String TAG = Zafran.class.getSimpleName();
    private static Zafran mInstance;
    @Override
    public void onCreate() {
        super.onCreate();


    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static synchronized Zafran getInstance() {

        if (mInstance == null) {
            // Create the instance
            mInstance = new Zafran();
        }

        return mInstance;
    }
}
