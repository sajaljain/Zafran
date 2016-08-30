package com.monkporter.zafran.Interfece;

import com.monkporter.zafran.model.TemporaryUser;
import com.monkporter.zafran.model.TemporaryUserResponse;
import com.monkporter.zafran.model.UpdateFcm;
import com.monkporter.zafran.model.UpdateFcmResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Vaibhav on 7/21/2016.
 */
public interface TempUserRequest {
    @POST("zafran/create/tempuser.php")
    Call<TemporaryUserResponse> getResponse(@Body TemporaryUser temporaryUser);
}
interface Update {
    @POST("zafran/update/fcmid.php")
    Call<UpdateFcmResponse> getResponse(@Body UpdateFcm updateFcm);
}