package com.monkporter.zafran.Interfece;

import com.monkporter.zafran.model.UpdateFcm;
import com.monkporter.zafran.model.UpdateFcmResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by vabs on 29/7/16.
 */
public interface UpdateFcmRequest {
    @POST("zafran/update/device_registration.php?id=%22223420%22&deviceRegId=%22dqweqwewqe3221313")
    Call<UpdateFcmResponse> getResponse(@Body UpdateFcm updateFcm);
}
