package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vabs on 29/7/16.
 */
public class CheckUser {
    @SerializedName("userId")
    private String userId;
    @SerializedName("tempUser")
    private boolean tempUser;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isTempUser() {
        return tempUser;
    }

    public void setTempUser(boolean tempUser) {
        this.tempUser = tempUser;
    }
}
