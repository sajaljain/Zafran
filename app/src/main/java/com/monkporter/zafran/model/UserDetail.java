package com.monkporter.zafran.model;
/*
* {
    "UserID": 49,
    "Cell": "7292061935",
    "EmailID": "sajaljain98@gmail.com",
    "Name": "Sajal",
    "DeviceRegistrationID": "dkWReBo9mo8:APA91bFqUlFF1kkqp6DZfDVJg-D9Na9UQmfojy6Io9N95efOKswSpNd8aziAjbhxqAWtjjSJgGgusatpNJ7b38OfbZ1_FfytbYjOIVLFbxhCvzSb9NkCIkX1XrmCuizBv2KVnr78knjG"
}
*
* */

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav on 7/14/2016.
 */
public class UserDetail {
    @SerializedName("UserID")
    private int userId;
    @SerializedName("DeviceRegistrationID")
    private String deviceRegistrationID;
    @SerializedName("Name")
    private String name;
    @SerializedName("EmailID")
    private String emailId;
    @SerializedName("Cell")
    private String cell;

    public UserDetail() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getDeviceRegistrationID() {
        return deviceRegistrationID;
    }

    public void setDeviceRegistrationID(String deviceRegistrationID) {
        this.deviceRegistrationID = deviceRegistrationID;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "UserDetail{" +
                "userId=" + userId +
                ", deviceRegistrationID='" + deviceRegistrationID + '\'' +
                ", name='" + name + '\'' +
                ", emailId='" + emailId + '\'' +
                ", cell='" + cell + '\'' +
                '}';
    }
}
