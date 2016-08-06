package com.monkporter.zafran.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vabs on 7/8/16.
 */
public class GetProducts {
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("products")
    private List<Product> products;

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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
