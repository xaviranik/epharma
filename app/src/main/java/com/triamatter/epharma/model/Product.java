package com.triamatter.epharma.model;

import android.widget.ImageView;

public class Product {
    private String productName;
    private float productPrice;
    //private ImageView productImage;


    public Product(String productName, float productPrice)
    {
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public String getProductName()
    {
        return productName;
    }

    public float getProductPrice()
    {
        return productPrice;
    }
}
