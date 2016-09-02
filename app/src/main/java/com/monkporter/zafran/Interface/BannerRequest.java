package com.monkporter.zafran.Interface;

import com.monkporter.zafran.model.GetBanner;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by vabs on 6/8/16.
 */
public interface BannerRequest {
    @GET("zafran/read/banners.php")
    Call<GetBanner> getResponse();
}
