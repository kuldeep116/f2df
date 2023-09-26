package com.springboot.ecommerce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.ecommerce.model.AppVersion;
import com.springboot.ecommerce.model.ContactUs;
import com.springboot.ecommerce.model.Insurance;
import com.springboot.ecommerce.model.Loan;
import com.springboot.ecommerce.model.ProductCategory;
import com.springboot.ecommerce.model.Training;
import com.springboot.ecommerce.model.User;
import com.springboot.ecommerce.pojo.CatExportQunatity;
import com.springboot.ecommerce.pojo.RequestProduct;
import com.springboot.ecommerce.pojo.SubCatQunatity;
import com.springboot.ecommerce.repository.CommonDao;
import com.springboot.ecommerce.service.HomeService;
import com.springboot.ecommerce.service.ProductCategoryService;
import com.springboot.ecommerce.service.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/home")
@Tag(name = "Home API")
public class HomeController {

	@Autowired
	HomeService homeService;

	@Autowired
	CommonDao dao;

	@Autowired
	ProductCategoryService categoryService;

	@Autowired
	ProductService productService;
	private static Logger logger = Logger.getLogger(HomeController.class.getName());

	@GetMapping("/")
	public String reg(Map<String, Object> model) {
		model.put("user", new User());
		return "index";
	}

	public static Map<String, Object> homemap = new HashMap<String, Object>();

	public static List<ProductCategory> rentCategrory = new ArrayList<ProductCategory>();
	public static List<ProductCategory> AllCat = new ArrayList<ProductCategory>();

	@PostMapping(value = "/homeApi", headers = "Accept=application/json")
	public ResponseEntity<Object> homeApi() {
		// if(homemap != null){
		homemap.put("message", "Home API with all data ");
		homemap.put("status", 200);
		homemap.put("categoryList", categoryService.getCategoryList(0, 6, "Sell"));
		homemap.put("recommdedProducts", productService.getProductList(0, 6, "recommded"));
		homemap.put("bestSellingProducts", productService.getProductList(0, 6, "bestSeller"));
		homemap.put("allProducts", productService.getProductList(0, 6, "All"));
		homemap.put("bannerList", homeService.getBanner("Home"));
		homemap.put("addList", homeService.getAddSpace());
		// }
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/homeWebApi", headers = "Accept=application/json")
	public ResponseEntity<Object> homeWebApi() {
		// if(homemap != null){
		homemap.put("message", "Home API with all data ");
		homemap.put("status", 200);
		homemap.put("categoryList", categoryService.getCategoryList(0, 6, "Sell"));
		homemap.put("recommdedProducts", productService.getProductList(0, 8, "recommded"));
		homemap.put("bestSellingProducts", productService.getProductList(0, 8, "bestSeller"));
		homemap.put("allProducts", productService.getProductList(0, 30, "All"));
		homemap.put("bannerList", homeService.getBanner("Home"));
		homemap.put("addList", homeService.getAddSpace());
		// }
		return new ResponseEntity<Object>(homemap, HttpStatus.OK);
	}

	@PostMapping(value = "/testimonial", headers = "Accept=application/json")
	public ResponseEntity<Object> testimonial() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "Home API testimonials with all data ");
		map.put("status", 200);
		map.put("testimonial", homeService.getTestimonials());
		map.put("bannerList", homeService.getBanner("Home"));

		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}

	@PostMapping(value = "/forceFullyUpdate", headers = "Accept=application/json")
	public boolean forceFullyVersion(@RequestBody RequestProduct req) {
		boolean version = false;
		logger.debug("version from app " + req.getId());
		AppVersion ver = dao.getVersion(); // 44
		logger.debug("version from db " + ver.getVersion());
		if (req.getId() > ver.getVersion()) {
			version = true;
		}
		return version;
	}

	@PostMapping(value = "/getAllCategory", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllCategory(@RequestBody RequestProduct req) {
		if (req.getProductType().equals("All")) {
			//if (AllCat.size() == 0) {
				AllCat = categoryService.getCategoryList(0, 33, "All");
		//	}
			return generateResponse("Home API for getAllCategory", AllCat, HttpStatus.OK);
		} else {
		//	if (rentCategrory.size() == 0) {
				rentCategrory = categoryService.getCategoryList(0, 34, req.getProductType());
		//	}
			return generateResponse("Home API for getAllCategory", rentCategrory, HttpStatus.OK);
		}
	}

	@PostMapping(value = "/navigation", headers = "Accept=application/json")
	public ResponseEntity<Object> navigation() {
		return generateResponse("Home API for navigation", homeService.getNavigation(), HttpStatus.OK);
	}

	@PostMapping(value = "/trainingType", headers = "Accept=application/json")
	public ResponseEntity<Object> trainingType() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "Home API with all data ");
		map.put("status", 200);
		map.put("trainingVenue", homeService.getTrainingType());
		map.put("trainingType", homeService.getTrainingType());
		map.put("mode", homeService.getTrainingType());
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}

	@PostMapping(value = "/trainingVenue", headers = "Accept=application/json")
	public ResponseEntity<Object> trainingVenue() {
		return generateResponse("Home API for navigation", homeService.getTrainingType(), HttpStatus.OK);
	}
	/*
	 * @PostMapping(value = "/saveTraining", headers = "Accept=application/json")
	 * public ResponseEntity<Object> save(@RequestBody Training training) {
	 * homeService.createTraining(training);
	 * 
	 * Map<String, Object> map = new HashMap<String, Object>(); map.put("message",
	 * "Training save succesfully"); map.put("status", HttpStatus.OK);
	 * map.put("data", "success");
	 * 
	 * return map; }
	 */

	@PostMapping(value = "/homeBanner", headers = "Accept=application/json")
	public ResponseEntity<Object> homeBanner() {
		return generateResponse("Home API for navigation", homeService.getBanner("Home"), HttpStatus.OK);
	}

	@PostMapping(value = "/getExportQuantity", headers = "Accept=application/json")
	public ResponseEntity<Object> getExportQuantity() {
		List<CatExportQunatity> catList = homeService.exportSubCatQuantity();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "Home API for Export cat/Sub Cat Quantity");
		map.put("status", 200);
		map.put("cat", catList);
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}

	@PostMapping(value = "/homeAddSpace", headers = "Accept=application/json")
	public ResponseEntity<Object> homeAddSpace() {
		return generateResponse("Home API for navigation", homeService.getAddSpace(), HttpStatus.OK);
	}

	@PostMapping(value = "/saveTraining", headers = "Accept=application/json")
	public ResponseEntity<Object> saveTraining(@RequestBody Training training) {

		int id = homeService.createTraining(training);
		if (id != 0) {
			return generateResponse("Training save succesfully", id, HttpStatus.OK);
		} else {
			return generateResponse("Training fail to save ", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/getTraining", headers = "Accept=application/json")
	public ResponseEntity<Object> getTraining(@RequestBody RequestProduct req) {
		List<Training> list = homeService.getTraining(req.getPage(), req.getSize());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "List  ");
		map.put("status", 200);
		map.put("data", list);
		map.put("length", homeService.countTraining());
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}

	@PostMapping(value = "/getInsurance", headers = "Accept=application/json")
	public ResponseEntity<Object> getInsurance(@RequestBody RequestProduct req) {
		int pageNo = req.getPageNo();
		if (pageNo == 0) {
			pageNo = 0;
		} else {
			pageNo = pageNo * req.getSize();
		}
		req.setPage(pageNo);
		List<Insurance> list = homeService.getInsurance(req.getPage(), req.getSize());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "List  ");
		map.put("status", 200);
		map.put("data", list);
		map.put("length", homeService.countInsurance());
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}

	@PostMapping(value = "/getLoan", headers = "Accept=application/json")
	public ResponseEntity<Object> getLoan(@RequestBody RequestProduct req) {
		int pageNo = req.getPageNo();
		if (pageNo == 0) {
			pageNo = 0;
		} else {
			pageNo = pageNo * req.getSize();
		}
		req.setPage(pageNo);
		List<Loan> list = homeService.getLoan(req.getPage(), req.getSize());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "List  ");
		map.put("status", 200);
		map.put("data", list);
		map.put("length", homeService.countLoan());
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}

	@PostMapping(value = "/saveInsurance", headers = "Accept=application/json")
	public ResponseEntity<Object> saveInsurance(@RequestBody Insurance insurance) {

		int id = homeService.createInsurance(insurance);
		if (id != 0) {
			return generateResponse("Insurance  Enquiry submitted succesfully", id, HttpStatus.OK);
		} else {
			return generateResponse("Insurance fail to save ", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/saveLoan", headers = "Accept=application/json")
	public ResponseEntity<Object> saveLoan(@RequestBody Loan entity) {

		int id = homeService.createLoan(entity);
		if (id != 0) {
			return generateResponse("Loan Enquiry submitted succesfully", id, HttpStatus.OK);
		} else {
			return generateResponse("Loan fail to save ", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/saveContactUsReq", headers = "Accept=application/json")
	public ResponseEntity<Object> saveContactUsReq(@RequestBody ContactUs entity) {

		int id = homeService.createContactUs(entity);
		if (id != 0) {
			return generateResponse("Enquiry submitted succesfully", id, HttpStatus.OK);
		} else {
			return generateResponse("fail to save ", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	public static ResponseEntity<Object> generateResponse(String message, Object responseObj, HttpStatus status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}

}
