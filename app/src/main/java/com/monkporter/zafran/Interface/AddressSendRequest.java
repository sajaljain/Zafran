package com.monkporter.zafran.Interface;

import com.monkporter.zafran.model.UserDetailResponse;
import com.monkporter.zafran.model.UserLocation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Vaibhav on 7/6/2016.
 */
public interface AddressSendRequest {
    @POST("zafran/read/availability.php")
    Call<UserDetailResponse> getResponseMessage(@Body UserLocation userLocation);
}
