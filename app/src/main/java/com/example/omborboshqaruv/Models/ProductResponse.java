package com.example.omborboshqaruv.Models;



import com.google.gson.annotations.SerializedName;

public class ProductResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("product_id")
    private int productId;

    public String getMessage() {
        return message;
    }

    public int getProductId() {
        return productId;
    }
}

