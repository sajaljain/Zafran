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


    public int getUserId() {

        return Integer.parseInt(userId);
    }

    public String getSmsSend() {
        return smsSend;
    }

    public String getError() {
        return error;
    }




    public String getMessage() {
        return message;
    }



    @Override
    public String toString() {
        return "UserDetailResponse{" +
                "userId='" + userId + '\'' +
                ", smsSend='" + smsSend + '\'' +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                '}';
    }


}
