package com.foodorder.model;

/**
 * Category model class
 */
public class Category {
    private int categoryId;
    private String name;
    
    // Constructors
    public Category() {}
    
    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }
    
    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return String.format("Category{id=%d, name='%s'}", categoryId, name);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return categoryId == category.categoryId;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(categoryId);
    }
}
