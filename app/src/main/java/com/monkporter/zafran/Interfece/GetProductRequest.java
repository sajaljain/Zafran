package com.monkporter.zafran.Interfece;

import com.monkporter.zafran.model.GetBanner;
import com.monkporter.zafran.model.GetProducts;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by vabs on 7/8/16.
 */
public interface GetProductRequest {
    @GET("zafran/read/products.php")
    Call<GetProducts> getResponse();
}
