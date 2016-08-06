package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vabs on 6/8/16.
 */
public class Banner {
    @SerializedName("bannerURL")
    private String bannerUrl;
    @SerializedName("bannerHead")
    private String bannerHead;
    @SerializedName("bannerSubHead")
    private String bannerSubHead;

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getBannerHead() {
        return bannerHead;
    }

    public void setBannerHead(String bannerHead) {
        this.bannerHead = bannerHead;
    }

    public String getBannerSubHead() {
        return bannerSubHead;
    }

    public void setBannerSubHead(String bannerSubHead) {
        this.bannerSubHead = bannerSubHead;
    }
}
