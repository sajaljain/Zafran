package com.monkporter.zafran.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sajal on 12-May-16.
 */
public class ApiClient  {
    //public static final String BASE_URL =  "http://zafran.curtkart.com/";
    public static final String BASE_URL =  "http://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;
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
