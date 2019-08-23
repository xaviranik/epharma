package com.medicine.emedic.model;

import java.io.Serializable;

public class Product implements Serializable {
    private int productID;
    private String productName;
    private float productPrice;
    private int productQuantity;
    private String productImage;
    private  String description;

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    private  String short_description;

    public Product(int productID, String productName, float productPrice, int productQuantity)
    {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;

    }

    public Product(int productID, String productName, float productPrice, String productImage, String description,String short_description) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.description = description;
        this.short_description=short_description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product(int productID, String productName, float productPrice, String productImage)
    {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getProductQuantity()
    {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity)
    {
        this.productQuantity = productQuantity;
    }

    public int getProductID()
    {
        return productID;
    }

    public void setProductID(int productID)
    {
        this.productID = productID;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public float getProductPrice()
    {
        return productPrice;
    }

    public void setProductPrice(float productPrice)
    {
        this.productPrice = productPrice;
    }
}
