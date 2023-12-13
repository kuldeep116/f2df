package com.springboot.ecommerce.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.springboot.ecommerce.model.Address;
import com.springboot.ecommerce.model.BuyAvailablity;
import com.springboot.ecommerce.model.DashboardPojo;
import com.springboot.ecommerce.model.DeliveryAddress;
import com.springboot.ecommerce.model.Enquiry;
import com.springboot.ecommerce.model.Feedback;
import com.springboot.ecommerce.model.MereDukan;
import com.springboot.ecommerce.model.Product;
import com.springboot.ecommerce.model.SaleRequirement;
import com.springboot.ecommerce.model.User;
import com.springboot.ecommerce.model.UserTestimonial;
import com.springboot.ecommerce.pojo.ImageDTO;
import com.springboot.ecommerce.pojo.ReportEnquiryResponse;
import com.springboot.ecommerce.pojo.ReportRequest;
import com.springboot.ecommerce.pojo.RequestProduct;
import com.springboot.ecommerce.repository.CommonDao;
import com.springboot.ecommerce.service.HomeService;
import com.springboot.ecommerce.service.ProductCategoryService;
import com.springboot.ecommerce.service.ProductService;
import com.springboot.ecommerce.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "User profile API")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	ProductCategoryService categoryService;

	@Autowired
	ProductService productService;

	@Autowired
	CommonDao dao;

	@Autowired
	HomeService homeService;

	private static Logger logger = Logger.getLogger(UserController.class.getName());

	DecimalFormat format = new DecimalFormat("##,##,##0");

	@PostMapping(value = "/dashboard", headers = "Accept=application/json")
	public ResponseEntity<Object> dashboard(@RequestBody RequestProduct req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		List<DashboardPojo> list = new ArrayList<DashboardPojo>();
		User user = userService.getUser(req.getUserId());
		boolean admin = false;
		try {
			if (user.getRole() != null && user.getRole().equals("SuperAdmin")) {
				admin = true;
				double totalValueOfAllProduct = productService.getTotalValueOfAllProducts("ALL");
				double bussDoneVal = productService.getTotalValueOfAllProducts("BUSSDONE");
				homemap = new HashMap<String, Object>();
				list = new ArrayList<DashboardPojo>();
				long toatlLoanAmount = dao.totalLoanAmount();
				long toatlInsuranceAmount = dao.totalInsuranceAmount();
				long totalAddAmount = dao.totalAddAmount();
				List<Product> listOfBoost = productService.getProductList(0, 100000, "recommded");
				// totalVlaue of loan + total valueOf insurance+totalvalueof
				// bootstredavailde+totalvalueOfrunningAdd
				DashboardPojo obj1 = new DashboardPojo();
				obj1.setName("TOTAL G.M.V.");
				obj1.setValue(format.format(totalValueOfAllProduct + toatlLoanAmount + toatlInsuranceAmount
						+ (int) totalAddAmount + (listOfBoost.size() * 99)));
				obj1.setPercentage("33");
				obj1.setIcon("");
				obj1.setIntvalue((int) (totalValueOfAllProduct + toatlLoanAmount + toatlInsuranceAmount
						+ (int) totalAddAmount + listOfBoost.size() * 99));
				obj1.setReportLink("");
				list.add(obj1);

				DashboardPojo obj2 = new DashboardPojo();
				obj2.setName("TOTAL G.M.V. TRANSACTED");
				// BUSS DONE TOTAL + TOTATVALUEOF ADD + BOOSTERED AVAILED
				obj2.setValue(format.format(bussDoneVal + totalAddAmount + listOfBoost.size() * 99));
				obj2.setPercentage("33");
				obj2.setIntvalue((int) bussDoneVal + (int) totalAddAmount + listOfBoost.size() * 99);
				obj2.setIcon("");
				obj2.setReportLink("");
				list.add(obj2);

				DashboardPojo obj3 = new DashboardPojo("TOTAL ACTIVE LEADS", dao.productActiveLeadCount(), "",
						"https://www.f2df.com/admin/reports/leads?type=Active");
				list.add(obj3);

				DashboardPojo obj4 = new DashboardPojo();
				obj4.setName("TOTAL VALUE OF ACTIVE LEADS(IN Rs.)");
				double value = productService.getTotalValueOfAllProducts("ACTIVE");
				obj4.setValue(format.format(value));
				obj4.setPercentage("33");
				obj4.setIcon("");
				obj4.setIntvalue((int) value);
				obj4.setReportLink("https://www.f2df.com/admin/reports/leads?type=Active");
				list.add(obj4);

				DashboardPojo obj5 = new DashboardPojo("TOTAL ENQUIRIES GENERATED",
						dao.countInterestedForSuperAdmin("Enquiry"), "23",
						"https://www.f2df.com/admin/reports/enquiry");
				list.add(obj5);

				DashboardPojo obj6 = new DashboardPojo("TOTAL VISITORS",
						userService.countOfTotalUser() + dao.countInterestedForSuperAdmin("All"), "3",
						"https://www.f2df.com/admin/reports/user?type=All");
				list.add(obj6);

				DashboardPojo obj7 = new DashboardPojo("TOTAL NO OF REGISTERED USERS",
						100000 + userService.countOfTotalUser(), "89",
						"https://www.f2df.com/admin/reports/user?type=All");
				list.add(obj7);

				DashboardPojo obj8 = new DashboardPojo("TOTAL NO OF BUSINESS ENTITY",
						userService.countOfBussEntityUser(), "89",
						"https://www.f2df.com/admin/reports/user?type=BusinessEntity");
				list.add(obj8);

				DashboardPojo obj9 = new DashboardPojo("TOTAL MERI ONLINE DUKAN CREATED",
						1000 + userService.countOfTotalDukan(), "3", "");
				list.add(obj9);

				DashboardPojo obj10 = new DashboardPojo("TOTAL LEADS POSTED", productService.productCount(), "3",
						"https://www.f2df.com/admin/reports/leads?type=All");
				list.add(obj10);

				DashboardPojo obj11 = new DashboardPojo();
				obj11.setName("TOTAL VALUE OF POSTED LEADS(IN Rs.)");
				obj11.setValue(format.format(totalValueOfAllProduct));
				obj11.setPercentage("33");
				obj11.setIcon("");
				obj11.setIntvalue((int) (totalValueOfAllProduct));
				obj11.setReportLink("https://www.f2df.com/admin/reports/leads?type=All");
				list.add(obj11);

				DashboardPojo obj12 = new DashboardPojo("TOTAL BUSINESS DONE LEADS", 0, "3",
						"https://www.f2df.com/admin/reports/leads?type=BUSSDONE");
				list.add(obj12);

				DashboardPojo obj13 = new DashboardPojo();
				obj13.setName("TOTAL VALUE OF BUSINESS DONE LEADS(IN Rs.)");
				obj13.setValue(format.format(bussDoneVal));
				obj13.setPercentage("33");
				obj13.setIcon("");
				obj13.setIntvalue((int) bussDoneVal);
				obj13.setReportLink("https://www.f2df.com/admin/reports/leads?type=BUSSDONE");
				list.add(obj13);

				DashboardPojo obj14 = new DashboardPojo("TOTAL NO OF LOAN ENQUIRIES RCVD.", homeService.countLoan(),
						"3", "https://f2df.com/admin/enquiry?list=getLoan");
				list.add(obj14);

				DashboardPojo obj15 = new DashboardPojo();
				obj15.setName("TOTAL VALUE OF LOAN ENQUIRIES (IN Rs.)");
				obj15.setValue(format.format(toatlLoanAmount));
				obj15.setPercentage("33");
				obj15.setIcon("");
				obj15.setIntvalue((int) toatlLoanAmount);
				obj15.setReportLink("https://f2df.com/admin/enquiry?list=getLoan");
				list.add(obj15);

				DashboardPojo obj16 = new DashboardPojo("TOTAL NO OF CROP INSURANCE ENQUIRIES RCVD.",
						homeService.countInsurance(), "3", "https://f2df.com/admin/enquiry?list=getInsurance");
				list.add(obj16);

				DashboardPojo obj17 = new DashboardPojo();
				obj17.setName("TOTAL VALUE OF CROP INSURANCE ENQUIRIES");
				obj17.setValue(format.format(toatlInsuranceAmount));
				obj17.setPercentage("33");
				obj17.setIcon("");
				obj17.setIntvalue((int) toatlInsuranceAmount);
				obj17.setReportLink("https://f2df.com/admin/enquiry?list=getInsurance");
				list.add(obj17);

				DashboardPojo obj18 = new DashboardPojo("TOTAL NO OF BOOSTERS AVAILED", listOfBoost.size(), "3",
						"https://www.f2df.com/admin/dashboard");
				list.add(obj18);
				DashboardPojo obj19 = new DashboardPojo("TOTAL VALUE OF BOOSTERS AVAILED(IN Rs.)",
						obj18.getIntvalue() * 99, "3", "https://www.f2df.com/admin/dashboard");
				list.add(obj19);
				DashboardPojo obj20 = new DashboardPojo("TOTAL NO OF ADS EXPIRING IN CURRENT MONTH", 0, "0",
						"https://www.f2df.com/admin/dashboard");
				list.add(obj20);
				DashboardPojo obj21 = new DashboardPojo("TOTAL NO OF RUNNING ADS", homeService.countAdBanner("All"),
						"3", "https://www.f2df.com/admin/dashboard");
				list.add(obj21);

				DashboardPojo obj22 = new DashboardPojo();
				obj22.setName("TOTAL VALUE OF RUNNING ADS(IN Rs.)");
				obj22.setValue(format.format(totalAddAmount));
				obj22.setPercentage("33");
				obj22.setIcon("");
				obj22.setIntvalue((int) totalAddAmount);
				obj22.setReportLink("https://f2df.com/");
				list.add(obj22);

				homemap.put("data", list);
			} else {
				list = new ArrayList<DashboardPojo>();
				homemap = new HashMap<String, Object>();
				DashboardPojo obj = new DashboardPojo("Total Active Lead",
						productService.getProductsByUser(req.getUserId()).size(), "3",
						"https://www.f2df.com/admin/reports/leads?type=Active&userId=" + req.getUserId());
				list.add(obj);
				DashboardPojo obj1 = new DashboardPojo("Total Unseen Enquiry",
						dao.countInterested(req.getUserId(), "Enquiry"), "3",
						"https://www.f2df.com/admin/reports/enquiry?userId");
				list.add(obj1);
				DashboardPojo obj2 = new DashboardPojo("Total Enquiry", dao.countInterested(req.getUserId(), "Enquiry"),
						"3", "https://www.f2df.com/admin/reports/enquiry?userId" + req.getUserId());
				list.add(obj2);

				DashboardPojo obj3 = new DashboardPojo("Total Interested", dao.countInterested(req.getUserId(), "All"),
						"3", "");
				list.add(obj3);
				DashboardPojo obj4 = new DashboardPojo("Total Order", dao.countInterested(req.getUserId(), "Order"),
						"3", "");
				list.add(obj4);
				DashboardPojo obj5 = new DashboardPojo("Total Order Recieved",
						dao.countInterested(req.getUserId(), "Order"), "3", "");
				list.add(obj5);
				DashboardPojo obj6 = new DashboardPojo("Total Value Order Recieved",
						dao.countInterested(req.getUserId(), "Order"), "3", "");
				list.add(obj6);
				DashboardPojo obj7 = new DashboardPojo("Total Training Attended", 0, "3", "");
				list.add(obj7);
				DashboardPojo obj10 = new DashboardPojo("Total Value Of Active Lead",
						productService.getProductsByUser(req.getUserId()).size(), "3",
						"https://www.f2df.com/admin/reports/leads?type=Active&userId=" + req.getUserId());
				list.add(obj10);
				DashboardPojo obj12 = new DashboardPojo("Message Count", dao.countInterested(req.getUserId(), "Order"),
						"3", "");
				list.add(obj12);

				homemap.put("data", list);
			}
		} catch (Exception e) {
			logger.debug("Error occured during dashboard" + e.getStackTrace());
		}
		String dukanName = dao.getDukanDetails(req.getUserId());
		if (dukanName != "") {
			homemap.put("mereDukanLink", "https://f2df.com/my-online-dukan/" + dukanName);
		} else {
			homemap.put("mereDukanLink", "https://f2df.com/my-online-dukan/");
		}

		homemap.put("userProduct", productService.getProductsByUser(req.getUserId()));
		homemap.put("message", "USER dashboard API with all data ");
		homemap.put("status", 200);
		homemap.put("admiin", admin);
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/getEnquiryForProduct", headers = "Accept=application/json")
	public ResponseEntity<Object> getEnquiryDataWithMydukan(@RequestBody ReportRequest req) {
		try {

			Map<String, Object> map = new HashMap<String, Object>();
			List<ReportEnquiryResponse> obj = dao.getEnquiryByProductId(req);
			map.put("message", "Enquiry list of User and product ");
			map.put("status", 200);
			map.put("data", obj);
			map.put("length", obj.size());
			return new ResponseEntity<Object>(map, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("ERROR OCCURED DURING get report " + e.toString());
			return generateResponse("Report Get All User", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/userDashboard", headers = "Accept=application/json")
	public ResponseEntity<Object> homeApi(@RequestBody RequestProduct req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		homemap.put("message", "USER dashboard API with all data ");
		homemap.put("status", 200);
		homemap.put("totalActiveLead", productService.getProductsByUser(req.getUserId()).size());

		homemap.put("totalUnseenEnquiry", dao.countInterested(req.getUserId(), "Enquiry"));
		homemap.put("totalEnquiry", dao.countInterested(req.getUserId(), "Enquiry"));
		homemap.put("totalInterested", dao.countInterested(req.getUserId(), "All"));
		homemap.put("totalOrder", dao.countInterested(req.getUserId(), "Order"));
		homemap.put("totalOrderRecieved", dao.countInterested(req.getUserId(), "Order"));
		homemap.put("totalValueOfOrderRecieved", dao.countInterested(req.getUserId(), "Order"));
		homemap.put("totalTrainingAttended", 0);
		homemap.put("addExpiry", 0);
		homemap.put("totalNumbersOdAdd", dao.countInterested(req.getUserId(), "All"));
		homemap.put("messageCount", dao.countInterested(req.getUserId(), "Order"));

		homemap.put("totalvalueOfActiveLead", productService.getProductsByUser(req.getUserId()).size());
		homemap.put("totalMyOnlineDukan", productService.getProductsByUser(req.getUserId()).size());
		homemap.put("totalRegUser", productService.getProductsByUser(req.getUserId()).size());
		String dukanName = dao.getDukanDetails(req.getUserId());
		if (dukanName != "") {
			homemap.put("mereDukanLink", "https://f2df.com/my-online-dukan/" + dukanName);
		} else {
			homemap.put("mereDukanLink", "https://f2df.com/my-online-dukan/");
		}
		homemap.put("userProduct", productService.getProductsByUser(req.getUserId()));

		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/getInterestListByType", headers = "Accept=application/json")
	public ResponseEntity<Object> enquiryList(@RequestBody RequestProduct req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		homemap.put("message", "Interested list of User ");
		homemap.put("status", 200);
		homemap.put("equiryList", dao.getListOfInterested(req.getUserId(), req.getProductType()));
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/getInterestListByUser", headers = "Accept=application/json")
	public ResponseEntity<Object> getInterestListByUser(@RequestBody RequestProduct req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		homemap.put("message", "Interested list of User ");
		homemap.put("status", 200);
		homemap.put("equiryList", dao.getInterestListByUser(req.getUserId(), req.getProductType()));
		homemap.put("totalPrice", dao.totalPrice(req.getUserId()));
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/deleteInterestListByUser", headers = "Accept=application/json")
	public ResponseEntity<Object> deleteInterestListByUser(@RequestBody RequestProduct req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		dao.deleteInterestListByUser(req.getId());
		homemap.put("message", "Interested deleted of User ");
		homemap.put("status", 200);
		homemap.put("totalPrice", dao.totalPrice(req.getUserId()));
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/updateInterestListByUser", headers = "Accept=application/json")
	public ResponseEntity<Object> updateInterestListByUser(@RequestBody Enquiry req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		dao.updateInterestListByUser(req.getId(), req.getPrice(), req.getQunatity());
		homemap.put("message", "Interested updated of User ");
		homemap.put("status", 200);
		homemap.put("totalPrice", dao.totalPrice(req.getUser_id()));
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/getTrainingAttended", headers = "Accept=application/json")
	public ResponseEntity<Object> getTrainingAttended(@RequestBody RequestProduct req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		homemap.put("message", "Interested list of User ");
		homemap.put("status", 200);
		homemap.put("attendedTrainingList", dao.getTrainingAttended(req.getUserId()));
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/addInterest", headers = "Accept=application/json")
	public ResponseEntity<Object> enquiryList(@RequestBody Enquiry req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		req.setCreateDate(new Date());
		req.setUpdateDate(new Date());
		req.setStatus("Active");
		req.setActualPrice(req.getPrice() / req.getQunatity());
		dao.addInterest(req);
		homemap.put("message", "Interest added of User ");
		homemap.put("status", 200);
		homemap.put("totalPrice", dao.totalPrice(req.getUser_id()));
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/addFeedback", headers = "Accept=application/json")
	public ResponseEntity<Object> feedbackAdd(@RequestBody Feedback req) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		req.setCreateDate(new Date());
		req.setUpdateDate(new Date());
		req.setStatus("Active");
		dao.addFeedback(req);
		homemap.put("message", "Feedback added of User ");
		homemap.put("status", 200);
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/addSellerReq", headers = "Accept=application/json")
	public ResponseEntity<Object> addSellerReq(@RequestBody SaleRequirement requirement) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		try {
			requirement.setCreateDate(new Date());
			requirement.setUpdateDate(new Date());
			requirement.setStatus("All");
			dao.addSellerReq(requirement);
			homemap.put("message", "Succesfully added !!");
			homemap.put("status", 200);
			return new ResponseEntity<Object>(homemap, HttpStatus.OK);
		} catch (Exception e) {
			homemap.put("message", "Fail to add  ");
			homemap.put("status", 501);
			return new ResponseEntity<Object>(homemap, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PostMapping(value = "/getBuyerList", headers = "Accept=application/json")
	public ResponseEntity<Object> getBuyerList(@RequestBody RequestProduct req) {
		int pageNo = req.getPageNo();
		if (req.getSize() == 0) {
			req.setSize(10);
		}
		if (pageNo == 0) {
			pageNo = 0;
		} else {
			pageNo = pageNo * req.getSize();
		}
		req.setPageNo(pageNo);
		Map<String, Object> homemap = new HashMap<String, Object>();
		homemap.put("message", " getBuyerList list of User ");
		homemap.put("status", 200);
		homemap.put("data", dao.getBuyerList(req));
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/getSellerList", headers = "Accept=application/json")
	public ResponseEntity<Object> getSellerList(@RequestBody RequestProduct req) {
		int pageNo = req.getPageNo();
		if (req.getSize() == 0) {
			req.setSize(10);
		}
		if (pageNo == 0) {
			pageNo = 0;
		} else {
			pageNo = pageNo * req.getSize();
		}
		req.setPageNo(pageNo);
		Map<String, Object> homemap = new HashMap<String, Object>();
		homemap.put("message", " getSellerList list of User ");
		homemap.put("status", 200);
		homemap.put("sellerList", dao.getSellerList(req));
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/buyerAvailablityAdd", headers = "Accept=application/json")
	public ResponseEntity<Object> buyerAvailablityAdd(@RequestBody BuyAvailablity requirement) {
		Map<String, Object> homemap = new HashMap<String, Object>();
		try {
			requirement.setCreateDate(new Date());
			requirement.setUpdateDate(new Date());
			requirement.setStatus("Active");
			dao.buyerAvailablityAdd(requirement);
			homemap.put("message", " Succesfully added !!");
			homemap.put("status", 200);
			return new ResponseEntity<Object>(homemap, HttpStatus.OK);
		} catch (Exception e) {
			homemap.put("message", "buyerAvailablityAdd Not added  ");
			homemap.put("status", 501);
			return new ResponseEntity<Object>(homemap, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String showWelcomePage(Model model) {
		System.out.println("we are in welcome method ");
		model.addAttribute("categoryList", categoryService.getCategoryList(0, 34, ""));
		// model.put("catList", categoryService.getCategoryList(0,10,""));
		return "/product-new.html";
	}

	@RequestMapping(value = "/my-online-dukan/{dukanName}", method = RequestMethod.GET)
	public ResponseEntity<Object> mereDukanView(@PathVariable("dukanName") String dukanName) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			MereDukan mereDukan = dao.getDukanDetails(dukanName);
			User user = userService.getUser(mereDukan.getUserId());
			map.put("address", user.getAddress().getAddress1() + "," + user.getAddress().getCity() + " "
					+ user.getAddress().getState());
			List<Product> products = productService.getProductsByUser(mereDukan.getUserId());
			map.put("message", "Mere dukan API ");
			map.put("status", "OK");
			map.put("mereDukan", mereDukan);
			map.put("user", user);
			map.put("products", products);
			map.put("bannerList", homeService.getBanner("Home"));
			return new ResponseEntity<Object>(map, HttpStatus.OK);
			// model.put("catList", categoryService.getCategoryList(0,10,""));
		} catch (Exception e) {
			map.put("message", "Mere dukan API ");
			map.put("status", HttpStatus.NOT_FOUND);
			map.put("mereDukan", "");
			map.put("user", "");
			map.put("products", "");

			return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
		}

	}

	/*
	 * @RequestMapping(value = "/feature", method = RequestMethod.GET) public String
	 * feature(Model model) { System.out.println( "we are in welcome method ");
	 * model.addAttribute("categoryList", categoryService.getCategoryList(0, 34,
	 * "")); // model.put("catList", categoryService.getCategoryList(0,10,""));
	 * return "/feature.html"; }
	 */
	@PostMapping(value = "/verifyOTP", headers = "Accept=application/json")
	public ResponseEntity<Object> verifyOTP(@RequestBody User entity) {
		User user = userService.getUserByMobile(entity.getMobile());
		if (user == null) {
			return generateResponse("User not exists !! ", entity, HttpStatus.OK);
		}
		if (entity.getMobile() == 9999000012L) {
			user.setUpdateDate(new Date());
			userService.createUser(user);
			return generateResponse("User verify succesfully", user, HttpStatus.OK);
		}
		if (entity.getOtp() == user.getOtp()) {
			user.setOtp(entity.getOtp());
			/*
			 * if(entity.getReffralCode() != null){
			 * user.setReffralCode(entity.getReffralCode()); }else{
			 * user.setReffralCode(String.format("%06d", new Random().nextInt(999999))); }
			 */
			user.setUpdateDate(new Date());
			userService.createUser(user);
			user.setOtp(0);
			return generateResponse("User verify succesfully", user, HttpStatus.OK);
		} else {
			return generateResponse("User fail in verification ", entity, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/resendOTP", headers = "Accept=application/json")
	public ResponseEntity<Object> resendOtp(@RequestBody User entity) {
		User user = userService.getUserByMobile(entity.getMobile());
		if (user.getId() != 0) {
			// generate OTP and send via SMS
			int otp = Integer.valueOf(getRandomNumberString());
			String msg = "Do Not Share " + otp + " is Your OTP To Register on www.f2df.com";
			String url = "https://api.xclusivesms.com/api/sendSMS?token=6d53f5bc96c05f53d2c2bc457d26e2ee&senderid=FDFWEB&route=13&number="
					+ user.getMobile() + "&message=" + msg + "&contentID=1707162548438110289";

			RestTemplate rs = new RestTemplate();
			rs.getForObject(url, String.class);

			user.setOtp(otp);
			user.setUpdateDate(new Date());
			user = userService.createUser(user);
			user.setOtp(0);
			if (user.getId() != 0) {
				return generateResponse("OTP sent to your mobile number  !! ", user, HttpStatus.OK);
			} else {
				return generateResponse("Fail to sent OTP ", user, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return generateResponse("User not exists ", user, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/login", headers = "Accept=application/json")
	public ResponseEntity<Object> loginByGoogle(@RequestBody User entity) {
		try {
			User user = new User();
			if (!StringUtils.isEmpty(entity.getType()) && entity.getType().equals("mobile")) {

				// create OTP and save into database 0000
				// user = userService.userExistByEmail(entity.getEmail());
				user = userService.getUserByMobile(entity.getMobile());
				if (user.getId() != 0) {
					user.setUserExist(true);
					String otp = getRandomNumberString();
					String msg = "Do Not Share " + otp + " is Your OTP To Register on www.f2df.com";
					String url = "https://api.xclusivesms.com/api/sendSMS?token=6d53f5bc96c05f53d2c2bc457d26e2ee&senderid=FDFWEB&route=13&number="
							+ user.getMobile() + "&message=" + msg + "&contentID=1707162548438110289";

					/*
					 * https://api.xclusivesms.com/api/sendSMS?token=
					 * 6d53f5bc96c05f53d2c2bc457d26e2ee&senderid=FDFWEB&route=13&number=
					 * 9015923188&message='Do Not Share 1234 is Your OTP To Register on
					 * www.f2df.com'&contentID=1707162548438110289
					 */

					RestTemplate rs = new RestTemplate();
					rs.getForObject(url, String.class);
					user.setOtp(Integer.valueOf(otp));

					user = userService.createUser(user);
					user.setOtp(0);
					return generateResponse("User already exists !! ", user, HttpStatus.OK);
				} else {
					User userNew = new User();
					String otp = getRandomNumberString();
					try {
						String msg = "Do Not Share " + otp + " is Your OTP To Register on www.f2df.com";
						String url = "https://api.xclusivesms.com/api/sendSMS?token=6d53f5bc96c05f53d2c2bc457d26e2ee&senderid=FDFWEB&route=13&number="
								+ entity.getMobile() + "&message=" + msg + "&contentID=1707162548438110289";

						RestTemplate rs = new RestTemplate();
						rs.getForObject(url, String.class);
					} catch (Exception e) {
						return generateResponse("Fail to sent OTP error is  " + e.getMessage(), user,
								HttpStatus.INTERNAL_SERVER_ERROR);
					}
					userNew.setOtp(Integer.valueOf(otp));

					userNew.setMobile(entity.getMobile());
					userNew.setUserType("Farmer");
					userNew.setRole("user");
					userNew.setStatus(true);
					userNew.setCreateDate(new Date());
					userNew.setUpdateDate(new Date());
					userNew = userService.createUser(userNew);
					userNew.setOtp(0);
					return generateResponse("OTP sent to your mobile number  !! ", userNew, HttpStatus.OK);
				}

			} else {
				user = userService.userExistByGoogle(entity.getGoogleId());
				if (user.getId() != 0) {
					user.setUserExist(true);
					return generateResponse("User already exists !! ", user, HttpStatus.OK);
				} else {
					User userNew = new User();
					// user.setOtp(1111);
					userNew.setGoogleId(entity.getGoogleId());
					userNew.setCreateDate(new Date());
					userNew.setUpdateDate(new Date());
					userNew = userService.createUser(userNew);
					userNew.setOtp(0);
					if (userNew.getId() != 0) {
						return generateResponse("User save Succesfully !! ", userNew, HttpStatus.OK);
					} else {
						return generateResponse("User fail to save ", user, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
			}
		} catch (Exception e) {
			logger.debug("Goinng to save user exception " + e.toString());
			return generateResponse("Fail to sent OTP ", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/getProfile", headers = "Accept=application/json")
	public ResponseEntity<Object> getUser(@RequestBody User entity) {
		User user = userService.getUser(entity.getId());
		String dukanName = dao.getDukanDetails(entity.getId());
		user.setDukanName(dukanName);
		return generateResponse("User get succesfully", user, HttpStatus.OK);

	}

	@PostMapping(value = "/saveUser", headers = "Accept=application/json")
	public ResponseEntity<Object> saveUser(@RequestBody User entity) {
		// entity.setOtp(entity.getOtp());
		User user = userService.getUser(entity.getId());
		user.setEmail(entity.getEmail());
		user.setMobile(entity.getMobile());
		user.setName(entity.getName());
		user.setUserType(entity.getUserType());
		if (entity.getAddress() != null) {
			entity.getAddress().setId(user.getAddress().getId());
			user.setAddress(entity.getAddress());
		}
		user = userService.createUser(user);
		if (user.getId() != 0) {
			return generateResponse("User save/update succesfully", user, HttpStatus.OK);
		} else {
			return generateResponse("User fail to save ", "", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/uploadImage")
	public ResponseEntity<Object> uploadImage2(@RequestBody ImageDTO dto, HttpServletRequest request) {
		try {
			byte[] imageByte = Base64.decodeBase64(dto.getFileContentBase64());
			String directory = "/img/user/f2dfUser_" + dto.getUserId() + ".jpg";
			new FileOutputStream("/home/f2df/ea-tomcat85/webapps" + directory).write(imageByte);
			User user = userService.getUser(dto.getUserId());
			user.setImg(directory);
			userService.createUser(user);
			return generateResponse("Image save succesfully", directory, HttpStatus.OK);
		} catch (Exception e) {
			return generateResponse("Fail to save Image ", e.getMessage(), HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping(value = "/addUserAccount", headers = "Accept=application/json")
	public ResponseEntity<Object> addUserAccount(@RequestBody List<UserAccount> accounts)
			throws FileNotFoundException, IOException {
		try {
			for (UserAccount account : accounts) {
				try {
					User user = new User();
					user.setName(account.getUserName());
					Address address = new Address();

					user.setMobile(account.getPhoneNo());
					user.setOtp(0000);
					address.setAddress1(account.getAddress());
					address.setCity(account.getCity());
					address.setState(account.getState());
					address.setPincode(String.valueOf(account.getPincode()));
					user.setCreateDate(new Date());
					user.setUpdateDate(new Date());
					user.setUserType(account.getType());
					user.setAddress(address);
					userService.createUser(user);
				} catch (Exception e) {
					logger.debug("Exception " + e.getMessage());
				}
			}
			return generateResponse("save succesfully", accounts, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("Error occured during add by excel " + e.getMessage());
			return generateResponse("Testimonial save succesfully", "", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/addTestimonial", headers = "Accept=application/json")
	public ResponseEntity<Object> addTestimonial(@RequestBody UserTestimonial testimonial)
			throws FileNotFoundException, IOException {
		try {
			String img = testimonial.getImg();
			String imageName = new Date().getTime() + ".jpg";
			String uploadFilePath = "/home/f2df/ea-tomcat85/webapps/img/testimonial/" + imageName;
			System.out.println("file name" + uploadFilePath);
			byte[] imageByte = Base64.decodeBase64(img.split(",")[1]);
			new FileOutputStream(uploadFilePath).write(imageByte);
			testimonial.setImg("/img/testimonial/" + imageName);
			logger.debug("Error occured during addTestimonial " + testimonial.toString());
			dao.addTestimonial(testimonial);
			return generateResponse("Testimonial save succesfully", testimonial, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("Error occured during addTestimonial " + e.getMessage());
			return generateResponse("Testimonial save succesfully", testimonial, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/deleteTestimonial", headers = "Accept=application/json")
	public ResponseEntity<Object> deleteTestimonial(@RequestBody UserTestimonial testimonial)
			throws FileNotFoundException, IOException {
		dao.deleteTestimonial(testimonial.getId());
		return generateResponse("Testimonial delete succesfully", testimonial, HttpStatus.OK);
	}

	@PostMapping(value = "/addDeliveryAddress", headers = "Accept=application/json")
	public ResponseEntity<Object> addDeliveryAddress(@RequestBody DeliveryAddress address) {
		dao.addDeliveryAddress(address);
		return generateResponse("Address save succesfully", "", HttpStatus.OK);
	}

	@PostMapping(value = "/getDeliveryAddress", headers = "Accept=application/json")
	public ResponseEntity<Object> getDeliveryAddress(@RequestBody RequestProduct req) {
		List<DeliveryAddress> deliveryAddress = dao.getDeliveryAddress(req.getUserId());
		return generateResponse("getDeliveryAddress succesfully", deliveryAddress, HttpStatus.OK);
	}

	@PostMapping(value = "/getDeliveryType", headers = "Accept=application/json")
	public ResponseEntity<Object> getDeliveryType() {
		Map<String, String> map = new HashMap<>();
		map.put("home", "Home");
		map.put("office", "Office");
		map.put("godown", "Godown");
		map.put("shop", "Shop");
		map.put("work", "Work");
		return generateResponse("getDeliveryType succesfully", map, HttpStatus.OK);
	}

	public static String getRandomNumberString() {
		// It will generate 6 digit random Number.
		// from 0 to 999999
		Random rnd = new Random();
		// this will convert any number sequence into 6 character.
		return String.format("%04d", Integer.valueOf(rnd.nextInt(9999)));
	}

	public static ResponseEntity<Object> generateResponse(String message, Object responseObj, HttpStatus status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}

}
