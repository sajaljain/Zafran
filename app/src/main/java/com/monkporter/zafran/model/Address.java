package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Neeraj Rautela on 19-03-2016.
 * <p>
 * <p>
 * {
 * "UserID" : 61,
 * "AreaName":"nehru nagar",
 * "CityName":"ghaziabad",
 * "CompanyName":"Hours",
 * "AddressStreet":"arjun nagar",
 * "Latitude":28.628937,
 * "Longitude":77.371140
 * }
 */
public class Address {

    @SerializedName("UserID")
    private int UserID;

    @SerializedName("AreaName")
    private String AreaName;

    @SerializedName("CityName")
    private String CityName;

    @SerializedName("CompanyName")
    private String CompanyName;

    @SerializedName("AddressStreet")
    private String AddressStreet;

    @SerializedName("Latitude")
    private float Latitude;

    @SerializedName("Longitude")
    private float Longitude;


    public void setUserID(int userID) {
        UserID = userID;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public void setAddressStreet(String addressStreet) {
        AddressStreet = addressStreet;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }

    @Override
    public String toString() {
        return "Address{" +
                "UserID=" + UserID +
                ", AreaName='" + AreaName + '\'' +
                ", CityName='" + CityName + '\'' +
                ", CompanyName='" + CompanyName + '\'' +
                ", AddressStreet='" + AddressStreet + '\'' +
                ", Latitude=" + Latitude +
                ", Longitude=" + Longitude +
                '}';
    }
}
