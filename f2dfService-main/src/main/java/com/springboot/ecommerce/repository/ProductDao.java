package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.model.Product;
import com.springboot.ecommerce.pojo.RequestProduct;

import java.util.List;

public interface ProductDao {
    public int addProduct(Product product);
    public void deleteProduct(int id);
    public int updateProduct(Product product);
    public Product findProductById(int id);
    public List<Product> getProducts(int page,int size,String type);
    public List<Product> getBrandNamesAndCountOfProductsInTheSameSubCategory(int psc_id);
    public List<Product> getBrandNamesAndCountOfProductsInTheSameCategory(int pc_id);
    public List<Product> getProductsInTheSameSubCategory(int psc_id);
    public List<Product> getProductsInTheSameSubCategory(int psc_id, Integer minCost, Integer maxCost);
    public List<Product> getProductsInTheSameSubCategory(int psc_id, Integer minCost, Integer maxCost, List<String> brandNameList);
    public List<Product> getProductsInTheSameCategory(int pc_id);
	public List<Product> getAllProductByFilter(RequestProduct req);
	int productCount();
	public List<Product> getProductsByUser(int userId);
	public int getCountProductByFilter(RequestProduct req);
	public void boostProduct(Product product);
	double getTotalValueOfAllProducts(String type);
}
