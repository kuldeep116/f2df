package com.springboot.ecommerce.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.ecommerce.model.Events;
import com.springboot.ecommerce.model.Images;
import com.springboot.ecommerce.pojo.RequestProduct;
import com.springboot.ecommerce.repository.CommonDao;
import com.springboot.ecommerce.repository.ProductDaoImpl;
import com.springboot.ecommerce.service.HomeService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/event")
@Tag(name = "Events API")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EventsController {

	@Autowired
	CommonDao dao;

	@Autowired
	HomeService homeService;

	private static Logger logger = Logger.getLogger(EventsController.class.getName());
	@PostMapping(value = "/add", headers = "Accept=application/json")
	public ResponseEntity<Object> createEvents(@RequestBody Events req) {
		List<Images> imagesObj;
		try { 
			imagesObj = saveUploadedFiles(req.getMultiImages());
			req.setImages(imagesObj);
			dao.add(req);
			return generateResponse("Events save succesfully", "OK", HttpStatus.OK);
		} catch (IOException e) {
			logger.debug("Error occured i  add events" +e.toString());
			return generateResponse("Events fail to save ", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/getEvents", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getNews(@RequestBody Events req) {
		Events obj = dao.getEventsById(req.getId());
		if (obj == null) {
			return generateResponse(" Events by id ", "", HttpStatus.NOT_FOUND);
		}
		return generateResponse("Events by id ", obj, HttpStatus.OK);
	}
	@PostMapping(value = "/getSuccessfulEvents", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSuccessfulEvents(@RequestBody RequestProduct req) {
		int pageNo = req.getPageNo();
		if (pageNo == 0) {
			pageNo = 0;
		} else {
			pageNo = pageNo * req.getSize();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "Home API getUpcomingEvents with all data ");
		map.put("status", 200);
		map.put("upcomingEvents", dao.getAllEvents(req.getPageNo(), req.getSize(),"upcoming"));
		map.put("pastEvents", dao.getAllEvents(req.getPageNo(), req.getSize(),"succesfull"));
		map.put("eventsCount", dao.getAllEventsCount());
		map.put("bannerList", homeService.getBanner("Events"));
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}
	@PostMapping(value = "/getUpcomingEvents", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getUpcomingEvents(@RequestBody RequestProduct req) {
		int pageNo = req.getPageNo();
		if (pageNo == 0) {
			pageNo = 0;
		} else {
			pageNo = pageNo * req.getSize();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "Home API getUpcomingEvents with all data ");
		map.put("status", 200);
		map.put("upcomingEvents", dao.getAllEvents(req.getPageNo(), req.getSize(),"upcoming"));
		map.put("pastEvents", dao.getAllEvents(req.getPageNo(), req.getSize(),"succesfull"));
		map.put("eventsCount", dao.getAllEventsCount());
		map.put("bannerList", homeService.getBanner("Events"));
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}
	@PostMapping(value = "/getAllEvents", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllEvents(@RequestBody RequestProduct req) {
		int pageNo = req.getPageNo();
		if (pageNo == 0) {
			pageNo = 0;
		} else {
			pageNo = pageNo * req.getSize();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "Home API Events with all data ");
		map.put("status", 200);
		map.put("upcomingEvents", dao.getAllEvents(req.getPageNo(), req.getSize(),"upcoming"));
		map.put("pastEvents", dao.getAllEvents(req.getPageNo(), req.getSize(),"succesfull"));
		map.put("eventsCount", dao.getAllEventsCount());
		map.put("bannerList", homeService.getBanner("Events"));
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}
	@PostMapping(value = "/deleteEvent", headers = "Accept=application/json")
	public ResponseEntity<Object> deleteNews(@RequestBody Events req) {
		//featureService.removeSubCategoryFeatureById(id);
		dao.deleteEvent(req.getId());
		return generateResponse("Event delete successfully", "", HttpStatus.OK);
	}
	
	public static ResponseEntity<Object> generateResponse(String message, Object responseObj, HttpStatus status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}

	// Save Files
	private List<Images> saveUploadedFiles(String[] files) throws IOException {
		List<Images> images = new ArrayList<>();
		for (String file : files) {
			Images imgObj = new Images();
			if (file.isEmpty()) {
				break;
			}
			String imageName ="events_"+new Date().getTime()+".jpg";
			byte[] imageByte = Base64.decodeBase64(file.split(",")[1]);
			new FileOutputStream("/home/f2df/ea-tomcat85/webapps/img/Events/"+imageName).write(imageByte);
			
			imgObj.setFilePath("/img/Events/"+imageName);
			imgObj.setOriginalFileName(imageName);
			images.add(imgObj);
		}
		return images;
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
