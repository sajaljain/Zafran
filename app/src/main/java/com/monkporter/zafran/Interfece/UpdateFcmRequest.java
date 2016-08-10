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
    @POST("zafran/update/fcmid.php")
    Call<UpdateFcmResponse> getResponse(@Body UpdateFcm updateFcm);
}
