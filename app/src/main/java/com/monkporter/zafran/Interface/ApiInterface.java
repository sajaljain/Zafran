package com.monkporter.zafran.Interface;

import com.monkporter.zafran.model.Banners;
import com.monkporter.zafran.model.Products;
import com.monkporter.zafran.model.TemporaryUser;
import com.monkporter.zafran.model.TemporaryUserResponse;
import com.monkporter.zafran.model.UpdateFcm;
import com.monkporter.zafran.model.UpdateFcmResponse;

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
}