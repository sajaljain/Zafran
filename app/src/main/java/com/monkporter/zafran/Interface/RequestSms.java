package com.monkporter.zafran.Interface;

import com.monkporter.zafran.model.UserDetail;
import com.monkporter.zafran.model.UserDetailResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Vaibhav on 7/14/2016.
 */
public interface RequestSms {

    @POST("zafran/create/request_sms.php")
    Call<UserDetailResponse> getResponse(@Body UserDetail userDetail);
}
