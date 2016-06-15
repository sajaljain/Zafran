package com.monkporter.zafran;

import android.app.Application;
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

    public static synchronized Zafran getInstance() {
        Log.i(TAG, "getInstance =" + mInstance);
        if (mInstance == null) {
            // Create the instance
            mInstance = new Zafran();
        }
        Log.i(TAG, "getInstance =" + mInstance);
        return mInstance;
    }
}
