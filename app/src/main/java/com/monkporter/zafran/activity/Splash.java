package com.monkporter.zafran.activity;

import android.app.ProgressDialog;
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

import com.monkporter.zafran.Interfece.CheckUserRequest;
import com.monkporter.zafran.Interfece.TempUserRequest;
import com.monkporter.zafran.Interfece.UpdateFcmRequest;
import com.monkporter.zafran.Manifest;
import com.monkporter.zafran.R;
import com.monkporter.zafran.helper.FetchUserEmail;
import com.monkporter.zafran.helper.PrefManager;
import com.monkporter.zafran.model.CheckUser;
import com.monkporter.zafran.model.TemporaryUser;
import com.monkporter.zafran.model.TemporaryUserResponse;
import com.monkporter.zafran.model.UpdateFcm;
import com.monkporter.zafran.model.UpdateFcmResponse;
import com.monkporter.zafran.pushnotification.MyFirebaseInstanceIDService;
import com.monkporter.zafran.rest.CheckUserApiClient;
import com.monkporter.zafran.rest.TempUserApiClient;
import com.monkporter.zafran.rest.UpdateFcmApi;

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
            android.Manifest.permission.RECEIVE_SMS ,android.Manifest.permission.READ_SMS,android.Manifest.permission.GET_ACCOUNTS};

    ProgressDialog progressDialog;

    private final int PERMISSIONS_REQUEST = 111;
    String usrNAme;
    Intent intent;
    String emailID;
    ProgressBar pb;
    PrefManager prefManager;
    String userId;
    private Handler mHandler = new Handler();
    boolean userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //progressDialog = new ProgressDialog(this);
        //progressDialog.setIndeterminate(true);
        //progressDialog.setMessage("Loading...");
        //progressDialog.show();
        if (Build.VERSION.SDK_INT < 23) {
            requestCalls();
        }else{
            permissionCheck();
        }





        //pb = (ProgressBar) findViewById(R.id.progressBar);

    }

    private void requestCalls() {
        intent = new Intent(this, MainActivity.class);
        prefManager = new PrefManager(Splash.this);
        dveiceRegistrationToken = new MyFirebaseInstanceIDService().getRefreshedToken();
        emailID = FetchUserEmail.getEmail(this);
        Toast.makeText(Splash.this, "Email ="+emailID, Toast.LENGTH_SHORT).show();
        Log.d("email","emailId");
        if(emailID == null)
            emailID = "";
        userId = prefManager.getUserId();
        if(userId == null) {
            TemporaryUser temporaryUser = new TemporaryUser();
            temporaryUser.setRegistrationChannelTypeID(0);
            temporaryUser.setCell("");
            temporaryUser.setFirstName("");
            temporaryUser.setLastName("");
            String mytime = java.text.DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());
            String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            usrNAme = "temp_" + mydate + "_" + mytime;
            temporaryUser.setUserName(usrNAme);
            temporaryUser.setSex(1);
            temporaryUser.setSocialMediaUserId("");
            temporaryUser.setEmailId(emailID);
            temporaryUser.setDeviceRegistrationID(dveiceRegistrationToken);

            // Here Sending post request for user
            tempUserRequest = TempUserApiClient.getClient().create(TempUserRequest.class);
            Call<TemporaryUserResponse> call = tempUserRequest.getResponse(temporaryUser);
            call.enqueue(new Callback<TemporaryUserResponse>() {
                @Override
                public void onResponse(Call<TemporaryUserResponse> call, Response<TemporaryUserResponse> response) {
                    int status = response.code();
                    TemporaryUserResponse temporaryUserResponse = response.body();
                    Log.d("Temporary user", "Response =" + status);
                    userId = temporaryUserResponse.getUserId();
                    prefManager.setUserId(userId);
                    startActivity(intent);
                    Splash.this.finish();
                }

                @Override
                public void onFailure(Call<TemporaryUserResponse> call, Throwable t) {
                    Log.d("Temporary user", "onFailure =" + t.getMessage());
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

        if(prefManager.getDeviceRegId() == null) {
            prefManager.setDeviceRegId(dveiceRegistrationToken);
        }
        else if(dveiceRegistrationToken != prefManager.getDeviceRegId()){
            prefManager.setDeviceRegId(dveiceRegistrationToken);
            final UpdateFcm updateFcm = new UpdateFcm();
            updateFcm.setDeviceRegesterationId(dveiceRegistrationToken);
            UpdateFcmRequest updateFcmRequest = UpdateFcmApi.getClient().create(UpdateFcmRequest.class);
            Call<UpdateFcmResponse> call = updateFcmRequest.getResponse(updateFcm);
            Log.d("UpdateFcm Request","Fcm ="+dveiceRegistrationToken);
            call.enqueue(new Callback<UpdateFcmResponse>() {
                @Override
                public void onResponse(Call<UpdateFcmResponse> call, Response<UpdateFcmResponse> response) {
                    int status = response.code();
                    UpdateFcmResponse updateFcmResponse = response.body();
                    Log.d("UpdateFcm Success","Error ="+updateFcmResponse.isError());

                    //  if(progressDialog.isShowing())
                    //    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<UpdateFcmResponse> call, Throwable t) {
                    Log.d("Update Fcm", "onFailure =" + t.getMessage());
                    //    if(progressDialog.isShowing())
                    //      progressDialog.dismiss();
                }
            });

        }
        if(userId != null){
            new Thread(new Runnable() {  @Override
            public void run() {
                try {
                    Thread.sleep(3000);
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
    }
    }

    private void permissionCheck() {
        int location = ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int receiveSms = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS);
        int readSms = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS);
        int fetchEmail = ContextCompat.checkSelfPermission(this,android.Manifest.permission.GET_ACCOUNTS);

        if ( storage != PackageManager.PERMISSION_GRANTED ||
        receiveSms != PackageManager.PERMISSION_GRANTED ||readSms != PackageManager.PERMISSION_GRANTED ||
                fetchEmail!= PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(Splash.this, "Requesting Permission", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, MY_PERMISSIONS,PERMISSIONS_REQUEST);
        }
        else {
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
            Log.i(TAG, "Received response for permissions request.");
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