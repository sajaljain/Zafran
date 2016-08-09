package com.monkporter.zafran.Interfece;

import com.monkporter.zafran.model.CheckConnection;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by vabs on 9/8/16.
 */
public interface GetConnection {
    @GET("zafran/read/checkconn.php")
    Call<CheckConnection> getResponse();
}
