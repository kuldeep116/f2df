package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.model.ProductSubCategory;

import java.util.Collection;
import java.util.List;

public interface ProductSubCategoryDao {
    public int addSubCategory(ProductSubCategory subCategory);
    public void deleteSubCategory(int id);
    public int updateSubCategory(ProductSubCategory subCategory);
    public ProductSubCategory findSubCategoryById(int id);
    public List<ProductSubCategory> getSubCategories(int page,int size);
	public Collection<ProductSubCategory> getSubCategories();
	public List<ProductSubCategory> findSubCategoryByCatId(int pc_id);
}
