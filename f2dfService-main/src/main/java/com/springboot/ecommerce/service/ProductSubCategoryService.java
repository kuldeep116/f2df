package com.springboot.ecommerce.service;

import java.util.List;

import com.springboot.ecommerce.model.ProductSubCategory;

public interface ProductSubCategoryService {
    public int createSubCategory(ProductSubCategory subCategory);
    public ProductSubCategory editSubCategory(ProductSubCategory subCategory);
    public void removeSubCategoryById(int id);
    public List<ProductSubCategory> getSubCategoryList(int page, int size);
    public ProductSubCategory findSubCategoryById(int id);
	List<ProductSubCategory> getSubCategoryList();
	public List<ProductSubCategory> getSubCategoryList(int pc_id);
}
