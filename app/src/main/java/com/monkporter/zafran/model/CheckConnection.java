package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vabs on 9/8/16.
 */
public class CheckConnection {
   @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
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
}
