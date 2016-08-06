package com.monkporter.zafran.Interfece;

import com.monkporter.zafran.model.GetBanner;
import com.monkporter.zafran.model.GetProducts;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by vabs on 6/8/16.
 */
public interface BannerRequest {
    @GET("zafran/read/banners.php")
    Call<GetBanner> getResponse();
}
