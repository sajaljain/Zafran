package com.monkporter.zafran.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.monkporter.zafran.Interfece.TempUserRequest;
import com.monkporter.zafran.Interfece.UpdateFcmRequest;
import com.monkporter.zafran.R;
import com.monkporter.zafran.helper.FetchUserEmail;
import com.monkporter.zafran.helper.PrefManager;
import com.monkporter.zafran.model.TemporaryUser;
import com.monkporter.zafran.model.TemporaryUserResponse;
import com.monkporter.zafran.model.UpdateFcm;
import com.monkporter.zafran.model.UpdateFcmResponse;

import com.monkporter.zafran.rest.ApiClient;
import com.monkporter.zafran.rest.UpdateFcmApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private static int SPLASH_TIME_OUT = 3000;

    TempUserRequest tempUserRequest;
    //private String[] locationPermission = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
    private String[] MY_PERMISSIONS = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.READ_SMS, android.Manifest.permission.GET_ACCOUNTS};
    private final int PERMISSIONS_REQUEST = 111;

    Intent intent;
    PrefManager prefManager;
    private String userName, emailID, deviceRegId, currentTimeMillis;
    private int userId, regType;
    //0=no-email[default] 1=email 2=mobile 3=facebook 4=linkedIn
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //FirebaseCrash.report(new Exception("My first Android non-fatal error"));
        //progressDialog = new ProgressDialog(this);
        //progressDialog.setIndeterminate(true);
        //progressDialog.setMessage("Loading...");
        //progressDialog.show();
     /*   if (Build.VERSION.SDK_INT < 23) {
            requestCalls();
        }else{
            permissionCheck();
        }*/


        //pb = (ProgressBar) findViewById(R.id.progressBar);

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
            Toast.makeText(Splash.this, "Permission alredy granted", Toast.LENGTH_SHORT).show();

            requestCalls();
            //  if(progressDialog.isShowing())
            //    progressDialog.dismiss();
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
                //    if(progressDialog.isShowing())
                //      progressDialog.dismiss();
                // startActivity(intent);
                //Splash.this.finish();
                requestCalls();
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

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Resume");
        if (Build.VERSION.SDK_INT < 23) {
            requestCalls();
        } else {
            permissionCheck();
        }
    }

    private void requestCalls() {
        //
        intent = new Intent(this, MainActivity.class);
        prefManager = new PrefManager(Splash.this);

        deviceRegId = prefManager.getDeviceRegId();

        emailID = FetchUserEmail.getEmail(this);

        Log.d(TAG, "Email Id is " + emailID);
        Log.d(TAG, "Device Registration ID is " + deviceRegId);
        if (emailID == null && deviceRegId == null) {
            //restart this activity
            recreate();
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

            // Here Sending post request for user
            tempUserRequest = ApiClient.getClient().create(TempUserRequest.class);
            //Logging the Request Input
            FirebaseCrash.logcat(Log.INFO, TAG, "Request for Creating a temporary user");
            FirebaseCrash.logcat(Log.INFO, TAG, "Cell " + temporaryUser.getCell());
            FirebaseCrash.logcat(Log.INFO, TAG, "FirstName " + temporaryUser.getFirstName());
            FirebaseCrash.logcat(Log.INFO, TAG, "Last Name " + temporaryUser.getLastName());
            FirebaseCrash.logcat(Log.INFO, TAG, "Reg type id " + temporaryUser.getRegistrationChannelTypeID());
            FirebaseCrash.logcat(Log.INFO, TAG, "User Name " + temporaryUser.getUserName());
            FirebaseCrash.logcat(Log.INFO, TAG, "Social Media Id " + temporaryUser.getSocialMediaUserId());
            FirebaseCrash.logcat(Log.INFO, TAG, "Sex " + temporaryUser.getSex());
            FirebaseCrash.logcat(Log.INFO, TAG, "Email Id " + temporaryUser.getEmailId());
            FirebaseCrash.logcat(Log.INFO, TAG, "FCM Id " + temporaryUser.getDeviceRegistrationID());

            Call<TemporaryUserResponse> call = tempUserRequest.getResponse(temporaryUser);
            call.enqueue(new Callback<TemporaryUserResponse>() {
                @Override
                public void onResponse(Call<TemporaryUserResponse> call, Response<TemporaryUserResponse> response) {

                    TemporaryUserResponse temporaryUserResponse = response.body();
                    FirebaseCrash.logcat(Log.INFO, TAG, "Response of Creating a temporary user");
                    FirebaseCrash.logcat(Log.INFO, TAG, "Temporary user error = " + temporaryUserResponse.isError());
                    FirebaseCrash.logcat(Log.INFO, TAG, "Temporary user message = " + temporaryUserResponse.getMessage());
                    FirebaseCrash.logcat(Log.INFO, TAG, "Temporary user userID = " + temporaryUserResponse.getUserId());


                    if (temporaryUserResponse.isError()) {
                        FirebaseCrash.logcat(Log.INFO, TAG, "Temporary user creation failed");
                        FirebaseCrash.report(new Exception("Temporary user creation failed"));
                        Splash.this.finish();
                    } else {
                        FirebaseCrash.logcat(Log.INFO, TAG, "Temporary user creation Success");
                        userId = temporaryUserResponse.getUserId();
                        prefManager.setUserId(userId);
                    }

                }

                @Override
                public void onFailure(Call<TemporaryUserResponse> call, Throwable t) {
                    FirebaseCrash.logcat(Log.INFO, TAG, "Some n/w error in device");
                    FirebaseCrash.report(new Exception("Some n/w error in device"));

                    startActivity(new Intent(Splash.this, Refresh.class));

                }
            });
        }

        //  else{
        /*    CheckUserRequest checkUserRequest = CheckUserApiClient.getClient().create(CheckUserRequest.class);
            Call<CheckUser> call = checkUserRequest.getUserType();
            call.enqueue(new Callback<CheckUser>() {
                @Override
                public void onResponse(Call<CheckUser> call, Response<CheckUser> response) {
                    int status = response.code();
                    CheckUser checkUser = response.body();
                    Log.d("Check USer Type", "Response =" + status);
                    Log.d("Check User Type", "User Type =" + checkUser.isTempUser());
                    userType = prefManager.getIsTempUser();
                }

                @Override
                public void onFailure(Call<CheckUser> call, Throwable t) {
                    Log.d("Check user", "onFailure =" + t.getMessage());
                }
            });*/
        //}

        if (prefManager.getDeviceRegId() != null) {

            UpdateFcm updateFcm = new UpdateFcm();
            updateFcm.setDeviceRegistrationId(deviceRegId);
            updateFcm.setUserId(userId);
            FirebaseCrash.logcat(Log.INFO, TAG, "Request for updating device registration id");
            FirebaseCrash.logcat(Log.INFO, TAG, "Device Registration Id " + updateFcm.getDeviceRegistrationId());
            FirebaseCrash.logcat(Log.INFO, TAG, "User Id " + updateFcm.getUserId());

            UpdateFcmRequest updateFcmRequest = UpdateFcmApi.getClient().create(UpdateFcmRequest.class);
            Call<UpdateFcmResponse> call = updateFcmRequest.getResponse(updateFcm);

            call.enqueue(new Callback<UpdateFcmResponse>() {
                @Override
                public void onResponse(Call<UpdateFcmResponse> call, Response<UpdateFcmResponse> response) {
                    int status = response.code();
                    UpdateFcmResponse updateFcmResponse = response.body();
                    FirebaseCrash.logcat(Log.INFO, TAG, "Response for updating device registration id");
                    //FirebaseCrash.logcat(Log.INFO, TAG, "UpdateFcm error =" + updateFcmResponse.isError());
                    FirebaseCrash.logcat(Log.INFO, TAG, "UpdateFcm message =" + updateFcmResponse.getMessage());

                    if (updateFcmResponse.isError()) {
                        FirebaseCrash.logcat(Log.INFO, TAG, "FCM cannot be updated");
                        FirebaseCrash.report(new Exception("FCM cannot be updated"));
                    } else {
                        FirebaseCrash.logcat(Log.INFO, TAG, "FCM ID updated success-fully on server");
                    }
                }

                @Override
                public void onFailure(Call<UpdateFcmResponse> call, Throwable t) {
                    startActivity(new Intent(Splash.this, Refresh.class));
                }
            });

        }
        //here we are starting the Main Activity from splash activity
        startActivity(intent);
        Splash.this.finish();

        //TODO: sajal Remove this code
        /*if (userId != -1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(SPLASH_TIME_OUT);
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
        }*/
    }

}