package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav on 7/6/2016.
 */
public class UserLocation {
    @SerializedName("SearchString")
    private String searchString;
    @SerializedName("Latitude")
    private String latitude;
    @SerializedName("Longitude")
    private String longitude;
    @SerializedName("PlaceID")
    private String placeId;

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
