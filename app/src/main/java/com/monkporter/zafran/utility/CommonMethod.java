package com.monkporter.zafran.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;

import android.graphics.BitmapFactory;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.view.Gravity;
import android.widget.Toast;






import java.io.File;


public class CommonMethod {
    static Context mcontext = null;

    public CommonMethod(Context context) {
        mcontext = context;
    }

    /**
     * Print Toast
     */


    /**
     * AlertDialogue
     */

    public static void showAlert(String message, Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static boolean isNetworkAvailable(Context mcontext) {
        ConnectivityManager connectivity = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                return true;
            } else {
                return false;
            }
        }
    }

}
