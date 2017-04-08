package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav on 7/14/2016.
 * <p>
 * profile": {
 * "userId": "49",
 * "name": "Sajal",
 * "email": "sajaljain98@gmail.com",
 * "mobile": "7292061935",
 * "fcmId": "eLBULBCJu0Y:APA91bGdpXN-jV455P1fNZ0_fFglxaYsmUYNeoKRY_MfqgpJOYn7UYhOFy_xFTkX1Z55awWrBmQYM5X1P92NOtHEE-3aump6Kij6XX1k-w4le5Fi8VhA6PEeyg3aoGtn2oUkGsvWAA8a",
 * "created_at": "2016-08-26 07:11:56"
 * }
 */
public class OtpUserObjResponse {

    @SerializedName("userId")
    private String userId;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("fcmId")
    private String fcmId;
    @SerializedName("created_at")
    private String created_at;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
