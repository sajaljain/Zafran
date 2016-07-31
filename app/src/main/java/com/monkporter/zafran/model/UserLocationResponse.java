package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav on 7/6/2016.
 */
public class UserLocationResponse {

     @SerializedName("CityID")
    private int cityId;
    @SerializedName("AreaID")
    private int areaId;
    @SerializedName("AreaName")
    private String areaName;
    @SerializedName("CityName")
    private String cityName;
    @SerializedName("SearchString")
    private String searchString;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
