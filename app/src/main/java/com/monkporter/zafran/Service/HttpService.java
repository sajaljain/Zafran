package com.monkporter.zafran.Service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.monkporter.zafran.Interfece.OtpPostRequest;
import com.monkporter.zafran.activity.MainActivity;
import com.monkporter.zafran.helper.PrefManager;
import com.monkporter.zafran.model.OtpUserObjResponse;
import com.monkporter.zafran.model.VerifyOtp;
import com.monkporter.zafran.model.VerifyOtpResponse;

import com.monkporter.zafran.rest.HttpApiClient;
import com.monkporter.zafran.rest.OtpApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vaibhav on 7/14/2016.
 */
public class HttpService extends IntentService{
    private static String TAG = HttpService.class.getSimpleName();
    OtpPostRequest otpPostRequest;
    public HttpService() {
        super(HttpService.class.getSimpleName());
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String otp = intent.getStringExtra("otp");
            verifyOtp(otp);
        }
    }

    private void verifyOtp(final String otp) {
        VerifyOtp vOtp = new VerifyOtp();
        vOtp.setOtp(otp);
        otpPostRequest = HttpApiClient.getClient().create(OtpPostRequest.class);
        Call<VerifyOtpResponse> call = otpPostRequest.getResponse(vOtp);
        call.enqueue(new Callback<VerifyOtpResponse>() {
            @Override
            public void onResponse(Call<VerifyOtpResponse> call, Response<VerifyOtpResponse> response) {
                int statusCode = response.code();
                VerifyOtpResponse responseObj = response.body();
                Log.d("OtpResponse","response status =" + responseObj.getMessage());
                boolean error =  responseObj.isError();
                String message = responseObj.getMessage();
                if(!error) {
                    OtpUserObjResponse profileObj = responseObj.getProfile();
                    String name = profileObj.getName();
                    String email = profileObj.getEmail();
                    String mobile = profileObj.getMobile();

                    PrefManager pref = new PrefManager(getApplicationContext());
                    pref.createLogin(name, email, mobile);

                    Intent intent = new Intent(HttpService.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                Log.d("OtpResponse","onFailure ="+t.getMessage());
            }
        });
    }
}
