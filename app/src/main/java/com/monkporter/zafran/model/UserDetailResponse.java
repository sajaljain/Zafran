package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav on 7/14/2016.
 * {
 * "userId": "49",
 * "error": "false",
 * "message": "Mobile number already existed!",
 * "sms_send": "false"
 * }
 */
public class UserDetailResponse {

    @SerializedName("userId")
    private String userId;

    @SerializedName("sms_send")
    private String smsSend;

    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSmsSend() {
        return smsSend;
    }

    public void setSmsSend(String smsSend) {
        this.smsSend = smsSend;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {

        return Boolean.getBoolean(error);
    }

    public void setMessage(String s) {
        this.message = s;
    }

    public void setError(String error) {
        this.error = error;
    }
}
