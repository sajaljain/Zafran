package com.monkporter.zafran.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vabs on 9/8/16.
 */
public class CheckConnectionApiClient {
    private static final String BASE_URL = "http://instashout.in/";
    public static Retrofit retrofit = null;
    public static Retrofit getClient(){
       if(retrofit == null){
           retrofit = new Retrofit.Builder()
                   .baseUrl(BASE_URL)
                   .addConverterFactory(GsonConverterFactory.create())
                   .build();
       }
        return retrofit;
    }
}
