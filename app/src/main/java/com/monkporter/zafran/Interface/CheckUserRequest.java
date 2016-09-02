package com.monkporter.zafran.Interface;

import com.monkporter.zafran.model.CheckUser;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by vabs on 29/7/16.
 */
public interface CheckUserRequest {
    @GET("")
    Call<CheckUser> getUserType();
}
