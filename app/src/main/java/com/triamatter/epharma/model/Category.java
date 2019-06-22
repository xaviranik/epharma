package com.triamatter.epharma.model;

public class Category {
    private String categoryTitle;
    private int categoryIcon;

    public Category(String categoryTitle, int categoryIcon)
    {
        this.categoryTitle = categoryTitle;
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryTitle()
    {
        return categoryTitle;
    }

    public int getCategoryIcon()
    {
        return categoryIcon;
    }
}
