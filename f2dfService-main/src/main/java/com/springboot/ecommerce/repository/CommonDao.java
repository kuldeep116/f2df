package com.springboot.ecommerce.repository;

import java.util.List;

import com.springboot.ecommerce.model.AppVersion;
import com.springboot.ecommerce.model.BuyAvailablity;
import com.springboot.ecommerce.model.DeliveryAddress;
import com.springboot.ecommerce.model.Enquiry;
import com.springboot.ecommerce.model.Events;
import com.springboot.ecommerce.model.Feedback;
import com.springboot.ecommerce.model.Images;
import com.springboot.ecommerce.model.MereDukan;
import com.springboot.ecommerce.model.News;
import com.springboot.ecommerce.model.SaleRequirement;
import com.springboot.ecommerce.model.TrainingAttended;
import com.springboot.ecommerce.model.Transaction;
import com.springboot.ecommerce.model.TransactionSale;
import com.springboot.ecommerce.model.TransactionSaleSold;
import com.springboot.ecommerce.model.UserTestimonial;
import com.springboot.ecommerce.pojo.ReportEnquiryResponse;
import com.springboot.ecommerce.pojo.ReportLeadResponse;
import com.springboot.ecommerce.pojo.ReportRequest;
import com.springboot.ecommerce.pojo.ReportUserResponse;
import com.springboot.ecommerce.pojo.RequestProduct;
import com.springboot.ecommerce.pojo.RequestTrans;

public interface CommonDao {
    	public int add(News news);

		public int updateNews(News news);

		public News getNewsById(int id);

		public List<News> getAllNews(int page, int size);

		int saveImage(Images img);

		public int addTestimonial(UserTestimonial testimonial);

		public List<Enquiry> getListOfInterested(int userId, String string);
		
		public int countInterested(int userId, String string);

		int addInterest(Enquiry enquiry);

		public List<TrainingAttended> getTrainingAttended(int userId);

		public MereDukan getDukanDetails(String dukanName);

		public String getDukanDetails(int userId);

		public void deleteNews(int id);

		public void addSellerReq(SaleRequirement requirement);

		public void buyerAvailablityAdd(BuyAvailablity requirement);

		public List<BuyAvailablity> getBuyerList(RequestProduct req);

		public List<SaleRequirement> getSellerList(RequestProduct req);

		public List<Enquiry> getInterestListByUser(int userId, String productType);

		public void deleteInterestListByUser(int id);

		void updateInterestListByUser(int id, int productPrice, int quantity);

		long totalPrice(int userId);

		public void saveTransaction(Transaction req);

		public void updateOrderInEnquiry(int id, String orderId, String status,String type);

		public int getAllNewsCount();

		public void addFeedback(Feedback req);

		public int countInterestedForSuperAdmin(String string);

		public List<News> getAllVideoNews(int pageNo, int size);

		public void deleteTestimonial(int id);

		public List<Events> getAllEvents(int pageNo, int size,String type);

		public Events getEventsById(int id);

		public int add(Events req);

		public int getAllEventsCount();

		AppVersion getVersion();

		void deleteEvent(int id);

		public List<ReportUserResponse> getAllUser(ReportRequest req);

		public List<ReportLeadResponse> getLeadDataWithDukanName(ReportRequest req);

		List<ReportEnquiryResponse> getEnquiryForProducts(ReportRequest req);

		List<ReportEnquiryResponse> getEnquiryByProductId(ReportRequest req);

		List<ReportEnquiryResponse> getAllInterestedForProducts(ReportRequest req);
		
		long totalLoanAmount();
		
		long totalInsuranceAmount();

		int productActiveLeadCount();

		public long totalAddAmount();

		public TransactionSale saveBuyTransaction(TransactionSale req);

		public TransactionSaleSold saveSoldTransaction(TransactionSaleSold req);

		public List<TransactionSaleSold> getSaleTrans(RequestTrans req);

		public List<BuyAvailablity> getBuyerProductList(RequestProduct req);

		public List<TransactionSale> getBuyTrans(RequestTrans req);

		public Object getOrderDetails(RequestTrans req);

		public List<TransactionSale> getSuccessBuyer(RequestTrans req);

		public void addDeliveryAddress(DeliveryAddress address);

		public List<DeliveryAddress> getDeliveryAddress(int userId);

		DeliveryAddress getAddressBasedOnId(int id);

		List<News> getAllNewsForSiteMap();

		List<News> getAllVideoNewsForSiteMap();

		long getCountOfSaleTrans();

}
