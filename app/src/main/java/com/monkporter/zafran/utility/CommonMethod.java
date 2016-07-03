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

    public static void makeToast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

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
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }





    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }








    public static String getFilename(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Welnus");

        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + fileName);
        return uriSting;
    }

    public static String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = mcontext.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }



    public static void cleanDeviceData() {
     /*   // Clean temporary order data after order complete
        WelNus.getInstance().setAddNotes(null);
        WelNus.getInstance().setSellerDetails(null);
        WelNus.getInstance().setBuyerAddressModule(null);*/
    }

    public static void cleanProductDeviceData(Context context) {
        /*// Clean temporary order data after order complete
        WelNus.getInstance().setBuyerAddressModule(null);
        WelNus.getInstance().setQuantityHashMap(null);
        MyCartDBHelper myCartDBHelper = new MyCartDBHelper(context);
        myCartDBHelper.open();
        myCartDBHelper.deleteCart();
        myCartDBHelper.close();*/
    }
}
