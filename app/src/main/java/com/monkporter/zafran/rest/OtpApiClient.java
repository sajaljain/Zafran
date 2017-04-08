package com.monkporter.zafran.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vaibhav on 7/14/2016.
 */
public class OtpApiClient {
    public static final String BASE_URL = "http://curtkart.com/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
