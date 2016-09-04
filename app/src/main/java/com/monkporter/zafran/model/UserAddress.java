package com.monkporter.zafran.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Neeraj Rautela on 19-03-2016.
 */
public class UserAddress implements Parcelable{

    private String areaName;
    private int areaId = -1;
    private String cityName;
    private int cityId = -1;
    private int userId;
    private String locationString;

    public String getLocationString() {
        return locationString;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
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

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {

        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(areaName);
        dest.writeInt(areaId);
        dest.writeInt(cityId);
        dest.writeString(cityName);
        dest.writeInt(userId);
        dest.writeString(locationString);
    }

    private void readFromParcel(Parcel in) {
        areaName = in.readString();
        areaId = in.readInt();
        cityId = in.readInt();
        cityName = in.readString();
        userId = in.readInt();
        locationString = in.readString();
    }

    public static final Creator CREATOR = new Creator() {
        public UserAddress createFromParcel(Parcel in)
        {
            return new UserAddress(in);
        }
        public UserAddress[] newArray(int size) {
            return new UserAddress[size];
        }
    };

    public UserAddress(Parcel in) {
        readFromParcel(in);
    }

    public UserAddress(){

    }
}
