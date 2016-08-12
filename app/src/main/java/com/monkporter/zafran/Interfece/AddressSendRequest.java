package com.monkporter.zafran.Interfece;

import com.monkporter.zafran.model.UserDetailResponse;
import com.monkporter.zafran.model.UserLocation;
import com.monkporter.zafran.model.UserLocationResponse;

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
