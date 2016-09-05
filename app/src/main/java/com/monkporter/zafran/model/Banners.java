package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vabs on 6/8/16.
 */
public class Banners {
    @SerializedName("error")
   private boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("banners")
    private List<Banner> banners;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }
}
