package com.springboot.ecommerce.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.springboot.ecommerce.model.ProductCategory;
import com.springboot.ecommerce.pojo.RequestProduct;
import com.springboot.ecommerce.service.HomeService;
import com.springboot.ecommerce.service.ProductCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/product-category")
@Tag(name = "Product Category API")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductCategoryController {

	@Autowired
	ProductCategoryService categoryService;

	@Autowired
	HomeService homeService;
	private static String UPLOAD_DIR = "/home/f2df/ea-tomcat85/webapps/img/Cat/";

	@PostMapping(value = "/category", headers = "Accept=application/json")
	public ResponseEntity<Object> createNewProduct(@RequestBody ProductCategory productCategory,
			UriComponentsBuilder ucBuilder) throws FileNotFoundException, IOException {
		try {
			String img = productCategory.getCategoryImg();
			String imageName = productCategory.getCategoryName()+"_"+new Date().getTime() + ".jpg";
			String uploadFilePath = UPLOAD_DIR + imageName;
			System.out.println("file name" + uploadFilePath);
			byte[] imageByte = Base64.decodeBase64(img.split(",")[1]);
			new FileOutputStream(uploadFilePath).write(imageByte);
			productCategory.setCategoryImg("/img/Cat/" + imageName);
			categoryService.createProductCategory(productCategory);
			return generateResponse("Category save succesfully", "", HttpStatus.OK);
		} catch (Exception e) {
			return generateResponse("Category not save succesfully", "", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/updateCategory/", headers = "Accept=application/json")
	public ResponseEntity<Object> updateProduct(@RequestBody ProductCategory productCategory) {
		try {
			String img = productCategory.getCategoryImg();
			String imageName = productCategory.getCategoryName()+"_"+new Date().getTime() + ".jpg";
			String uploadFilePath = UPLOAD_DIR + imageName;
			System.out.println("file name" + uploadFilePath);
			byte[] imageByte = Base64.decodeBase64(img.split(",")[1]);
			new FileOutputStream(uploadFilePath).write(imageByte);
			productCategory.setCategoryImg("/img/Cat/" + imageName);
			categoryService.editProductCategory(productCategory);
			return generateResponse("Category save succesfully", "", HttpStatus.OK);
		} catch (Exception e) {
			return generateResponse("Category not save succesfully", "", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(value = "/category/{id}", headers = "Accept=application/json")
	public ResponseEntity<Object> deleteProduct(@PathVariable("id") int id) {
		ProductCategory productCategory = categoryService.findCategoryById(id);
		if (productCategory == null) {
			return generateResponse("Category not found", "", HttpStatus.NOT_FOUND);
		}
		categoryService.removeCategoryById(id);
		return generateResponse("Category delete succesfully ", "", HttpStatus.OK);
	}

	@PostMapping(value = "/getCategory", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getTypeById(@RequestBody RequestProduct req) {

		ProductCategory productCategory = categoryService.findCategoryById(req.getPc_id());

		if (productCategory == null) {
			return generateResponse("No category with this id", "", HttpStatus.NOT_FOUND);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "Cateogy list ");
		map.put("status", 200);
		map.put("data", productCategory);

		map.put("bannerList", homeService.getBannerByCatId(req.getPc_id()));
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}

	@PostMapping(value = "/categories", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllTypes(@RequestBody RequestProduct req) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", "Cateogy list ");
		map.put("status", 200);
		map.put("data", categoryService.getCategoryList(req.getPageNo(), req.getSize(), ""));
		map.put("bannerList", homeService.getBannerByCatId(0));
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}

	public static ResponseEntity<Object> generateResponse(String message, Object responseObj, HttpStatus status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}
}
