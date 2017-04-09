package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav on 7/14/2016.
 * {
 * "UserID" : 47,
 * "Otp" : "6067"
 * }
 */
public class VerifyOtp {

    @SerializedName("UserID")
    private int userId;

    @SerializedName("Otp")
    private String otp;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "VerifyOtp{" +
                "userId=" + userId +
                ", otp='" + otp + '\'' +
                '}';
    }
}
