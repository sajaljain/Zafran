package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vabs on 29/7/16.
 */
public class UpdateFcm {
    @SerializedName("UserID")
    private int userId;
    @SerializedName("DeviceRegistrationID")
    private String deviceRegistrationId;

    public String getDeviceRegistrationId() {
        return deviceRegistrationId;
    }

    public void setDeviceRegistrationId(String deviceRegistrationId) {
        this.deviceRegistrationId = deviceRegistrationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
