package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.model.ProductCategory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductCategoryDaoImpl implements ProductCategoryDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public int addCategory(ProductCategory productCategory) {
		Session session = sessionFactory.getCurrentSession();
		session.save(productCategory);
		return productCategory.getPc_id();
	}

	@Override
	public void deleteCategory(int id) {
		Session session = sessionFactory.getCurrentSession();
		ProductCategory productCategory = session.get(ProductCategory.class, id);
		session.delete(productCategory);
	}

	@Override
	public int updateCategory(ProductCategory productCategory) {
		Session session = sessionFactory.getCurrentSession();
		session.createQuery(
				" Update com.springboot.ecommerce.model.ProductCategory p set p.categoryImg =:categoryImg,p.categoryName=:categoryName,p.productType=:productType where p.pc_id =:pcId")
				.setParameter("pcId", productCategory.getPc_id())
				.setParameter("categoryImg", productCategory.getCategoryImg())
				.setParameter("categoryName", productCategory.getCategoryName())
				.setParameter("productType", productCategory.getProductType()).executeUpdate();
		return productCategory.getPc_id();
	}

	@Override
	public ProductCategory findCategoryById(int id) {
		Session session = sessionFactory.getCurrentSession();
		ProductCategory productCategory = (ProductCategory) session.get(ProductCategory.class, id);
		return productCategory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProductCategory> getCategories(int page, int size, String productType) {
		Session session = sessionFactory.getCurrentSession();
		if (productType.contains("All")) {
			return session
					.createQuery(
							"from com.springboot.ecommerce.model.ProductCategory p where p.productType !=:productType ORDER BY p.pc_id")
					.setParameter("productType", "News").getResultList();
		}  else if (productType.contains("Sell") || productType.contains("Rent")) {
			if(size > 30){
				return session
						.createQuery(
								"from com.springboot.ecommerce.model.ProductCategory p where p.productType =:productType ORDER BY p.pc_id")
						.setParameter("productType", productType).getResultList();
			}else{
				return session
						.createQuery(
								"from com.springboot.ecommerce.model.ProductCategory p where p.productType =:productType ORDER BY p.pc_id")
						.setParameter("productType", productType).setFirstResult(page).setMaxResults(size).getResultList();
			}
			
		}else {
			return session.createQuery("from com.springboot.ecommerce.model.ProductCategory p ORDER BY p.pc_id")
					.getResultList();
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ProductCategory> getCategoriesForSiteMap() {
		Session session = sessionFactory.getCurrentSession();
		
			return session
					.createQuery(
							"from com.springboot.ecommerce.model.ProductCategory p  ORDER BY p.pc_id")
					.getResultList();
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProductCategory> getAllCategories() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.ProductCategory").getResultList();
	}
}
