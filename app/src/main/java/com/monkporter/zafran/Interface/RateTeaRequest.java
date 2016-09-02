package com.monkporter.zafran.Interface;

import com.monkporter.zafran.model.RateResponse;
import com.monkporter.zafran.model.RateTea;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by vabs on 8/8/16.
 */
public interface RateTeaRequest {
    @POST("")
    Call<RateResponse > getResponse(@Body RateTea rateTea);
}
