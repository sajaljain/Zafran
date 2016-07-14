package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav on 7/14/2016.
 */
public class VerifyOtpResponse {
    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("profile")
    private OtpUserObjResponse profile;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OtpUserObjResponse getProfile() {
        return profile;
    }

    public void setProfile(OtpUserObjResponse profile) {
        this.profile = profile;
    }
}
