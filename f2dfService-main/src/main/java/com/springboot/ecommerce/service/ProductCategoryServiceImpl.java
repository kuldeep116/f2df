package com.springboot.ecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.ecommerce.model.ProductCategory;
import com.springboot.ecommerce.repository.ProductCategoryDao;

@Service
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    ProductCategoryDao productCategoryDao;

    @Override
    public int createProductCategory(ProductCategory productCategory) {
        return productCategoryDao.addCategory(productCategory);
    }

    @Override
    public ProductCategory editProductCategory(ProductCategory productCategory) {
        productCategoryDao.updateCategory(productCategory);
        return productCategory;
    }

    @Override
    public void removeCategoryById(int id) {
        productCategoryDao.deleteCategory(id);
    }

    @Override
    public List<ProductCategory> getCategoryList(int page, int size,String productType) {
        return productCategoryDao.getCategories(page,size,productType).stream().distinct().collect(Collectors.toList());
    }

    @Override
    public ProductCategory findCategoryById(int id) {
        return productCategoryDao.findCategoryById(id);
    }

	@Override
	public List<ProductCategory> getAllCategories() {
		return productCategoryDao.getAllCategories();
	}

}
