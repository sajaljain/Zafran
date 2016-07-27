package com.monkporter.zafran.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.monkporter.zafran.Interfece.TempUserRequest;
import com.monkporter.zafran.Manifest;
import com.monkporter.zafran.R;
import com.monkporter.zafran.model.TemporaryUser;
import com.monkporter.zafran.model.TemporaryUserResponse;
import com.monkporter.zafran.pushnotification.MyFirebaseInstanceIDService;
import com.monkporter.zafran.rest.TempUserApiClient;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {
    private static final String TAG = "Splash";
    private static int SPLASH_TIME_OUT = 3000;
    private PreferenceManager pm;
    Context context;
    String dveiceRegistrationToken;
    TempUserRequest tempUserRequest;
    private String[] locationPermission = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
    private String[] MY_PERMISSIONS = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECEIVE_SMS ,android.Manifest.permission.READ_SMS};


    private final int PERMISSIONS_REQUEST = 111;
    String usrNAme;
    Intent intent;
    ProgressBar pb;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dveiceRegistrationToken = new MyFirebaseInstanceIDService().getRefreshedToken();
        intent = new Intent(this, MainActivity.class);
        TemporaryUser temporaryUser = new TemporaryUser();
        temporaryUser.setRegistrationChannelTypeID(0);
        temporaryUser.setCell("");
        temporaryUser.setFirstName("");
        temporaryUser.setLastName("");
        String mytime = java.text.DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());
        String mydate =java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        usrNAme = "temp_"+mydate+"_"+mytime;
        temporaryUser.setUserName(usrNAme);
        temporaryUser.setSex(1);
        temporaryUser.setSocialMediaUserId("");
        temporaryUser.setEmailId("");
        temporaryUser.setDeviceRegistrationIDDeviceRegistrationID(dveiceRegistrationToken);
        temporaryUser.setCellVerified(0);

        // Here Sending post request for user
        tempUserRequest = TempUserApiClient.getClient().create(TempUserRequest.class);
        Call<TemporaryUserResponse> call = tempUserRequest.getResponse(temporaryUser);
        call.enqueue(new Callback<TemporaryUserResponse>() {
            @Override
            public void onResponse(Call<TemporaryUserResponse> call, Response<TemporaryUserResponse> response) {
                int status = response.code();
                TemporaryUserResponse temporaryUserResponse = response.body();
            }

            @Override
            public void onFailure(Call<TemporaryUserResponse> call, Throwable t) {
                Log.d("Temporary user","onFailure ="+t.getMessage());
            }
        });
        pb = (ProgressBar) findViewById(R.id.progressBar);
        if (Build.VERSION.SDK_INT < 23) {
            Toast.makeText(Splash.this, "Below 23", Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                            Splash.this.finish();
                        }
                    });
                }

            }).start();
        } else {
            permissionCheck();
        }
    }

    private void permissionCheck() {
        int location = ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int receiveSms = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS);
        int readSms = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS);



        if ( storage != PackageManager.PERMISSION_GRANTED ||
        receiveSms != PackageManager.PERMISSION_GRANTED ||readSms != PackageManager.PERMISSION_GRANTED ) {
            Toast.makeText(Splash.this, "Requesting Permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, MY_PERMISSIONS,PERMISSIONS_REQUEST);
        }
        else {
            Toast.makeText(Splash.this, "Permission alredy granted", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            Splash.this.finish();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST) {
            Log.i(TAG, "Received response for permissions request.");
            Toast.makeText(Splash.this, "Received response for permissions request.", Toast.LENGTH_SHORT).show();

            // We have requested multiple permissions for contacts, so all of them need to be
            // checked.
            if (verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
                Toast.makeText(Splash.this, "Permissions were granted.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                Splash.this.finish();
            } else {
                Log.i(TAG, "Permissions were NOT granted.");
                Toast.makeText(Splash.this, "Permissions were NOT granted.", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean verifyPermissions(int[] grantResults) {
        if(grantResults.length < 1){
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}