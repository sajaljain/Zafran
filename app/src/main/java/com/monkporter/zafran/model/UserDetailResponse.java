package com.monkporter.zafran.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vaibhav on 7/14/2016.
 */
public class UserDetailResponse {
    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }
    public boolean isError() {
        return error;
    }
    public void setMessage(String s){
        this.message = s;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
