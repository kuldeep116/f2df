package com.springboot.ecommerce.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.ecommerce.model.Banner;
import com.springboot.ecommerce.pojo.LeadBussDoneReportRes;
import com.springboot.ecommerce.pojo.LeadReportResponse;
import com.springboot.ecommerce.pojo.MereDukanReportRes;
import com.springboot.ecommerce.pojo.ReportEnquiryResponse;
import com.springboot.ecommerce.pojo.ReportLeadResponse;
import com.springboot.ecommerce.pojo.ReportRequest;
import com.springboot.ecommerce.pojo.ReportUserResponse;
import com.springboot.ecommerce.pojo.UserReportResponse;
import com.springboot.ecommerce.repository.CommonDao;
import com.springboot.ecommerce.service.HomeService;
import com.springboot.ecommerce.service.ProductService;
import com.springboot.ecommerce.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/report")
@Tag(name = "Report API")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {

	@Autowired
	CommonDao dao;

	@Autowired
	UserService userService;
	
	@Autowired
	HomeService homeService;

	@Autowired
	ProductService productService;

	private static Logger logger = Logger.getLogger(ReportController.class.getName());

	@PostMapping(value = "/getAllUserReport", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllUser(@RequestBody ReportRequest req) {
		try {
			int pageNo = req.getPageNo();
			if (pageNo == 0) {
				pageNo = 0;
			} else {
				pageNo = pageNo * req.getSize();
			}
			req.setPageNo(pageNo);
			Map<String, Object> map = new HashMap<String, Object>();
			List<ReportUserResponse> obj = dao.getAllUser(req);
			map.put("message", "Interested list of User ");
			map.put("status", 200);
			map.put("data", obj);
			map.put("length", userService.countOfTotalUser());
			return new ResponseEntity<Object>(map, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("ERROR OCCURED DURING get report " + e.toString());
			return generateResponse("Report Get All User", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping(value = "/getAdsReport", headers = "Accept=application/json")
	public ResponseEntity<Object> getAdsReport(@RequestBody ReportRequest req) {
		try {
			int pageNo = req.getPageNo();
			if (pageNo == 0) {
				pageNo = 0;
			} else {
				pageNo = pageNo * req.getSize();
			}
			req.setPageNo(pageNo);
			Map<String, Object> map = new HashMap<String, Object>();
			List<Banner> obj = (List<Banner>) homeService.getBanner("All");
			map.put("message", "banner list of User ");
			map.put("status", 200);
			map.put("data", obj);
			map.put("length", userService.countOfTotalUser());
			return new ResponseEntity<Object>(map, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("ERROR OCCURED DURING get banner report " + e.toString());
			return generateResponse("Report Get All banner list ", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public static void main(String[] args) throws IOException {
		InputStream fis = new FileInputStream("C:/opt/Kamal/totalleadspostedandvalueofpostedleads.json");
		// String staticDataString = IOUtils.toString(fis,
		// StandardCharsets.UTF_8);
		// create JsonReader object
		// System.out.println(new
		// org.json.JSONObject(staticDataString).toMap());
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<LeadReportResponse>> typeReference = new TypeReference<List<LeadReportResponse>>() {
		};
		List<LeadReportResponse> users = mapper.readValue(fis, typeReference);
		System.out.println(users.toString());
	}

	@PostMapping(value = "/getUserReport", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllOldUser(@RequestBody ReportRequest req) {
		try {
			int pageNo = req.getPageNo();
			if (pageNo == 0) {
				pageNo = 0;
			} else {
				pageNo = pageNo * req.getSize();
			}
			List<UserReportResponse> users = new ArrayList<UserReportResponse>();
			InputStream fis = new FileInputStream("/home/f2df/public_html/reports/Registereduserandvisitors.json");
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<UserReportResponse>> typeReference = new TypeReference<List<UserReportResponse>>() {
			};
			List<UserReportResponse> objList = mapper.readValue(fis, typeReference);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("message", "Old report user");
			map.put("status", 200);
			map.put("data", objList);
			map.put("length", 7219);
			return new ResponseEntity<Object>(map, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("ERROR OCCURED DURING get report " + e.toString());
			return generateResponse("Report Get All User", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/getLeadData", headers = "Accept=application/json")
	public ResponseEntity<Object> getDataWithMydukan(@RequestBody ReportRequest req) {
		try {
			int pageNo = req.getPageNo();
			if (pageNo == 0) {
				pageNo = 0;
			} else {
				pageNo = pageNo * req.getSize();
			}
			req.setPageNo(pageNo);
			Map<String, Object> map = new HashMap<String, Object>();

			List<ReportLeadResponse> obj = dao.getLeadDataWithDukanName(req);
			map.put("message", "Report getLeadData ");
			map.put("status", 200);
			map.put("data", obj);
			map.put("length", productService.productCount());
			return new ResponseEntity<Object>(map, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("ERROR OCCURED DURING get report " + e.toString());
			return generateResponse("Report Get All User leads", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/getAllOldLead", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllOldLead(@RequestBody ReportRequest req) {
		try {
			int pageNo = req.getPageNo();
			if (pageNo == 0) {
				pageNo = 0;
			} else {
				pageNo = pageNo * req.getSize();
			}
			InputStream fis = new FileInputStream(
					"/home/f2df/public_html/reports/totalleadspostedandvalueofpostedleads.json");
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<LeadReportResponse>> typeReference = new TypeReference<List<LeadReportResponse>>() {
			};
			List<LeadReportResponse> list = mapper.readValue(fis, typeReference);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("message", "Old report lead all posted");
			map.put("status", 200);
			map.put("data", list);
			map.put("length", 3247);
			return new ResponseEntity<Object>(map, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("ERROR OCCURED DURING get report " + e.toString());
			return generateResponse("Report Get All User", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/getAllMereDukanOld", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllMereDukanOld(@RequestBody ReportRequest req) {
		try {
			int pageNo = req.getPageNo();
			if (pageNo == 0) {
				pageNo = 0;
			} else {
				pageNo = pageNo * req.getSize();
			}
			InputStream fis = new FileInputStream("/home/f2df/public_html/reports/TotalMeriOnlinedukanCreated.json");
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<MereDukanReportRes>> typeReference = new TypeReference<List<MereDukanReportRes>>() {
			};
			List<MereDukanReportRes> list = mapper.readValue(fis, typeReference);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("message", "Old report mere dukan");
			map.put("status", 200);
			map.put("data", list);
			map.put("length", 336);
			return new ResponseEntity<Object>(map, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("ERROR OCCURED DURING get report " + e.toString());
			return generateResponse("Report Get All User", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/getAllOldBussDoneLead", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllOldBussDoneLead(@RequestBody ReportRequest req) {
		try {
			int pageNo = req.getPageNo();
			if (pageNo == 0) {
				pageNo = 0;
			} else {
				pageNo = pageNo * req.getSize();
			}
			InputStream fis = new FileInputStream("/home/f2df/public_html/reports/totalbusinessdoneleadsandvalue.json");
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<LeadBussDoneReportRes>> typeReference = new TypeReference<List<LeadBussDoneReportRes>>() {
			};
			List<LeadBussDoneReportRes> list = mapper.readValue(fis, typeReference);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("message", "Old report user");
			map.put("status", 200);
			map.put("data", list);
			map.put("length", 1527);
			return new ResponseEntity<Object>(map, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("ERROR OCCURED DURING get report " + e.toString());
			return generateResponse("Report Get All User", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/getEnquiryData", headers = "Accept=application/json")
	public ResponseEntity<Object> getEnquiryDataWithMydukan(@RequestBody ReportRequest req) {
		try {
			int pageNo = req.getPageNo();
			if (pageNo == 0) {
				pageNo = 0;
			} else {
				pageNo = pageNo * req.getSize();
			}
			req.setPageNo(pageNo);
			Map<String, Object> map = new HashMap<String, Object>();
			List<ReportEnquiryResponse> obj = dao.getEnquiryForProducts(req);
			map.put("message", "Enquiry list of User for his product  ");
			map.put("status", 200);
			map.put("data", obj);
			map.put("length", obj.size());
			return new ResponseEntity<Object>(map, HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("ERROR OCCURED DURING get report " + e.toString());
			return generateResponse("Report Get All User", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public static ResponseEntity<Object> generateResponse(String message, Object responseObj, HttpStatus status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}

	public static boolean imgExisting(String path) {
		File f = new File(path);
		boolean istrue = false;
		if (f.exists()) {
			istrue = true;
		}
		return istrue;
	}

}
