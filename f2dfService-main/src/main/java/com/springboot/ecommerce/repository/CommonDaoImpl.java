package com.springboot.ecommerce.repository;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.ecommerce.model.Address;
import com.springboot.ecommerce.model.AppVersion;
import com.springboot.ecommerce.model.BuyAvailablity;
import com.springboot.ecommerce.model.DeliveryAddress;
import com.springboot.ecommerce.model.Enquiry;
import com.springboot.ecommerce.model.Events;
import com.springboot.ecommerce.model.Feedback;
import com.springboot.ecommerce.model.Images;
import com.springboot.ecommerce.model.MereDukan;
import com.springboot.ecommerce.model.News;
import com.springboot.ecommerce.model.Product;
import com.springboot.ecommerce.model.SaleRequirement;
import com.springboot.ecommerce.model.TrainingAttended;
import com.springboot.ecommerce.model.Transaction;
import com.springboot.ecommerce.model.TransactionSale;
import com.springboot.ecommerce.model.TransactionSaleSold;
import com.springboot.ecommerce.model.User;
import com.springboot.ecommerce.model.UserTestimonial;
import com.springboot.ecommerce.pojo.ReportEnquiryResponse;
import com.springboot.ecommerce.pojo.ReportLeadResponse;
import com.springboot.ecommerce.pojo.ReportRequest;
import com.springboot.ecommerce.pojo.ReportUserResponse;
import com.springboot.ecommerce.pojo.RequestProduct;
import com.springboot.ecommerce.pojo.RequestTrans;
import com.springboot.ecommerce.service.ProductService;

@Repository
@Transactional
public class CommonDaoImpl implements CommonDao {

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	UserDao userdao;

	@Autowired
	private ProductService productService;

	private static Logger logger = Logger.getLogger(CommonDaoImpl.class.getName());
	DecimalFormat format = new DecimalFormat("##,##,##0");

	@Override
	public int add(News news) {
		Session session = sessionFactory.getCurrentSession();
		try {
			List<Long> imageIds = new ArrayList<>();
			for (Images img : news.getImages()) {
				session.save(img);
				imageIds.add(img.getId());
			}
			session.save(news);
			for (long id : imageIds) {
				session.createQuery(
						"update com.springboot.ecommerce.model.Images a  set a.newsId =:newsId where a.id=:id")
						.setParameter("newsId", news.getId()).setParameter("id", id).executeUpdate();

			}
			return news.getId();
		} catch (Exception e) {
			logger.debug("Error occured during add news " + e.getMessage());
			return 0;
		}
	}

	@Override
	public int saveImage(Images img) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(img);
			return img.getId().intValue();
		} catch (Exception e) {
			logger.debug("Error occured during add image " + e.getMessage());
			return 0;
		}
	}

	@Override
	public int updateNews(News news) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.createQuery(
					"update com.springboot.ecommerce.model.News a set a.title =:title,a.description =:description,a.keywords=:keywords,a.altTag =:altTag,a.url=:url where a.id=:id")
					.setParameter("title", news.getTitle()).setParameter("description", news.getDescription())
					.setParameter("keywords", news.getKeywords()).setParameter("altTag", news.getAltTag())
					.setParameter("url", news.getUrl()).setParameter("id", news.getId()).executeUpdate();
			return news.getId();
		} catch (Exception e) {
			logger.debug("Error occured during update news " + e.getMessage());
			return 0;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<News> getAllNews(int page, int size) {
		Session session = sessionFactory.getCurrentSession();
		try {
			List<News> news = session
					.createQuery("from com.springboot.ecommerce.model.News n where n.status='0' ORDER BY n.id desc")
					.setFirstResult(page).setMaxResults(size).getResultList();
			List<Images> imagesSet = new ArrayList<>();
			for (News newsObj : news) {
				imagesSet = session
						.createQuery("from com.springboot.ecommerce.model.Images img where img.newsId =:newsId ")
						.setParameter("newsId", newsObj.getId()).getResultList();
				newsObj.setImages(imagesSet);
			}
			return news;
		} catch (Exception e) {
			logger.debug("Error occured during getInterestListByUser " + e.getMessage());
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<News> getAllNewsForSiteMap() {
		Session session = sessionFactory.getCurrentSession();
		try {
			List<News> news = session
					.createQuery("from com.springboot.ecommerce.model.News n where n.status='0' ORDER BY n.id desc")
					.getResultList();
			return news;
		} catch (Exception e) {
			logger.debug("Error occured during getInterestListByUser " + e.getMessage());
			return null;
		}
	}

	@Override
	public News getNewsById(int id) {
		Session session = sessionFactory.getCurrentSession();
		try {
			News news = (News) session.get(News.class, id);
			List<Images> imagesSet = new ArrayList<>();
			imagesSet = session.createQuery("from com.springboot.ecommerce.model.Images img where img.newsId =:newsId ")
					.setParameter("newsId", news.getId()).getResultList();
			news.setImages(imagesSet);
			return news;
		} catch (Exception e) {
			logger.debug("Error occured during getInterestListByUser " + e.getMessage());
			return null;
		}
	}

	@Override
	public int addTestimonial(UserTestimonial testimonial) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.saveOrUpdate(testimonial);
			return testimonial.getId();
		} catch (Exception e) {
			logger.debug("Error occured during addTestimonial " + e.getMessage());
			return 0;
		}
	}

	@Override
	public int addInterest(Enquiry enquiry) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(enquiry);
			return enquiry.getId();
		} catch (Exception e) {
			logger.debug("Error occured during add addInterest " + e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Enquiry> getListOfInterested(int userId, String type) {
		Session session = sessionFactory.getCurrentSession();
		List<Enquiry> enquiries = new ArrayList<>();
		try {
			if (type.contains("All")) {
				enquiries = session.createQuery(
						"from com.springboot.ecommerce.model.Enquiry enquiry where enquiry.product_user_id =:user_id ORDER BY enquiry.id desc")
						.setParameter("user_id", userId).getResultList();

			} else {
				enquiries = session.createQuery(
						"from com.springboot.ecommerce.model.Enquiry enquiry where enquiry.type =:type and enquiry.product_user_id =:user_id ORDER BY enquiry.id desc")
						.setParameter("type", type).setParameter("user_id", userId).getResultList();

			}

			return enquiries;
		} catch (Exception e) {
			logger.debug("Error occured during getListOfInterested " + e.getMessage());
			return null;
		}
	}

	@Override
	public int countInterested(int userId, String type) {
		Session session = sessionFactory.getCurrentSession();
		List<Enquiry> enquiries = new ArrayList<>();
		try {
			if (type.contains("All")) {
				enquiries = session.createQuery(
						"from com.springboot.ecommerce.model.Enquiry enquiry where enquiry.product_user_id =:user_id ORDER BY enquiry.id desc")
						.setParameter("user_id", userId).getResultList();

			} else {
				enquiries = session.createQuery(
						"from com.springboot.ecommerce.model.Enquiry enquiry where enquiry.type =:type and enquiry.product_user_id =:user_id ORDER BY enquiry.id desc")
						.setParameter("type", type).setParameter("user_id", userId).getResultList();

			}
			return enquiries.size();
		} catch (Exception e) {
			logger.debug("Error occured during getInterestListByUser " + e.getMessage());
			return 0;
		}
	}

	@Override
	public List<TrainingAttended> getTrainingAttended(int userId) {
		Session session = sessionFactory.getCurrentSession();
		List<TrainingAttended> list = new ArrayList<>();
		try {
			list = session.createQuery(
					"from com.springboot.ecommerce.model.TrainingAttended enquiry where enquiry.user_id =:user_id ORDER BY enquiry.id desc")
					.setParameter("user_id", userId).getResultList();

			return list;
		} catch (Exception e) {
			logger.debug("Error occured during getInterestListByUser " + e.getMessage());
			return null;
		}
	}

	@Override
	public MereDukan getDukanDetails(String dukanName) {
		Session session = sessionFactory.getCurrentSession();
		MereDukan dukan = new MereDukan();
		try {
			return dukan = (MereDukan) session.createQuery(
					"from com.springboot.ecommerce.model.MereDukan meredukan where meredukan.dukanLink =:dukanName")
					.setParameter("dukanName", dukanName).getSingleResult();
		} catch (NoResultException nre) {

			return dukan;// Code for handling NoResultException
		} catch (NonUniqueResultException nure) {
			// Code for handling NonUniqueResultException
			return dukan;
		}

	}

	@Override
	public String getDukanDetails(int userId) {
		Session session = sessionFactory.getCurrentSession();
		MereDukan list = new MereDukan();
		try {
			list = (MereDukan) session
					.createQuery(
							"from com.springboot.ecommerce.model.MereDukan meredukan where meredukan.userId =:userId")
					.setParameter("userId", userId).getSingleResult();
			return list.getDukanLink();
		} catch (NoResultException nre) {
			// Code for handling NoResultException
		} catch (NonUniqueResultException nure) {
			// Code for handling NonUniqueResultException
		}
		return "";

	}

	@Override
	public void deleteNews(int id) {
		Session session = sessionFactory.getCurrentSession();
		session.createQuery(" update com.springboot.ecommerce.model.News p set p.status =:status where p.id=:id")
				.setParameter("status", "1").setParameter("id", id).executeUpdate();
	}

	@Override
	public void addSellerReq(SaleRequirement requirement) {
		// TODO Auto-generated method stub
		if (requirement.getProductName() != null) {
			Product product = getProductByName(requirement.getProductName());
			if (product != null) {
				System.out.println("Product category ::"+product.getPc_id());
				if (product.getProductCategory() != null)
					requirement.setPcId(product.getProductCategory().getPc_id());
				if (product.getSubCategory() != null)
					requirement.setPscId(product.getSubCategory().getPsc_id());
			}
		}
		Session session = sessionFactory.getCurrentSession();

		session.save(requirement);
	}

	private Product getProductByName(String name) {
		List<Product> productList = productService.getProductByName(name);
		System.out.println("Size is ::"+productList.size());
		if (!productList.isEmpty()) {
			return productList.get(0);
		}
		return null;
			
		
	}

	@Override
	public void buyerAvailablityAdd(BuyAvailablity requirement) {
		// TODO Auto-generated method stub
		if (requirement.getProductName() != null) {
			Product product = getProductByName(requirement.getProductName());
			if (product != null) {
				if (product.getProductCategory() != null)
					requirement.setPcId(product.getProductCategory().getPc_id());
				if (product.getSubCategory() != null)
					requirement.setPscId(product.getSubCategory().getPsc_id());
			}
		}
		Session session = sessionFactory.getCurrentSession();
		session.save(requirement);
	}

	@Override
	public List<BuyAvailablity> getBuyerList(RequestProduct req) {
		Session session = sessionFactory.getCurrentSession();
		try {
			List<BuyAvailablity> list = new ArrayList<>();
			list = session.createQuery("from com.springboot.ecommerce.model.BuyAvailablity buy order by buy.id desc")
					.setFirstResult(req.getPageNo()).setMaxResults(req.getSize()).list();

			return list;
		} catch (Exception e) {
			logger.debug("Error occured during getInterestListByUser " + e.getMessage());
			return null;
		}
	}

	@Override
	public List<SaleRequirement> getSellerList(RequestProduct req) {
		Session session = sessionFactory.getCurrentSession();
		try {
			List<SaleRequirement> list = new ArrayList<>();
			if (req.getPsc_id() > 0) {
				list = session
						.createQuery(
								"from com.springboot.ecommerce.model.SaleRequirement  sale where sale.pscId =:pscId order by sale.id desc")
						.setParameter("pscId", req.getPsc_id()).setFirstResult(req.getPageNo())
						.setMaxResults(req.getSize()).list();
				return list;

			}
			list = session.createQuery("from com.springboot.ecommerce.model.SaleRequirement sale order by sale.id desc")
					.setFirstResult(req.getPageNo()).setMaxResults(req.getSize()).list();

			return list;
		} catch (Exception e) {
			logger.debug("Error occured during getInterestListByUser " + e.getMessage());
			return null;
		}
	}

	@Override
	public List<Enquiry> getInterestListByUser(int userId, String type) {
		Session session = sessionFactory.getCurrentSession();
		List<Enquiry> enquiries = new ArrayList<>();
		try {
			enquiries = session.createQuery(
					"from com.springboot.ecommerce.model.Enquiry enquiry where enquiry.type =:type and enquiry.user_id =:user_id  ORDER BY enquiry.id desc")
					.setParameter("type", type).setParameter("user_id", userId).getResultList();

			return enquiries;
		} catch (Exception e) {
			logger.debug("Error occured during getInterestListByUser " + e.getMessage());
			return null;
		}
	}

	@Override
	public void deleteInterestListByUser(int id) {
		String sql = " delete from Enquiry e where e.id =" + id;
		Session session = sessionFactory.getCurrentSession();
		session.createQuery(sql).executeUpdate();
	}

	@Override
	public long totalPrice(int userId) {
		String sql = " select sum(e.price) as sum from Enquiry e where e.user_id =" + userId + " and e.type='Cart'";
		Session session = sessionFactory.getCurrentSession();
		try {
			return (long) session.createQuery(sql).getSingleResult();
		} catch (Exception e) {
			return 0;
		}

	}

	@Override
	public void updateInterestListByUser(int id, int productPrice, int quantity) {
		String sql = " update Enquiry e set e.qunatity =" + quantity + " ,e.price =" + productPrice + " where  e.id="
				+ id;
		Session session = sessionFactory.getCurrentSession();
		session.createQuery(sql).executeUpdate();
	}

	@Override
	public void updateOrderInEnquiry(int id, String orderId, String orderStatus, String type) {
		String sql = " update Enquiry e set e.orderId ='" + orderId + "' ,e.orderStatus ='" + orderStatus
				+ "',e.type ='" + type + "' where  e.id=" + id;
		Session session = sessionFactory.getCurrentSession();
		session.createQuery(sql).executeUpdate();
	}

	@Override
	public void saveTransaction(Transaction req) {
		Session session = sessionFactory.getCurrentSession();
		try {
			if (req.getPaymentId() != null) {
				List<Enquiry> list = getInterestListByUser(req.getUserId(), "Cart");
				for (Enquiry enquiry : list) {
					updateOrderInEnquiry(enquiry.getId(), req.getOrderId(), "PENDING", "Order");
				}
			}
			session.save(req);
		} catch (Exception e) {
			logger.debug("Error occured during save transaction " + e.getMessage());
		}
	}

	@Override
	public int getAllNewsCount() {
		String sql = "from com.springboot.ecommerce.model.News ";
		Session session = sessionFactory.getCurrentSession();
		try {
			return (int) session.createQuery(sql).getResultList().size();
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public void addFeedback(Feedback req) {
		Session session = sessionFactory.getCurrentSession();
		session.save(req);
	}

	@Override
	public int countInterestedForSuperAdmin(String string) {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.Enquiry enquiry ").getResultList().size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<News> getAllVideoNews(int pageNo, int size) {
		Session session = sessionFactory.getCurrentSession();
		try {
			@SuppressWarnings("unchecked")
			List<News> news = session.createQuery(
					"from com.springboot.ecommerce.model.News n where n.videoLink != null and n.videoLink != '' and n.status = '0' ORDER BY n.id desc")
					.setFirstResult(pageNo).setMaxResults(size).getResultList();
			List<Images> imagesSet = new ArrayList<>();
			for (News newsObj : news) {
				imagesSet = session
						.createQuery("from com.springboot.ecommerce.model.Images img where img.newsId =:newsId ")
						.setParameter("newsId", newsObj.getId()).getResultList();
				newsObj.setImages(imagesSet);
			}
			return news;
		} catch (Exception e) {
			logger.debug("Error occured during getInterestListByUser " + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<News> getAllVideoNewsForSiteMap() {
		Session session = sessionFactory.getCurrentSession();
		try {
			@SuppressWarnings("unchecked")
			List<News> news = session.createQuery(
					"from com.springboot.ecommerce.model.News n where n.videoLink != null and n.videoLink != '' and n.status = '0' ORDER BY n.id desc")
					.getResultList();

			return news;
		} catch (Exception e) {
			logger.debug("Error occured during getInterestListByUser " + e.getMessage());
			return null;
		}
	}

	@Override
	public void deleteTestimonial(int id) {
		String sql = " delete from UserTestimonial p where p.id =" + id;
		Session session = sessionFactory.getCurrentSession();
		session.createQuery(sql).executeUpdate();

	}

	@Override
	public void deleteEvent(int id) {
		String sql = " delete from Events p where p.id =" + id;
		Session session = sessionFactory.getCurrentSession();
		session.createQuery(sql).executeUpdate();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Events> getAllEvents(int pageNo, int size, String type) {
		Session session = sessionFactory.getCurrentSession();
		List<Events> list = new ArrayList<>();
		try {
			if (type.contains("successfull")) {
				list = session.createQuery("from com.springboot.ecommerce.model.Events n ORDER BY n.id desc")
						.setFirstResult(pageNo).setMaxResults(size).getResultList();
			} else {
				list = session.createQuery("from com.springboot.ecommerce.model.Events n ORDER BY n.id desc")
						.setFirstResult(pageNo).setMaxResults(size).getResultList();
			}

			List<Images> imagesSet = new ArrayList<>();
			for (Events obj : list) {
				imagesSet = session
						.createQuery("from com.springboot.ecommerce.model.Images img where img.eventsId =:id ")
						.setParameter("id", obj.getId()).getResultList();
				obj.setImages(imagesSet);
			}
			return list;
		} catch (Exception e) {
			logger.debug("Error occured during getAllEvents " + e.getMessage());
			return null;
		}
	}

	@Override
	public Events getEventsById(int id) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Events obj = (Events) session.get(Events.class, id);
			List<Images> imagesSet = new ArrayList<>();
			imagesSet = session.createQuery("from com.springboot.ecommerce.model.Images img where img.eventsId =:id ")
					.setParameter("id", obj.getId()).getResultList();
			obj.setImages(imagesSet);
			return obj;
		} catch (Exception e) {
			logger.debug("Error occured during getEventsById " + e.getMessage());
			return null;
		}
	}

	@Override
	public int add(Events req) {
		Session session = sessionFactory.getCurrentSession();
		try {
			List<Long> imageIds = new ArrayList<>();
			for (Images img : req.getImages()) {
				logger.debug("Error occured during add events " + img.getFilePath());
				session.save(img);
				imageIds.add(img.getId());
			}
			session.save(req);
			for (long id : imageIds) {
				session.createQuery(
						" update com.springboot.ecommerce.model.Images a set a.eventsId =:eventsId where a.id=:id")
						.setParameter("eventsId", req.getId()).setParameter("id", id).executeUpdate();

			}
			return req.getId();
		} catch (Exception e) {
			logger.debug("Error occured during add events " + e.getMessage());
			return 0;
		}
	}

	@Override
	public int getAllEventsCount() {
		String sql = "from com.springboot.ecommerce.model.Events ";
		Session session = sessionFactory.getCurrentSession();
		try {
			return (int) session.createQuery(sql).getResultList().size();
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public AppVersion getVersion() {
		Session session = sessionFactory.getCurrentSession();
		return (AppVersion) session.createQuery("from com.springboot.ecommerce.model.AppVersion").getSingleResult();
	}

	@Override
	public List<ReportUserResponse> getAllUser(ReportRequest req) {
		List<ReportUserResponse> list = new ArrayList<ReportUserResponse>();
		String userSql = "";
		if (req.getType().contains("All")) {
			userSql = "SELECT u.name as userName,u.mobile as moblie,u.createDate as createDate,IF(a.city is null, '-',a.city) as city,IF(a.state is null, '-',a.state) as state,IF(a.country is null, '-',a.country) as country,IF(a.pincode is null, '-',a.pincode) as pincode,IF(m.name is null, '-',m.name) as dukanName,IF(m.dukanLink is null, '-',m.dukanLink) as dukanLink"
					+ " FROM User u " + " left join Address a on a.userId=u.id "
					+ " left join MereDukan m on m.userId=u.id order by u.id desc limit " + req.getPageNo() + ","
					+ req.getSize();
		} else {
			userSql = "SELECT u.name as userName,u.mobile as moblie,u.createDate as createDate,IF(a.city is null, '-',a.city) as city,IF(a.state is null, '-',a.state) as state,IF(a.country is null, '-',a.country) as country,IF(a.pincode is null, '-',a.pincode) as pincode,IF(m.name is null, '-',m.name) as dukanName,IF(m.dukanLink is null, '-',m.dukanLink) as dukanLink"
					+ " FROM User u " + " left join Address a on a.userId=u.id "
					+ " left join MereDukan m on m.userId=u.id where u.userType like '%" + req.getType()
					+ "%' order by u.id desc limit " + req.getPageNo() + "," + req.getSize();
		}

		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(userSql);
		List<Object[]> rowsForSub = query.list();
		for (Object[] obj : rowsForSub) {
			ReportUserResponse model = new ReportUserResponse();
			model.setUserName(String.valueOf(obj[0]));
			model.setUserContact(String.valueOf(obj[1]));
			model.setUserCreateDate(String.valueOf(obj[2]));
			model.setCity(String.valueOf(obj[3]));
			model.setState(String.valueOf(obj[4]));
			model.setCountry(String.valueOf(obj[5]));
			model.setPincode(String.valueOf(obj[6]));
			model.setDukanName(String.valueOf(obj[7]));
			model.setDukanLink("https://f2df.com/my-online-dukan/" + String.valueOf(obj[8]));
			list.add(model);
		}
		return list;
	}

	@Override
	public List<ReportLeadResponse> getLeadDataWithDukanName(ReportRequest req) {
		List<ReportLeadResponse> list = new ArrayList<>();
		String sql = "";
		if (checkUserRole(req.getUserId()).contains("SuperAdmin")) {
			if (req.getType().contains("BUSSDONE")) {
				sql = "SELECT p.p_name,p.p_fee,IF(p.typeOfProduct is null, '-',p.typeOfProduct),pc.categoryName,psc.subCategoryName,"
						+ " IF(pf.productFeatureValue is null, '1- ',pf.productFeatureValue),IF(p.productDesc is null, 'Active',p.productDesc),IF(m.name is null, '-',m.name) as dukanname,"
						+ " IF(m.dukanLink is null, '-',m.dukanLink),u.name as username,u.mobile,IF(a.city is null, '-',a.city),"
						+ " IF(a.state is null, '-',a.state), "
						+ " IF(a.pincode is null, '-',a.pincode),IF(p.createDate is null, '-',p.createDate) FROM product p "
						+ " inner join ProductCategory pc on pc.pc_id=p.pc_id"
						+ " inner join ProductSubCategory psc on psc.psc_id=p.psc_id"
						+ " inner join User u on u.id=p.userId " + " inner join Address a on a.userId=p.userId"
						+ " left  join ProductFeatureValue pf on pf.p_id=p.p_id and pf.productFeatureKey like '%QUANTITY%'"
						+ " left  join MereDukan m on m.userId=p.userId where p.productDesc ='BUSS DONE' or p.productDesc ='PARTIAL DONE' order by p.p_id desc limit "
						+ req.getPageNo() + "," + req.getSize();
			} else if (req.getType().contains("Active")) {
				sql = "SELECT p.p_name,p.p_fee,IF(p.typeOfProduct is null, '-',p.typeOfProduct),pc.categoryName,psc.subCategoryName,"
						+ " IF(pf.productFeatureValue is null, '1- ',pf.productFeatureValue),IF(p.productDesc is null, 'Active',p.productDesc),IF(m.name is null, '-',m.name) as dukanname,"
						+ " IF(m.dukanLink is null, '-',m.dukanLink),u.name as username,u.mobile,IF(a.city is null, '-',a.city),"
						+ " IF(a.state is null, '-',a.state), "
						+ " IF(a.pincode is null, '-',a.pincode),IF(p.createDate is null, '-',p.createDate) FROM product p "
						+ " inner join ProductCategory pc on pc.pc_id=p.pc_id"
						+ " inner join ProductSubCategory psc on psc.psc_id=p.psc_id"
						+ " inner join User u on u.id=p.userId " + " inner join Address a on a.userId=p.userId"
						+ " left  join ProductFeatureValue pf on pf.p_id=p.p_id and pf.productFeatureKey like '%QUANTITY%'"
						+ " left  join MereDukan m on m.userId=p.userId where p.productDesc ='Active' order by p.p_id desc limit "
						+ req.getPageNo() + "," + req.getSize();
			} else {
				sql = "SELECT p.p_name,p.p_fee,IF(p.typeOfProduct is null, '-',p.typeOfProduct),pc.categoryName,psc.subCategoryName,"
						+ " IF(pf.productFeatureValue is null, '1- ',pf.productFeatureValue),IF(p.productDesc is null, 'Active',p.productDesc),IF(m.name is null, '-',m.name) as dukanname,"
						+ " IF(m.dukanLink is null, '-',m.dukanLink),u.name as username,u.mobile,IF(a.city is null, '-',a.city),"
						+ " IF(a.state is null, '-',a.state), "
						+ " IF(a.pincode is null, '-',a.pincode),IF(p.createDate is null, '-',p.createDate) FROM product p "
						+ " inner join ProductCategory pc on pc.pc_id=p.pc_id"
						+ " inner join ProductSubCategory psc on psc.psc_id=p.psc_id"
						+ " inner join User u on u.id=p.userId " + " inner join Address a on a.userId=p.userId"
						+ " left  join ProductFeatureValue pf on pf.p_id=p.p_id and pf.productFeatureKey like '%QUANTITY%'"
						+ " left  join MereDukan m on m.userId=p.userId order by p.p_id desc limit " + req.getPageNo()
						+ "," + req.getSize();
			}
		} else {
			if (req.getType().contains("BUSSDONE")) {
				sql = "SELECT p.p_name,p.p_fee,IF(p.typeOfProduct is null, '-',p.typeOfProduct),pc.categoryName,psc.subCategoryName,"
						+ " IF(pf.productFeatureValue is null, '1- ',pf.productFeatureValue),IF(p.productDesc is null, 'Active',p.productDesc),IF(m.name is null, '-',m.name) as dukanname,"
						+ " IF(m.dukanLink is null, '-',m.dukanLink),u.name as username,u.mobile,IF(a.city is null, '-',a.city),"
						+ " IF(a.state is null, '-',a.state), "
						+ " IF(a.pincode is null, '-',a.pincode),IF(p.createDate is null, '-',p.createDate) FROM product p "
						+ " inner join ProductCategory pc on pc.pc_id=p.pc_id"
						+ " inner join ProductSubCategory psc on psc.psc_id=p.psc_id" + " inner join User u on u.id="
						+ req.getUserId() + " inner join Address a on a.userId=" + req.getUserId()
						+ " left  join ProductFeatureValue pf on pf.p_id=p.p_id and pf.productFeatureKey like '%QUANTITY%'"
						+ " left  join MereDukan m on m.userId=p.userId where p.productDesc ='BUSS DONE' or p.productDesc ='PARTIAL DONE' and p.userId="
						+ req.getUserId() + " order by p.p_id desc limit " + req.getPageNo() + "," + req.getSize();
			} else if (req.getType().contains("Active")) {
				sql = "SELECT p.p_name,p.p_fee,IF(p.typeOfProduct is null, '-',p.typeOfProduct),pc.categoryName,psc.subCategoryName,"
						+ " IF(pf.productFeatureValue is null, '1- ',pf.productFeatureValue),IF(p.productDesc is null, 'Active',p.productDesc),IF(m.name is null, '-',m.name) as dukanname,"
						+ " IF(m.dukanLink is null, '-',m.dukanLink),u.name as username,u.mobile,IF(a.city is null, '-',a.city),"
						+ " IF(a.state is null, '-',a.state), "
						+ " IF(a.pincode is null, '-',a.pincode),IF(p.createDate is null, '-',p.createDate) FROM product p "
						+ " inner join ProductCategory pc on pc.pc_id=p.pc_id"
						+ " inner join ProductSubCategory psc on psc.psc_id=p.psc_id" + " inner join User u on u.id="
						+ req.getUserId() + " inner join Address a on a.userId=" + req.getUserId()
						+ " left  join ProductFeatureValue pf on pf.p_id=p.p_id and pf.productFeatureKey like '%QUANTITY%'"
						+ " left  join MereDukan m on m.userId=p.userId where p.productDesc ='Active' and p.userId="
						+ req.getUserId() + " and p.userId=" + req.getUserId() + " order by p.p_id desc limit "
						+ req.getPageNo() + "," + req.getSize();
			} else {
				sql = "SELECT p.p_name,p.p_fee,IF(p.typeOfProduct is null, '-',p.typeOfProduct),pc.categoryName,psc.subCategoryName,"
						+ " IF(pf.productFeatureValue is null, '1- ',pf.productFeatureValue),IF(p.productDesc is null, 'Active',p.productDesc),IF(m.name is null, '-',m.name) as dukanname,"
						+ " IF(m.dukanLink is null, '-',m.dukanLink),u.name as username,u.mobile,IF(a.city is null, '-',a.city),"
						+ " IF(a.state is null, '-',a.state), "
						+ " IF(a.pincode is null, '-',a.pincode),IF(p.createDate is null, '-',p.createDate) FROM product p "
						+ " inner join ProductCategory pc on pc.pc_id=p.pc_id"
						+ " inner join ProductSubCategory psc on psc.psc_id=p.psc_id" + " inner join User u on u.id="
						+ req.getUserId() + " inner join Address a on a.userId=" + req.getUserId()
						+ " left  join ProductFeatureValue pf on pf.p_id=p.p_id and pf.productFeatureKey like '%QUANTITY%'"
						+ " left  join MereDukan m on m.userId=p.userId and p.userId=" + req.getUserId()
						+ " order by p.p_id desc limit " + req.getPageNo() + "," + req.getSize();
			}
		}
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		List<Object[]> rowsForSub = query.list();
		for (Object[] obj : rowsForSub) {
			ReportLeadResponse model = new ReportLeadResponse();
			model.setProductName(String.valueOf(obj[0]));
			model.setProductPrice(String.valueOf(obj[1]));
			model.setProductType(String.valueOf(obj[2]).replaceAll("/", ""));
			model.setCategoryName(String.valueOf(obj[3]));
			model.setSubCategoryName(String.valueOf(obj[4]));
			String value = String.valueOf(obj[5]);
			logger.debug("value --" + value);
			if (value != null) {
				if (value.contains("-")) {
					logger.debug("value 1--" + value);
					model.setQuantity(value.split("-")[0]);
					model.setType(value.split("-")[1]);
				} else {
					logger.debug("value 2 --" + value);
					model.setQuantity(value);
					model.setType("-");
				}
			} else {
				model.setQuantity("-");
				model.setType("-");
			}
			// model.setQuantity(String.valueOf(obj[5]));
			model.setProductStatus(String.valueOf(obj[6]));
			model.setDukanName(String.valueOf(obj[7]));
			model.setDukanLink("https://f2df.com/my-online-dukan/" + String.valueOf(obj[8]));
			model.setUserName(String.valueOf(obj[9]));
			model.setUserContactNo(String.valueOf(obj[10]));
			model.setCity(String.valueOf(obj[11]));
			model.setState(String.valueOf(obj[12]));
			model.setPincode(String.valueOf(obj[13]));
			model.setPostedDate(String.valueOf(obj[14]));
			try {
				if (!model.getQuantity().contains("-")) {
					int sumOfPrice = Integer.valueOf(model.getProductPrice().trim())
							* Integer.valueOf(model.getQuantity().trim());
					model.setProductValue(String.valueOf(format.format(sumOfPrice)));
				} else {
					model.setProductValue("-");
				}
			} catch (Exception e) {
				logger.debug("Error MSG in " + model.getProductPrice() + "" + e.getMessage());
			}
			list.add(model);
		}
		return list;
	}

	public String checkUserRole(int id) {
		Session session = sessionFactory.getCurrentSession();
		User user = new User();
		String role = "user";
		try {
			user = (User) session.createQuery(" select u from com.springboot.ecommerce.model.User u where u.id =:id")
					.setParameter("id", id).getSingleResult();
		} catch (NoResultException nre) {
			// Code for handling NoResultException
		} catch (NonUniqueResultException nure) {
			// Code for handling NonUniqueResultException
		}
		if (user.getRole() == null) {
			role = "user";
		} else {
			role = user.getRole();
		}
		logger.debug("User role " + role);
		return role;
	}

	@Override
	public List<ReportEnquiryResponse> getEnquiryForProducts(ReportRequest req) {
		List<ReportEnquiryResponse> list = new ArrayList<>();
		String sql = "";

		if (checkUserRole(req.getUserId()).contains("SuperAdmin")) {
			sql = "select u.name,e.mobile,e.qunatity,p.p_name,e.createDate from Enquiry e, product p,User u "
					+ " where e.product_id = p.p_id and e.type = 'Enquiry' and u.id = e.user_id order by e.id desc limit "
					+ req.getPageNo() + "," + req.getSize();
		} else {
			sql = "select u.name,e.mobile,e.qunatity,p.p_name,e.createDate from Enquiry e, product p,User u where "
					+ " p.userId=" + req.getUserId()
					+ " and e.product_id = p.p_id and e.type ='Enquiry' and u.id=e.user_Id order by e.id desc ";
		}
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		List<Object[]> rowsForSub = query.list();
		for (Object[] obj : rowsForSub) {
			ReportEnquiryResponse model = new ReportEnquiryResponse();
			model.setUserName(String.valueOf(obj[0]));
			model.setUserContact(String.valueOf(obj[1]));
			model.setQuantity(String.valueOf(obj[2]));
			model.setProductName(String.valueOf(obj[3]));
			model.setCreateDate(String.valueOf(obj[4]));
			list.add(model);
		}
		return list;
	}

	@Override
	public List<ReportEnquiryResponse> getAllInterestedForProducts(ReportRequest req) {
		List<ReportEnquiryResponse> list = new ArrayList<>();
		String sql = "";
		logger.debug("User id" + req.getUserId());
		sql = "select u.name,e.mobile,e.qunatity,p.p_name,e.createDate from Enquiry e, product p,User u "
				+ " where e.product_id = p.p_id  and p.user_id= " + req.getUserId() + " and u.id = " + req.getUserId()
				+ " order by e.id desc ";

		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		List<Object[]> rowsForSub = query.list();
		for (Object[] obj : rowsForSub) {
			ReportEnquiryResponse model = new ReportEnquiryResponse();
			model.setUserName(String.valueOf(obj[0]));
			model.setUserContact(String.valueOf(obj[1]));
			model.setQuantity(String.valueOf(obj[2]));
			model.setProductName(String.valueOf(obj[3]));
			model.setCreateDate(String.valueOf(obj[4]));
			list.add(model);
		}
		return list;
	}

	@Override
	public List<ReportEnquiryResponse> getEnquiryByProductId(ReportRequest req) {
		List<ReportEnquiryResponse> list = new ArrayList<>();
		String sql = "";

		sql = "select u.name,e.mobile,e.qunatity,p.p_name,e.createDate from Enquiry e, product p,User u "
				+ " where e.product_id = " + req.getProductId() + " and e.type = 'Enquiry' and u.id = "
				+ req.getUserId();
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		List<Object[]> rowsForSub = query.list();
		for (Object[] obj : rowsForSub) {
			ReportEnquiryResponse model = new ReportEnquiryResponse();
			model.setUserName(String.valueOf(obj[0]));
			model.setUserContact(String.valueOf(obj[1]));
			model.setQuantity(String.valueOf(obj[2]));
			model.setProductName(String.valueOf(obj[3]));
			model.setCreateDate(String.valueOf(obj[4]));
			list.add(model);
		}
		return list;
	}

	@Override
	public long totalLoanAmount() {
		String sql = " select sum(e.loanAmount) as sum from Loan e ";
		Session session = sessionFactory.getCurrentSession();
		try {
			return (long) session.createQuery(sql).getSingleResult();
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public long totalInsuranceAmount() {
		String sql = " select sum(e.cropAmount) as sum from Insurance e ";
		Session session = sessionFactory.getCurrentSession();
		try {
			return (long) session.createQuery(sql).getSingleResult();
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public int productActiveLeadCount() {
		Session session = sessionFactory.getCurrentSession();
		return session.createQuery("from com.springboot.ecommerce.model.Product p where p.productDesc ='Active'").list()
				.size();
	}

	@Override
	public long totalAddAmount() {
		String sql = " select sum(e.amount) as sum from Banner e ";
		Session session = sessionFactory.getCurrentSession();
		try {
			return (long) session.createQuery(sql).getSingleResult();
		} catch (Exception e) {
			return 0;
		}

	}

	@Override
	public TransactionSale saveBuyTransaction(TransactionSale req) {
		Session session = sessionFactory.getCurrentSession();
		if (req.getId() > 0) {
			session.createQuery(
					"update com.springboot.ecommerce.model.TransactionSale a  set a.status =:status where a.id=:id")
					.setParameter("status", "Sold").setParameter("id", req.getId()).executeUpdate();
		} else {
			session.save(req);
		}
		return req;
	}

	@Override
	public TransactionSaleSold saveSoldTransaction(TransactionSaleSold req) {
		Session session = sessionFactory.getCurrentSession();
		session.save(req);
		return req;
	}

	@Override
	public List<TransactionSaleSold> getSaleTrans(RequestTrans req) {
		Session session = sessionFactory.getCurrentSession();
		List<TransactionSaleSold> transSaleSold = new ArrayList<>();
		transSaleSold = session.createQuery("from com.springboot.ecommerce.model.TransactionSaleSold u order by id desc").setFirstResult(req.getPageNo()).setMaxResults(req.getSize()).list();
		return transSaleSold;
	}
	
	@Override
	public long getCountOfSaleTrans() {
	    Session session = sessionFactory.getCurrentSession();
	    
	    // Execute a count query to get the total number of transactions
	    Long count = (Long) session.createQuery("select count(*) from com.springboot.ecommerce.model.TransactionSaleSold").uniqueResult();

	    // Return the count (or 0 if count is null)
	    return count != null ? count : 0;
	}


	@Override
	public List<BuyAvailablity> getBuyerProductList(RequestProduct req) {
		Session session = sessionFactory.getCurrentSession();
		List<BuyAvailablity> list = new ArrayList<>();
		String query = "select p.p_id,p.p_name,u.name,SUM(pf.productFeatureValue),psc.subCategoryName,p.p_fee,u.id,u.mobile "
				+ " from product p" + " inner join User u on u.id=p.userId"
				+ " inner join ProductSubCategory psc on psc.psc_id=p.psc_id"
				+ " inner join ProductFeatureValue pf on pf.p_id=p.p_id and pf.productFeatureKey like '%QUANTITY%'"
				+ " where p.psc_id= " + req.getPsc_id()
				+ " group by p.p_name,u.name,psc.subCategoryName,p.p_id,p.p_fee,u.id,u.mobile";
		SQLQuery query1 = session.createSQLQuery(query);
		List<Object[]> rowsForSub = query1.list();
		for (Object[] obj : rowsForSub) {
			BuyAvailablity raw = new BuyAvailablity();
			raw.setProductId((int) obj[0]);
			raw.setProductName(String.valueOf(obj[1]));
			raw.setName(String.valueOf(obj[2]));
			raw.setQuantity(String.valueOf(obj[3].toString()));
			raw.setSubCatName(String.valueOf(obj[4]));
			raw.setOfferPrice(String.valueOf(obj[5]));
			raw.setUserId((int) obj[6]);
			raw.setPhoneNumber(String.valueOf(obj[7]));
			Address address = new Address();
			address = userdao.getAddress(raw.getUserId());
			raw.setLocation(address.getAddress1() + " " + address.getAddress2() + " " + address.getState());
			list.add(raw);
		}
		return list;
	}

	@Override
	public List<TransactionSale> getBuyTrans(RequestTrans req) {
		Session session = sessionFactory.getCurrentSession();
		List<TransactionSale> transSaleBuy = new ArrayList<>();
		transSaleBuy = session.createQuery(
				"from com.springboot.ecommerce.model.TransactionSale u where  u.status =:status and u.mobile =:mobile ")
				.setParameter("status", req.getStatus()).setParameter("mobile", req.getMobile()).list();
		return transSaleBuy;
	}

	@Override
	public Object getOrderDetails(RequestTrans req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionSale> getSuccessBuyer(RequestTrans req) {
		Session session = sessionFactory.getCurrentSession();
		List<Integer> buyerIds = new ArrayList<>();
		for (String s : req.getIds().split(",")) {
			buyerIds.add(Integer.valueOf(s));
		}
		List<TransactionSale> transSaleBuy = new ArrayList<>();
		transSaleBuy = session.createQuery("from TransactionSale t where t.id in :ids ").setParameter("ids", buyerIds)
				.list();
		return transSaleBuy;
	}

	@Override
	public void addDeliveryAddress(DeliveryAddress address) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		try {
			if (address.getId() != 0) {
				session.update(address);
			} else {
				session.save(address);
			}

		} catch (Exception e) {
			logger.debug("Error occured during add DeliveryAddress " + e.getMessage());
		}

	}

	@Override
	public List<DeliveryAddress> getDeliveryAddress(int userId) {
		Session session = sessionFactory.getCurrentSession();
		List<DeliveryAddress> list = new ArrayList<>();
		list = session.createQuery("from DeliveryAddress t where t.userId in :userId ").setParameter("userId", userId)
				.list();
		return list;
	}

	@Override
	public DeliveryAddress getAddressBasedOnId(int id) {
		Session session = sessionFactory.getCurrentSession();

		return (DeliveryAddress) session.createQuery("from DeliveryAddress t where t.id=:id ").setParameter("id", id)
				.getSingleResult();

	}
}
