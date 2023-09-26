package com.springboot.ecommerce.controller;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.ecommerce.model.Banner;
import com.springboot.ecommerce.repository.CommonDaoImpl;
import com.springboot.ecommerce.service.HomeService;
import com.springboot.ecommerce.service.ProductCategoryService;
import com.springboot.ecommerce.service.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/banner")
@Tag(name = "Banner API")
public class BannerController {

	@Autowired
	HomeService homeService;

	@Autowired
	ProductCategoryService categoryService;

	@Autowired
	ProductService productService;

	private static Logger logger = Logger.getLogger(BannerController.class.getName());
	
	@PostMapping(value = "/getAllBanners", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllBanners() {
		return generateResponse("Home API for navigation", homeService.getBanner("All"), HttpStatus.OK);
	}
	@PostMapping(value = "/getAllBannersByType", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllBannersByType(@RequestBody Banner req) {
		return generateResponse("Home API for navigation", homeService.getBanner(req.getType()), HttpStatus.OK);
	}

	@PostMapping(value = "/deleteBanner", headers = "Accept=application/json")
	public ResponseEntity<Object> deleteBanner(@RequestBody Banner req) {
		homeService.deleteBanner(req.getId());
		return generateResponse("Home API for navigation", "", HttpStatus.OK);
	}

	@PostMapping(value = "/bannerLocationList", headers = "Accept=application/json")
	public ResponseEntity<Object> bannerLocationList() {
		return generateResponse("API for dropdown location ", homeService.getBannerLocation(), HttpStatus.OK);
		
	}

	@PostMapping(value = "/addBanner", headers = "Accept=application/json")
	public ResponseEntity<Object> addBanner(@RequestBody Banner req) {
		try {
			if(req.getType().equals("Category")){
				req.setCatId(req.getCategoryId());
				req.setSubCatId(0);
			}else{
				req.setCatId(0);
			}
			if(req.getType().equals("SubCategory")){
				req.setSubCatId(req.getSubCategoryId()[0]);
				req.setCatId(0);
			}else{
				req.setSubCatId(0);
			}
			for (String img : req.getMultiImages()) {
				byte[] imageByte = Base64.decodeBase64(img.split(",")[1]);
				String directory = "/img/banner/" + req.getType() + "" + new Date().getTime() + ".jpg";
				// new FileOutputStream(dir + directory).write(imageByte);
				new FileOutputStream("/home/f2df/ea-tomcat85/webapps" + directory).write(imageByte);
				req.setImg(directory);
				req.setUrlLink(req.getLink());
				int id = homeService.addBanner(req);
			}
			
		} catch (Exception e) {
			return generateResponse("Banner fail to save " + e.getMessage(), 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return generateResponse("Banner save succesfully", "", HttpStatus.OK);

	}

	@PostMapping(value = "/updateBanner", headers = "Accept=application/json")
	public ResponseEntity<Object> updateBanner(@RequestBody Banner req) {
		try {
			if (req.getImg().contains("/img/banner/")) {
				int id = homeService.updateBanner(req);
				if (id != 0) {
					return generateResponse("Banner save/update succesfully", id, HttpStatus.OK);
				} else {
					return generateResponse("Banner fail to save/update ", 0, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				byte[] imageByte = Base64.decodeBase64(req.getImg().split(",")[1]);
				String directory = "/img/banner/" + req.getType() + "" + new Date().getTime() + ".jpg";
				// new FileOutputStream(dir + directory).write(imageByte);
				new FileOutputStream("/home/f2df/ea-tomcat85/webapps" + directory).write(imageByte);
				req.setImg(directory);
				int id = homeService.updateBanner(req);
				if (id != 0) {
					return generateResponse("Banner update succesfully", id, HttpStatus.OK);
				} else {
					return generateResponse("Banner fail to update ", 0, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		} catch (Exception e) {
			return generateResponse("Banner fail to update " + e.getMessage(), 0, HttpStatus.INTERNAL_SERVER_ERROR);
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
