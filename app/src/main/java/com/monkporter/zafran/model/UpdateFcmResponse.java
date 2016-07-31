package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vabs on 29/7/16.
 */
public class UpdateFcmResponse {
    @SerializedName("error")
    private boolean error;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
