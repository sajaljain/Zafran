package com.monkporter.zafran.model;

import com.google.android.gms.common.api.BooleanResult;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav on 7/14/2016.
 *
 *
 * {
 "error": "false",
 "message": "User created successfully!",
 "profile": {
 "userId": "49",
 "name": "Sajal",
 "email": "sajaljain98@gmail.com",
 "mobile": "7292061935",
 "fcmId": "eLBULBCJu0Y:APA91bGdpXN-jV455P1fNZ0_fFglxaYsmUYNeoKRY_MfqgpJOYn7UYhOFy_xFTkX1Z55awWrBmQYM5X1P92NOtHEE-3aump6Kij6XX1k-w4le5Fi8VhA6PEeyg3aoGtn2oUkGsvWAA8a",
 "created_at": "2016-08-26 07:11:56"
 }
 }
 */
public class VerifyOtpResponse {
    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;

    @SerializedName("profile")
    private OtpUserObjResponse profile;

    public boolean isError() {
        return Boolean.parseBoolean(error);
    }



    public String getMessage() {
        return message;
    }


    public OtpUserObjResponse getProfile() {
        return profile;
    }

    @Override
    public String toString() {
        return "VerifyOtpResponse{" +
                "error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", profile=" + profile +
                '}';
    }
}
