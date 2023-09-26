package com.springboot.ecommerce.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springboot.ecommerce.model.AddSpace;
import com.springboot.ecommerce.model.Banner;
import com.springboot.ecommerce.model.ContactUs;
import com.springboot.ecommerce.model.Insurance;
import com.springboot.ecommerce.model.Loan;
import com.springboot.ecommerce.model.Training;
import com.springboot.ecommerce.pojo.CatExportQunatity;
import com.springboot.ecommerce.pojo.SubCatQunatity;

@Repository
public class HomeDaoImpl implements HomeDao {

	@Autowired
	private SessionFactory sessionFactory;
	private static Logger logger = Logger.getLogger(HomeDaoImpl.class.getName());

	@Override
	public int addTraining(Training training) {
		Session session = sessionFactory.getCurrentSession();
		session.save(training);
		return training.getId();
	}

	@Override
	public int addInsuranceLead(Insurance insurance) {
		Session session = sessionFactory.getCurrentSession();
		session.save(insurance);
		return insurance.getId();
	}

	@Override
	public int createLoan(Loan loan) {
		Session session = sessionFactory.getCurrentSession();
		session.save(loan);
		return loan.getId();
	}

	@Override
	public Object getNavigation() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.Navigation").list();
	}

	@Override
	public List<Banner> getBanner(String type) {
		Session session = sessionFactory.getCurrentSession();
		if (type.equals("All")) {
			return session.createQuery("from com.springboot.ecommerce.model.Banner b order by b.type asc ").list();
		} else {
			return session.createQuery("from com.springboot.ecommerce.model.Banner b where b.type =:home")
					.setParameter("home", type).list();
		}

	}

	@Override
	public void deleteBanner(int id) {
		String sql = " delete from Banner p where p.id =" + id;
		Session session = sessionFactory.getCurrentSession();
		session.createQuery(sql).executeUpdate();
	}

	@Override
	public int updateBanner(Banner req) {
		Session session = sessionFactory.getCurrentSession();
		session.createQuery(
				" update com.springboot.ecommerce.model.Banner b  set b.img =:img,b.link =:link where b.id =:id")
				.setParameter("img", req.getImg()).setParameter("link", req.getUrlLink()).setParameter("id", req.getId())
				.executeUpdate();
		return req.getId();
	}

	@Override
	public Object getTrainingType() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.TrainingType").list();
	}

	@Override
	public AddSpace getAddSpace() {
		Session session = sessionFactory.getCurrentSession();
		return (AddSpace) session.createQuery("from com.springboot.ecommerce.model.AddSpace").getSingleResult();
	}

	@Override
	public Object getTestimonials() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.UserTestimonial").list();
	}

	@Override
	public Object getBannerByCatId(int pc_id) {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.Banner b where b.catId =:catId")
				.setParameter("catId", pc_id).list();
	}

	@Override
	public Object getBannerBySubCatId(int psc_id) {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.Banner b where b.subCatId =:subCatId")
				.setParameter("subCatId", psc_id).list();
	}

	@Override
	public int getCategoryBySubCat(int pscId) {
		String sql = " select e.pc_id as sum from ProductCategory e where e.psc_id =" + pscId;
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		try {
			return (int) query.getFirstResult();
		} catch (Exception e) {
			logger.debug(" Error occured during getCategoryBySubCat " + e.getMessage());
			return 0;
		}

	}

	@Override
	public List<CatExportQunatity> exportSubCatQuantity() {
		// TODO Auto-generated method stub'
		try {
			List<CatExportQunatity> catList = new ArrayList<CatExportQunatity>();
			String catSql = "select e.pc_id,e.categoryName,e.categoryImg,SUM(pf.ProductFeatureValue) as value from ProductCategory e "
					+ " inner join product p on p.pc_id=e.pc_id "
					+ " inner join ProductFeatureValue pf on pf.p_id = p.p_id and pf.productFeatureKey like '%QUANTITY%'"
					+ " where e.pc_id in (3,5,12,13,16,17,19,24,29,31,34) group by e.categoryName,e.pc_id ";

			Session session = sessionFactory.getCurrentSession();
			SQLQuery query = session.createSQLQuery(catSql);
			List<Object[]> rows = query.list();
			for (Object[] row : rows) {
				CatExportQunatity cat = new CatExportQunatity();
				List<SubCatQunatity> subList = new ArrayList<SubCatQunatity>();
				cat.setCatId(String.valueOf(row[0]));
				cat.setCategoryName(String.valueOf(row[1]));
				cat.setCategoryImg(String.valueOf(row[2]));
				cat.setQuantity(String.valueOf(row[3]));
				cat.setType("Quintal");

				String subCatSql = " select psc.subCategoryName,SUM(pf.productFeatureValue),psc.subCategoryImg,psc.psc_id "
						+ " from ProductCategory e inner join ProductSubCategory psc on psc.pc_id=e.pc_id "
						+ " inner join product p on p.psc_id=psc.psc_id "
						+ " inner join ProductFeatureValue pf on pf.p_id=p.p_id and pf.productFeatureKey like '%QUANTITY%'"
						+ " where e.pc_id = " + cat.getCatId() + " group by psc.subCategoryName,psc.subCategoryImg,psc.psc_id ";
				SQLQuery query1 = session.createSQLQuery(subCatSql);
				List<Object[]> rowsForSub = query1.list();
				for (Object[] obj : rowsForSub) {
					SubCatQunatity subCat = new SubCatQunatity();
					subCat.setSubCategoryName(String.valueOf(obj[0]));
					subCat.setQuantity(String.valueOf(obj[1].toString()));
					if(String.valueOf(obj[2]) != null && !String.valueOf(obj[2]).isEmpty()){
						subCat.setSubCategoryImg(String.valueOf(obj[2]));
					}else{
					subCat.setSubCategoryImg("/img/subCat/noImg.webp");
					}
					subCat.setType("Quintal");
					subCat.setSubCatId((int) obj[3]);
					subList.add(subCat);
				}
				cat.setSubCat(subList);
				catList.add(cat);
			}
			return catList;
		} catch (Exception e) {
			logger.debug(" Error occured during getCategoryBySubCat " + e.getMessage());
		}
		return null;
	}

	@Override
	public int addBanner(Banner req) {
		Session session = sessionFactory.getCurrentSession();
		session.save(req);
		return req.getId();
	}

	@Override
	public Object getBannerLocation() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.BannerLocation").list();
	}

	@Override
	public int createContactUs(ContactUs loan) {
		Session session = sessionFactory.getCurrentSession();
		session.save(loan);
		return loan.getId();
	}

	@Override
	public List<Training> getTraining(int page, int size) {
		Session session = sessionFactory.getCurrentSession();
		return (List<Training>) session
				.createQuery("from com.springboot.ecommerce.model.Training obj ORDER BY obj.id desc ")
				.setFirstResult(page).setMaxResults(size).list();
	}

	@Override
	public List<Insurance> getInsurance(int page, int size) {
		Session session = sessionFactory.getCurrentSession();
		return (List<Insurance>) session
				.createQuery("from com.springboot.ecommerce.model.Insurance obj ORDER BY obj.id desc ")
				.setFirstResult(page).setMaxResults(size).list();
	}

	@Override
	public List<Loan> getLoan(int page, int size) {
		Session session = sessionFactory.getCurrentSession();
		return (List<Loan>) session.createQuery("from com.springboot.ecommerce.model.Loan obj ORDER BY obj.id desc ")
				.setFirstResult(page).setMaxResults(size).list();
	}

	@Override
	public int countLoan() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.Loan").list().size();
	}

	@Override
	public int countInsurance() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.Insurance").list().size();
	}

	@Override
	public int countTraining() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.Training").list().size();
	}

	@Override
	public int countAdBanner(String type) {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.Banner ").list().size();
	}

}
