package com.springbootcartapp.Dto;

import java.util.List;

import com.springbootcartapp.entities.Category;
import com.springbootcartapp.entities.SubCategory;

public class CategoryWithSubCategories {

    private Integer categoryId;
    private String categoryName;
    private List<SubCategory> subCategories;

    public CategoryWithSubCategories(Category category, List<SubCategory> subCategories) {
        this.categoryId = category.getId();
        this.categoryName = category.getCategoryName();
        this.subCategories = subCategories;
    }

    // Getters and setters
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}

