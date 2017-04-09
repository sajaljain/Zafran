package com.monkporter.zafran.Interface;

import com.monkporter.zafran.model.Address;
import com.monkporter.zafran.model.AddressResponse;
import com.monkporter.zafran.model.Banners;
import com.monkporter.zafran.model.Products;
import com.monkporter.zafran.model.TemporaryUser;
import com.monkporter.zafran.model.TemporaryUserResponse;
import com.monkporter.zafran.model.UpdateFcm;
import com.monkporter.zafran.model.UpdateFcmResponse;
import com.monkporter.zafran.model.UserDetail;
import com.monkporter.zafran.model.UserDetailResponse;
import com.monkporter.zafran.model.VerifyOtp;
import com.monkporter.zafran.model.VerifyOtpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Sajal on 30-Aug-16.
 */
public interface ApiInterface {
    @POST("zafran/create/tempuser.php")
    Call<TemporaryUserResponse> createTemporaryUser(@Body TemporaryUser temporaryUser);

    @POST("zafran/update/fcmid.php")
    Call<UpdateFcmResponse> updateFCM(@Body UpdateFcm updateFcm);

    @GET("zafran/read/banners.php")
    Call<Banners> getBanners();

    @GET("zafran/read/products.php")
    Call<Products> getProducts();


    @POST("zafran/update/verify_otp.php")
    Call<VerifyOtpResponse> getResponse_OTP(@Body VerifyOtp verifyOtp);



    @POST("zafran/create/request_sms.php")
    Call<UserDetailResponse> getResponse_RequestSms(@Body UserDetail userDetail);


    @POST("zafran/create/address.php")
    Call<AddressResponse> getResponse_CreateAddress(@Body Address userDetail);

}