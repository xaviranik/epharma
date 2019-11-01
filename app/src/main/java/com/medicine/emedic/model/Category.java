package com.medicine.emedic.model;

public class Category {
    private int categoryId;
    private String categoryName;
    private String url;

    public Category(int categoryId, String categoryName)
    {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Category(int categoryId, String categoryName, String url) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.url = url;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public String getCategoryName()
    {
        return categoryName;
    }
}
