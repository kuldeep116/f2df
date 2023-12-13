package com.springboot.ecommerce.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springboot.ecommerce.model.Product;
import com.springboot.ecommerce.model.ProductFeatureValue;
import com.springboot.ecommerce.pojo.RequestProduct;

@Repository
public class ProductDaoImpl implements ProductDao {

	@Autowired
	private SessionFactory sessionFactory;

	private static Logger logger = Logger.getLogger(ProductDaoImpl.class.getName());

	@Override
	public int addProduct(Product product) {
		Session session = sessionFactory.getCurrentSession();
		try {
			if (product.getP_id() != 0) {
				session.update(product);
				for (ProductFeatureValue feature : product.getFeatureDetailsValue()) {
					// feature.setProduct(product);
					session.update(feature);
				}
			} else {
				session.saveOrUpdate(product);
				for (ProductFeatureValue feature : product.getFeatureDetailsValue()) {
					feature.setProduct(product);
					session.saveOrUpdate(feature);
				}
			}
			return product.getP_id();
		} catch (Exception e) {
			logger.debug("Error occured during save product " + e.getMessage());
			return 0;
		}
	}

	@Override
	public void deleteProduct(int id) {
		// String sql = " delete from Product p where p.p_id =" + id;
		Session session = sessionFactory.getCurrentSession();
		session.createQuery(
				" update com.springboot.ecommerce.model.Product p set p.productDesc =:status where p.p_id=:p_id")
				.setParameter("status", "DELETED").setParameter("p_id", id).executeUpdate();
	}

	@Override
	public int updateProduct(Product product) {
		Session session = sessionFactory.getCurrentSession();
		if (product.getImg1() != null && !product.getImg1().isEmpty()) {
			session.createQuery(" update com.springboot.ecommerce.model.Product p set p.img1 =:img1 where p.p_id=:p_id")
					.setParameter("img1", product.getImg1()).setParameter("p_id", product.getP_id()).executeUpdate();
			return product.getP_id();
		} else {
			session.createQuery(
					" update com.springboot.ecommerce.model.Product p set p.productDesc =:action, p.updateDate =:date where p.p_id=:p_id")
					.setParameter("action", product.getProductDesc()).setParameter("date", new Date())
					.setParameter("p_id", product.getP_id()).executeUpdate();
			return product.getP_id();
		}

	}

	@Override
	public Product findProductById(int id) {
		Session session = sessionFactory.getCurrentSession();
		Product p = new Product();
		try {
			p = (Product) session.createQuery("from com.springboot.ecommerce.model.Product p where p.p_id =:id")
					.setParameter("id", id).getSingleResult();
		} catch (NoResultException nre) {
			logger.debug("Error occrured in findProductById " + nre.getMessage());
			return p;// Code for handling NoResultException
		} catch (NonUniqueResultException nure) {
			logger.debug("Error occrured in findProductById " + nure.getMessage());
			return p;// Code for handling NonUniqueResultException
		}
		return p;
	}

	public ProductFeatureValue getProductFeatureOrganic(int productId) {
		Session session = sessionFactory.getCurrentSession();
		ProductFeatureValue productValue = new ProductFeatureValue();
		try {
			productValue = (ProductFeatureValue) session.createQuery(
					"from com.springboot.ecommerce.model.ProductFeatureValue where productId :productId and productFeatureKey :organic ")
					.setParameter("productId", productId).setParameter("organic", "ORGANIC").getSingleResult();
		} catch (NoResultException nre) {
			logger.debug("Error occrured in getProductFeatureOrganic " + nre.getMessage());
			return productValue;// Code for handling NoResultException
		} catch (NonUniqueResultException nure) {
			logger.debug("Error occrured in getProductFeatureOrganic " + nure.getMessage());
			return productValue;// Code for handling NonUniqueResultException
		}
		return productValue;
	}

	public List<ProductFeatureValue> getProductFeature(int productId) {
		Session session = sessionFactory.getCurrentSession();
		try {
			List<ProductFeatureValue> productValue = session
					.createQuery("from com.springboot.ecommerce.model.ProductFeatureValue where productId :productId")
					.setParameter("productId", productId).getResultList();
			return productValue;
		} catch (Exception e) {
			logger.debug("Error occrured in getProductFeature " + e.getMessage());
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Product> getProducts(int page, int size, String type) {
		Session session = sessionFactory.getCurrentSession();
		try {
			if (type.contains("recommded")) {
				return session.createQuery(
						"from com.springboot.ecommerce.model.Product p where p.recommended = 1 and p.productDesc='Active' ORDER BY p.p_id desc ")
						.setFirstResult(page).setMaxResults(size).list();
			} else if (type.contains("bestSeller")) {
				return session.createQuery(
						"from com.springboot.ecommerce.model.Product p where p.bestSeller = 1 and p.productDesc='Active' ORDER BY p.p_id desc ")
						.setFirstResult(page).setMaxResults(size).list();
			}
			return session.createQuery("from com.springboot.ecommerce.model.Product p ORDER BY p.p_id desc ")
					.setFirstResult(page).setMaxResults(size).list();
		} catch (Exception e) {
			logger.debug("Error occrured in getProductsInTheSameSubCategory " + e.getMessage());
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Product> getProductsForSiteMap() {
		Session session = sessionFactory.getCurrentSession();
		try {
			return session.createQuery("from com.springboot.ecommerce.model.Product p ORDER BY p.p_id desc ").list();
		} catch (Exception e) {
			logger.debug("Error occrured in getProductsInTheSameSubCategory " + e.getMessage());
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public int productCount() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.Product p ").list().size();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Product> getBrandNamesAndCountOfProductsInTheSameSubCategory(int psc_id) {
		Session session = sessionFactory.getCurrentSession();
		try {
			return session.createQuery(
					"select new com.springboot.ecommerce.pojo.BrandAndCountOfProduct(p.brandOfProduct, count(p.p_id)) "
							+ "from com.springboot.ecommerce.model.Product p  "
							+ "where p.subCategory.psc_id in :psc_id_temp and p.productDesc ='Active'"
							+ "group by p.brandOfProduct  ORDER BY p.p_id desc ")
					.setParameter("psc_id_temp", psc_id).getResultList();
		} catch (Exception e) {
			logger.debug("Error occrured in getProductsInTheSameSubCategory " + e.getMessage());
			return null;
		}

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Product> getBrandNamesAndCountOfProductsInTheSameCategory(int pc_id) {
		Session session = sessionFactory.getCurrentSession();
		try {
			return session.createQuery(
					"select new com.springboot.ecommerce.pojo.BrandAndCountOfProduct(p.brandOfProduct, count(p.p_id)) "
							+ "from com.springboot.ecommerce.model.Product p  "
							+ "where p.productCategory.pc_id in :pc_id_temp and p.productDesc ='Active' "
							+ "group by p.brandOfProduct ORDER BY p.p_id desc")
					.setParameter("pc_id_temp", pc_id).getResultList();
		} catch (Exception e) {
			logger.debug("Error occrured in getProductsInTheSameSubCategory " + e.getMessage());
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Product> getProductsInTheSameSubCategory(int psc_id) {
		Session session = sessionFactory.getCurrentSession();
		try {
			return session
					.createQuery("select p from com.springboot.ecommerce.model.Product p  "
							+ "inner join com.springboot.ecommerce.model.ProductSubCategory s "
							+ "on p.subCategory.psc_id=s.psc_id "
							+ "where s.psc_id in :psc_id_temp and p.productDesc ='Active' ORDER BY p.p_id desc")
					.setParameter("psc_id_temp", psc_id).list();
		} catch (Exception e) {
			logger.debug("Error occrured in getProductsInTheSameSubCategory " + e.getMessage());
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Product> getProductsInTheSameSubCategory(int psc_id, Integer minCost, Integer maxCost,
			List<String> brandNameList) {
		Session session = sessionFactory.getCurrentSession();
		try {
			return session.createQuery("select p from com.springboot.ecommerce.model.Product p  "
					+ "inner join com.springboot.ecommerce.model.ProductSubCategory s "
					+ "on p.subCategory.psc_id=s.psc_id " + "where s.psc_id in :psc_id_temp "
					+ "and p.productFee > :minCost_temp " + "and p.productFee < :maxCost_temp "
					+ "and p.brandOfProduct in :brandNameList_temp and p.productDesc ='Active' ORDER BY p.p_id desc")
					.setParameter("psc_id_temp", psc_id).setParameter("minCost_temp", minCost)
					.setParameter("maxCost_temp", maxCost).setParameterList("brandNameList_temp", brandNameList)
					.getResultList();
		} catch (Exception e) {
			logger.debug("Error occrured in getProductsInTheSameSubCategory " + e.getMessage());
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Product> getProductsInTheSameSubCategory(int psc_id, Integer minCost, Integer maxCost) {
		Session session = sessionFactory.getCurrentSession();
		try {
			return session
					.createQuery("select p from com.springboot.ecommerce.model.Product p  "
							+ "inner join com.springboot.ecommerce.model.ProductSubCategory s "
							+ "on p.subCategory.psc_id=s.psc_id " + "where s.psc_id in :psc_id_temp "
							+ "and p.productFee > :minCost_temp "
							+ "and p.productFee < :maxCost_temp  and p.productDesc ='Active' ORDER BY p.p_id desc")
					.setParameter("psc_id_temp", psc_id).setParameter("minCost_temp", minCost)
					.setParameter("maxCost_temp", maxCost).list();
		} catch (Exception e) {
			logger.debug("Error occrured in getProductsInTheSameSubCategory " + e.getMessage());
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Product> getProductsInTheSameCategory(int pc_id) {
		Session session = sessionFactory.getCurrentSession();
		try {
			List<Product> productList = session
					.createQuery("select p from com.springboot.ecommerce.model.Product p  "
							+ "inner join com.springboot.ecommerce.model.ProductCategory c "
							+ "on p.productCategory.pc_id=c.pc_id "
							+ "where c.pc_id in :pc_id_temp and p.productDesc ='Active' ORDER BY p.p_id desc ")
					.setParameter("pc_id_temp", pc_id).list();

			return productList;
		} catch (Exception e) {
			logger.debug("Error occrured in getProductsInTheSameCategory " + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Product> getAllProductByFilter(RequestProduct req) {
		Session session = sessionFactory.getCurrentSession();
		List<Product> productList = new ArrayList<>();
		try {
			if (req.getPc_id() > 0 && req.getPsc_id() == 0) {
				productList = (List<Product>) session
						.createQuery(" select p from com.springboot.ecommerce.model.Product p  "
								+ " inner join com.springboot.ecommerce.model.ProductCategory c "
								+ " on p.productCategory.pc_id=c.pc_id " + " where c.pc_id in :pc_id_temp "
								+ " and p.productFee > :minCost_temp "
								+ " and p.productFee < :maxCost_temp and p.productDesc ='Active' "
								+ "ORDER BY p.p_id desc ")
						.setParameter("pc_id_temp", req.getPc_id()).setParameter("minCost_temp", req.getMinCost())
						.setParameter("maxCost_temp", req.getMaxCost()).setFirstResult(req.getPageNo())
						.setMaxResults(req.getSize()).list();
			} else if (req.getPsc_id() > 0) {
				productList = (List<Product>) session
						.createQuery(" select p from com.springboot.ecommerce.model.Product p  "
								+ " inner join com.springboot.ecommerce.model.ProductSubCategory s "
								+ " on p.subCategory.psc_id=s.psc_id " + " where s.psc_id in :psc_id_temp "
								+ " and p.productFee > :minCost_temp "
								+ "and p.productFee < :maxCost_temp  and p.productDesc ='Active'"
								+ " ORDER BY p.p_id desc ")
						.setParameter("psc_id_temp", req.getPsc_id()).setParameter("minCost_temp", req.getMinCost())
						.setParameter("maxCost_temp", req.getMaxCost()).setFirstResult(req.getPageNo())
						.setMaxResults(req.getSize()).list();
			} else {
				productList = (List<Product>) session.createQuery(
						" select p from com.springboot.ecommerce.model.Product p where p.productDesc ='Active' ORDER BY p.p_id desc ")
						.setFirstResult(req.getPageNo()).setMaxResults(req.getSize()).list();
			}
			if (req.getSearchKeyword() != null && req.getSearchKeyword() != "") {
				productList = (List<Product>) session.createQuery(
						" select p  from com.springboot.ecommerce.model.Product p where p.productName like :productName"
								+ " and p.productFee > :minCost_temp "
								+ "and p.productFee < :maxCost_temp and p.productDesc ='Active' "
								+ " ORDER BY p.p_id desc ")
						.setParameter("productName", "%" + req.getSearchKeyword() + "%")
						.setParameter("minCost_temp", req.getMinCost()).setParameter("maxCost_temp", req.getMaxCost())
						.setFirstResult(req.getPageNo()).setMaxResults(req.getSize()).list();

			}
		} catch (Exception e) {
			logger.debug("Error occrured in getAllProductByFilter " + e.getMessage());
			return null;
		}
		return productList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getCountProductByFilter(RequestProduct req) {
		Session session = sessionFactory.getCurrentSession();
		List<Product> productList = new ArrayList<>();
		try {
			if (req.getPc_id() > 0 && req.getPsc_id() == 0) {
				productList = (List<Product>) session
						.createQuery(" select p from com.springboot.ecommerce.model.Product p  "
								+ " inner join com.springboot.ecommerce.model.ProductCategory c "
								+ " on p.productCategory.pc_id=c.pc_id " + " where c.pc_id in :pc_id_temp "
								+ " and p.productFee > :minCost_temp " + "and p.productFee < :maxCost_temp "
								+ "ORDER BY p.p_id desc ")
						.setParameter("pc_id_temp", req.getPc_id()).setParameter("minCost_temp", req.getMinCost())
						.setParameter("maxCost_temp", req.getMaxCost()).list();
			} else if (req.getPsc_id() > 0) {
				productList = (List<Product>) session
						.createQuery(" select p from com.springboot.ecommerce.model.Product p  "
								+ " inner join com.springboot.ecommerce.model.ProductSubCategory s "
								+ " on p.subCategory.psc_id=s.psc_id " + " where s.psc_id in :psc_id_temp "
								+ " and p.productFee > :minCost_temp " + "and p.productFee < :maxCost_temp "
								+ "ORDER BY p.p_id desc ")
						.setParameter("psc_id_temp", req.getPsc_id()).setParameter("minCost_temp", req.getMinCost())
						.setParameter("maxCost_temp", req.getMaxCost()).list();
			} else {
				productList = (List<Product>) session
						.createQuery(" select p from com.springboot.ecommerce.model.Product p ORDER BY p.p_id desc ")
						.list();
			}
			if (req.getSearchKeyword() != null && req.getSearchKeyword() != "") {
				productList = (List<Product>) session.createQuery(
						" select p  from com.springboot.ecommerce.model.Product p where p.productName like :productName"
								+ " and p.productFee > :minCost_temp " + "and p.productFee < :maxCost_temp "
								+ " ORDER BY p.p_id desc ")
						.setParameter("productName", "%" + req.getSearchKeyword() + "%")
						.setParameter("minCost_temp", req.getMinCost()).setParameter("maxCost_temp", req.getMaxCost())
						.list();

			}
		} catch (Exception e) {
			logger.debug("Error occrured in getcountProductByFilter " + e.getMessage());
			return 0;
		}
		return productList.size();
	}
	
	@Override
	public List<Product> getProductByName(String name)
	{
		Session session = sessionFactory.getCurrentSession();
		try {
			@SuppressWarnings("unchecked")
			List<Product> productList = (List<Product>) session.createQuery(
					" select p  from com.springboot.ecommerce.model.Product p where p.productName like :productName "
							+ " ORDER BY p.p_id desc ")
					.setParameter("productName", "%" + name + "%")
					.list();
			return productList;
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return new ArrayList<Product>();
	}

	@Override
	public List<Product> getProductsByUser(int userId) {
		Session session = sessionFactory.getCurrentSession();
		try {
			@SuppressWarnings("unchecked")
			List<Product> productList = session.createQuery(
					"select p from com.springboot.ecommerce.model.Product p where p.userId =:userId  ORDER BY p.p_id desc ")
					.setParameter("userId", userId).list();
			return productList;
		} catch (Exception e) {
			logger.debug("Error occrured in getProductsByUser " + e.getMessage());
			return null;
		}
	}

	@Override
	public double getTotalValueOfAllProducts(String typeOfAction) {
		double totalPrice = 0;
		String sql = "";
		if (typeOfAction.contains("BUSSDONE")) {
			sql = "select pf.productFeatureKey,IF(pf.productFeatureValue is null, '0- ',pf.productFeatureValue),p.p_fee from product p "
					+ " inner join ProductFeatureValue pf on pf.p_id = p.p_id and pf.productFeatureKey like '%QUANTITY%' where p.productDesc='BUSS DONE'";
		} else if (typeOfAction.contains("ACTIVE")) {
			sql = "select pf.productFeatureKey,IF(pf.productFeatureValue is null, '0- ',pf.productFeatureValue),p.p_fee from product p "
					+ " inner join ProductFeatureValue pf on pf.p_id = p.p_id and pf.productFeatureKey like '%QUANTITY%' where p.productDesc='Active'";
		} else {
			sql = "select pf.productFeatureKey,IF(pf.productFeatureValue is null, '0- ',pf.productFeatureValue),p.p_fee from product p "
					+ " inner join ProductFeatureValue pf on pf.p_id = p.p_id and pf.productFeatureKey like '%QUANTITY%' ";
		}

		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		List<Object[]> rows = query.list();
		for (Object[] row : rows) {
			String str = row[1].toString();
			// str = str.replaceAll(".", "");
			int qyt = 1;
			int price = 0;
			if (str != null && str != "" && str.contains("-")) {
				try {
					qyt = Integer.valueOf(str.split("-")[0].trim());
					price = Integer.valueOf(row[2].toString().trim());
				} catch (Exception e) {
					logger.debug("Error occured during price count" + e.getMessage());
				}
				totalPrice = totalPrice + (qyt * price);
			}

		}
		return totalPrice;
	}

	@Override
	public void boostProduct(Product product) {
		Session session = sessionFactory.getCurrentSession();
		session.createQuery(
				" update com.springboot.ecommerce.model.Product p set p.recommended =:recommended,p.assured =:assured where p.p_id=:p_id")
				.setParameter("recommended", product.isRecommended()).setParameter("assured", product.isAssured())
				.setParameter("p_id", product.getP_id()).executeUpdate();
	}

}
