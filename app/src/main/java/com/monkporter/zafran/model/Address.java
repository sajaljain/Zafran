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


    private String placeID;
    private int addressId;



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


    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getPlaceID() {
        return placeID;
    }

    public int getUserID() {
        return UserID;
    }

    public String getAreaName() {
        return AreaName;
    }

    public String getCityName() {
        return CityName;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getAddressStreet() {
        return AddressStreet;
    }

    public float getLatitude() {
        return Latitude;
    }

    public float getLongitude() {
        return Longitude;
    }


    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
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
                ", placeID='" + placeID + '\'' +
                ", addressId=" + addressId +
                '}';
    }
}
