package com.triamatter.epharma.model;

public class Product {
    private int productID;
    private String productName;
    private float productPrice;

    public Product(int productID, String productName, float productPrice)
    {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
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
