package com.monkporter.zafran.Interface;

import com.monkporter.zafran.model.VerifyOtp;
import com.monkporter.zafran.model.VerifyOtpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Vaibhav on 7/14/2016.
 */
public interface OtpPostRequest {
    @POST("verify_otp.php")
    Call<VerifyOtpResponse> getResponse(@Body VerifyOtp verifyOtp);
}
