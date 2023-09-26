package com.springboot.ecommerce.service;

import com.springboot.ecommerce.model.Product;
import com.springboot.ecommerce.pojo.RequestProduct;

import java.util.List;
import java.util.Map;

public interface ProductService {
    public int createProduct(Product product);
    public void removeProductById(int id);
    public Product editProduct(Product product);
    public List<Product> getProductList(int page,int size,String type);
    public Product findProductById(int id);
    public List<Product> getProductListInTheSameSubCategory(int psc_id);
    public List<Product> getBrandNameAndCountOfProductListInTheSameSubcategory(int psc_id);
    public List<Product> getBrandNameAndCountOfProductListInTheSameCategory(int pc_id);
    public List<Product> getProductListInTheSameSubCategory(int psc_id, Integer minCost, Integer maxCost, List<String> brandNameList);
    public List<Product> getProductListInTheSameSubCategory(int psc_id, Integer minCost, Integer maxCost);
    public List<Product> getAllProductsUnderTheSameCategory(int pc_id);
	public List<Product> getAllProductByFilter(RequestProduct req);
	public int getCountProductByFilter(RequestProduct req);
	
	
	int productCount();
	public List<Product> getProductsByUser(int userId);
	public double getTotalValueOfAllProducts(String type);
	public void boostProduct(Product product);
}
