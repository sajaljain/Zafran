package com.monkporter.zafran.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.monkporter.zafran.Interface.ApiInterface;
import com.monkporter.zafran.R;

import com.monkporter.zafran.Zafran;
import com.monkporter.zafran.helper.FetchUserEmail;
import com.monkporter.zafran.helper.PrefManager;
import com.monkporter.zafran.model.TemporaryUser;
import com.monkporter.zafran.model.TemporaryUserResponse;
import com.monkporter.zafran.model.UpdateFcm;
import com.monkporter.zafran.model.UpdateFcmResponse;

import com.monkporter.zafran.rest.ApiClient;
import com.monkporter.zafran.utility.CommonMethod;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private static int SPLASH_TIME_OUT = 1000;
    private Handler handler;

    //TempUserRequest tempUserRequest;

    //private String[] locationPermission = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
    private String[] MY_PERMISSIONS = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.READ_SMS, android.Manifest.permission.GET_ACCOUNTS};
    private final int PERMISSIONS_REQUEST = 111;
    PrefManager prefManager;
    private String userName, emailID, deviceRegId, currentTimeMillis;
    private int userId, regType;
    //0=no-email[default] 1=email 2=mobile 3=facebook 4=linkedIn
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();

        if (CommonMethod.isNetworkAvailable(Splash.this)) {

            if (Build.VERSION.SDK_INT < 23) {
                isFCMIdPresent();
            } else {
                permissionCheck();
            }
        } else {
            startRefreshActivity();
        }

    }

    private void init() {
        prefManager = PrefManager.getInstance(Splash.this);
        handler = new Handler();
    }


    private void permissionCheck() {
        // int location = ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int receiveSms = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS);
        int readSms = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS);
        int fetchEmail = ContextCompat.checkSelfPermission(this, android.Manifest.permission.GET_ACCOUNTS);

        if (storage != PackageManager.PERMISSION_GRANTED ||
                receiveSms != PackageManager.PERMISSION_GRANTED || readSms != PackageManager.PERMISSION_GRANTED ||
                fetchEmail != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(Splash.this, "Requesting Permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, MY_PERMISSIONS, PERMISSIONS_REQUEST);
        } else {
            Toast.makeText(Splash.this, "Permission already granted", Toast.LENGTH_SHORT).show();

            isFCMIdPresent();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST) {
            Log.d(TAG, "Received response for permissions request.");
            Toast.makeText(Splash.this, "Received response for permissions request.", Toast.LENGTH_SHORT).show();

            // We have requested multiple permissions for contacts, so all of them need to be
            // checked.
            if (verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
                Toast.makeText(Splash.this, "Permissions were granted.", Toast.LENGTH_SHORT).show();

                isFCMIdPresent();
            } else {
                Log.d(TAG, "Permissions were NOT granted.");
                Toast.makeText(Splash.this, "Permissions were NOT granted.", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
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


    private int requestCreateTemporaryUser() {


        PrefManager prefManager = PrefManager.getInstance(Zafran.getInstance());
        //Here Sending post request for user
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        deviceRegId = prefManager.getDeviceRegId();

        emailID = FetchUserEmail.getEmail(this);

        if (emailID == null && deviceRegId == null) {
            //Tell user to restart the app, as both email and device id are missing
            return 400;

        }
        //here the activity will only come after either its having a email id or its having a deviceRegId or both

        if (emailID == null && deviceRegId != null) {
            regType = 0;
        } else {
            regType = 1;
        }
        userId = prefManager.getUserId();

        if (userId == 0) {
            TemporaryUser temporaryUser = new TemporaryUser();
            temporaryUser.setRegistrationChannelTypeID(regType);
            temporaryUser.setCell(null);
            temporaryUser.setFirstName(null);
            temporaryUser.setLastName(null);
            Time time = new Time();
            time.setToNow();
            currentTimeMillis = Long.toString(time.toMillis(false));
            userName = "temp_" + currentTimeMillis;
            temporaryUser.setUserName(userName);
            temporaryUser.setSex(1);
            temporaryUser.setSocialMediaUserId(null);
            temporaryUser.setEmailId(emailID);
            temporaryUser.setDeviceRegistrationID(deviceRegId);


            //Logging the Request Input
            FirebaseCrash.logcat(Log.DEBUG, TAG, "Request for Creating a temporary user");
            FirebaseCrash.logcat(Log.DEBUG, TAG, "Cell " + temporaryUser.getCell());
            FirebaseCrash.logcat(Log.DEBUG, TAG, "FirstName " + temporaryUser.getFirstName());
            FirebaseCrash.logcat(Log.DEBUG, TAG, "Last Name " + temporaryUser.getLastName());
            FirebaseCrash.logcat(Log.DEBUG, TAG, "Reg type id " + temporaryUser.getRegistrationChannelTypeID());
            FirebaseCrash.logcat(Log.DEBUG, TAG, "User Name " + temporaryUser.getUserName());
            FirebaseCrash.logcat(Log.DEBUG, TAG, "Social Media Id " + temporaryUser.getSocialMediaUserId());
            FirebaseCrash.logcat(Log.DEBUG, TAG, "Sex " + temporaryUser.getSex());
            FirebaseCrash.logcat(Log.DEBUG, TAG, "Email Id " + temporaryUser.getEmailId());
            FirebaseCrash.logcat(Log.DEBUG, TAG, "FCM Id " + temporaryUser.getDeviceRegistrationID());

            Call<TemporaryUserResponse> call = apiService.createTemporaryUser(temporaryUser);

            try {
                TemporaryUserResponse temporaryUserResponse = call.execute().body();
                FirebaseCrash.logcat(Log.DEBUG, TAG, "Response of Creating a temporary user");
                FirebaseCrash.logcat(Log.DEBUG, TAG, "Temporary user error = " + temporaryUserResponse.isError());
                FirebaseCrash.logcat(Log.DEBUG, TAG, "Temporary user message = " + temporaryUserResponse.getMessage());
                FirebaseCrash.logcat(Log.DEBUG, TAG, "Temporary user userID = " + temporaryUserResponse.getUserId());

                if (temporaryUserResponse.isError()) {
                    FirebaseCrash.logcat(Log.DEBUG, TAG, "Temporary user creation failed");
                    FirebaseCrash.report(new Exception("Temporary user creation failed"));
                    //Error occurred from server side, incapable of creating a user on server
                    return 201;
                } else {
                    FirebaseCrash.logcat(Log.DEBUG, TAG, "Temporary user creation Success");
                    userId = temporaryUserResponse.getUserId();
                    prefManager.setUserId(userId);
                    // here we cannot return true because now we will make the next call to update the FCM id
                }

            } catch (IOException e) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "Some n/w error in device " + e.getMessage());
                FirebaseCrash.report(new Exception("Some n/w error in device"));
                //some network error occurred
                return 500;
            }

        }
        if (prefManager.getDeviceRegId() != null) {

            requestUpdateFcmId();

        }

        return 200;

    }

    private void requestUpdateFcmId() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        UpdateFcm updateFcm = new UpdateFcm();
        updateFcm.setDeviceRegistrationId(deviceRegId);
        updateFcm.setUserId(userId);
        FirebaseCrash.logcat(Log.DEBUG, TAG, "Request for updating device registration id");
        FirebaseCrash.logcat(Log.DEBUG, TAG, "Device Registration Id " + updateFcm.getDeviceRegistrationId());
        FirebaseCrash.logcat(Log.DEBUG, TAG, "User Id " + updateFcm.getUserId());


        Call<UpdateFcmResponse> call = apiService.updateFCM(updateFcm);
        try {
            UpdateFcmResponse updateFcmResponse = call.execute().body();
            FirebaseCrash.logcat(Log.DEBUG, TAG, "Response for updating device registration id");
            FirebaseCrash.logcat(Log.DEBUG, TAG, "UpdateFcm message =" + updateFcmResponse.getMessage());

            if (updateFcmResponse.isError()) {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "FCM cannot be updated");
                FirebaseCrash.report(new Exception("FCM cannot be updated"));
                //fcm id cannot be updated
            } else {
                FirebaseCrash.logcat(Log.DEBUG, TAG, "FCM ID updated success-fully on server");
            }
        } catch (IOException e) {
            FirebaseCrash.logcat(Log.DEBUG, TAG, "Some n/w error in device " + e.getMessage());
            FirebaseCrash.report(new Exception("Some n/w error in device"));
        }
    }


    private void isFCMIdPresent() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String registrationId;
                int count = 0;
                while(true){
                    registrationId = prefManager.getDeviceRegId();
                    if (registrationId != null && !registrationId.equalsIgnoreCase("")) {
                        if(count == 0)
                        {
                            Log.i(TAG,"I am here because i am revisiting");
                            try {
                                Thread.sleep(SPLASH_TIME_OUT);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                        Log.i(TAG,"I am issuing");
                        handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    new NetworkCalls().execute();
                                }
                            });

                    break;
                    }
                    count++;
                }
            }

        }).start();
    }

    private void startMainActivity() {
        Intent refreshIntent = new Intent(Splash.this, MainActivity.class);
        refreshIntent.putExtra("previousScreen", "splashScreen");
        startActivity(refreshIntent);
        overridePendingTransition(R.anim.start_activity_slide_in_left, R.anim.start_activity_slide_out_right);
        Splash.this.finish();
    }

    class NetworkCalls extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {

            return requestCreateTemporaryUser();
        }

        @Override
        protected void onPostExecute(Integer res) {


            switch (res) {
                //every thing is fine
                case 200:
                    startMainActivity();
                    break;
                //Error occurred from server side, incapable of creating a user on server
                case 201:
                    Toast.makeText(Splash.this, "Oops!!! Our servers are sipping a tea.", Toast.LENGTH_LONG).show();
                    startRefreshActivity();
                    break;
                //Tell user to restart the app, as both email and device id are missing
                case 400:
                    Toast.makeText(Splash.this, "Oops!!! Insufficient Permissions", Toast.LENGTH_LONG).show();
                    startRefreshActivity();
                    break;
                //Network exception occurred
                case 500:
                    Toast.makeText(Splash.this, "Oops!!! Network congestion.", Toast.LENGTH_LONG).show();
                    startRefreshActivity();
                    break;
                default:
                    Toast.makeText(Splash.this, "Sorry For inconvenience's.", Toast.LENGTH_LONG).show();
                    Splash.this.finish();
            }
        }
    }

    private void startRefreshActivity() {
        Intent refreshIntent = new Intent(Splash.this, Refresh.class);
        refreshIntent.putExtra("previousScreen", "splash");
        startActivityForResult(refreshIntent, 1001);
        overridePendingTransition(R.anim.start_activity_slide_in_left, R.anim.start_activity_slide_out_right);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001 && resultCode == RESULT_OK ){

            if (Build.VERSION.SDK_INT < 23) {
                isFCMIdPresent();
            } else {
                permissionCheck();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Splash.this.finish();
    }
}