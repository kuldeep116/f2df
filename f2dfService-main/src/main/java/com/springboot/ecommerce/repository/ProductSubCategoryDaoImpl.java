package com.springboot.ecommerce.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springboot.ecommerce.model.ProductSubCategory;

@Repository
public class ProductSubCategoryDaoImpl implements ProductSubCategoryDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public int addSubCategory(ProductSubCategory subCategory) {
		Session session = sessionFactory.getCurrentSession();
		session.save(subCategory);
		return subCategory.getPsc_id();
	}

	@Override
	public void deleteSubCategory(int id) {
		Session session = sessionFactory.getCurrentSession();
		ProductSubCategory subCategory = findSubCategoryById(id);
		session.delete(subCategory);
	}

	@Override
	public int updateSubCategory(ProductSubCategory subCategory) {
		Session session = sessionFactory.getCurrentSession();
		//session.update(subCategory);
	   // return subCategory.getPsc_id();
		return session
				.createQuery(
						" Update com.springboot.ecommerce.model.ProductSubCategory p set p.subCategoryImg =:subCategoryImg where p.psc_id =:pscId")
				.setParameter("pscId", subCategory.getPsc_id())
				.setParameter("subCategoryImg", subCategory.getSubCategoryImg()).executeUpdate();
	}

	@Override
	public ProductSubCategory findSubCategoryById(int id) {
		Session session = sessionFactory.getCurrentSession();
		ProductSubCategory subCategory = (ProductSubCategory) session.get(ProductSubCategory.class, id);
		return subCategory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProductSubCategory> getSubCategories(int page, int size) {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.ProductSubCategory p ORDER BY p.psc_id")
				.setFirstResult(page).setMaxResults(size).getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProductSubCategory> getSubCategories() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.ProductSubCategory p ORDER BY p.psc_id")
				.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProductSubCategory> findSubCategoryByCatId(int pc_id) {
		Session session = sessionFactory.getCurrentSession();
		return session
				.createQuery(
						"from com.springboot.ecommerce.model.ProductSubCategory psc where psc.productCategory.id =:pc_id")
				.setParameter("pc_id", pc_id).list();
	}
}
