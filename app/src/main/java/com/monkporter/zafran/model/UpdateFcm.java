package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vabs on 29/7/16.
 */
public class UpdateFcm {
    @SerializedName("UserID")
    private String userId;
    @SerializedName("DeviceRegesterationID")
    private String deviceRegesterationId;

    public String getDeviceRegesterationId() {
        return deviceRegesterationId;
    }

    public void setDeviceRegesterationId(String deviceRegesterationId) {
        this.deviceRegesterationId = deviceRegesterationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
