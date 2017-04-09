package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

/*
{
  "error": false,
  "message": "address created successfully!",
  "addressId": 1
}
 */

public class AddressResponse {


    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("addressId")
    private int addressId;

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public int getAddressId() {
        return addressId;
    }

    @Override
    public String toString() {
        return "AddressResponse{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", addressId=" + addressId +
                '}';
    }
}
