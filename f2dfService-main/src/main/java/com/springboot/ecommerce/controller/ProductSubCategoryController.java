package com.springboot.ecommerce.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.springboot.ecommerce.model.ProductCategory;
import com.springboot.ecommerce.model.ProductSubCategory;
import com.springboot.ecommerce.pojo.RequestProduct;
import com.springboot.ecommerce.service.ProductSubCategoryService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/product-subcategory")
@Tag(name = "Product SubCategory API")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductSubCategoryController {

	@Autowired
	ProductSubCategoryService subCategoryService;
	private static String UPLOAD_DIR = "/home/f2df/ea-tomcat85/webapps/img/subCat/";

	@PostMapping(value = "/subcategory", headers = "Accept=application/json")
	public ResponseEntity<Object> createNewSubCategory(@RequestBody ProductSubCategory subCategory,
			UriComponentsBuilder ucBuilder) {
		try {
			String img = subCategory.getSubCategoryImg();
			String imageName = subCategory.getSubCategoryName()+"_"+new Date().getTime() + ".jpg";
			String uploadFilePath = UPLOAD_DIR + imageName;
			System.out.println("file name" + uploadFilePath);
			byte[] imageByte = Base64.decodeBase64(img.split(",")[1]);
			new FileOutputStream(uploadFilePath).write(imageByte);
			subCategory.setSubCategoryImg("/img/subCat/" + imageName);
			ProductCategory cat= new ProductCategory();
			cat.setPc_id(subCategory.getCategoryId());
			subCategory.setProductCategory(cat);
			subCategoryService.createSubCategory(subCategory);
			return generateResponse("subcategory save succesfully", "", HttpStatus.OK);
		} catch (Exception e) {
			return generateResponse("subcategory not save succesfully", "", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/updateSubCategory", headers = "Accept=application/json")
	public ResponseEntity<Object> updateSubCategory(@RequestBody ProductSubCategory subCategory)
			throws FileNotFoundException, IOException {
		try {
			String img = subCategory.getSubCategoryImg();
			String imageName = subCategory.getPsc_id() + ".jpg";
			String uploadFilePath = UPLOAD_DIR + imageName;
			System.out.println("file name" + uploadFilePath);
			byte[] imageByte = Base64.decodeBase64(img.split(",")[1]);
			new FileOutputStream(uploadFilePath).write(imageByte);
			subCategory.setSubCategoryImg("/img/subCat/" + imageName);
			subCategoryService.editSubCategory(subCategory);
			return generateResponse("subcategory save succesfully", "", HttpStatus.OK);
		} catch (Exception e) {
			return generateResponse("subcategory not save succesfully", "", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(value = "/subcategory/{id}", headers = "Accept=application/json")
	public ResponseEntity<Object> deleteSubCategory(
			@Parameter(description = "ID of the product subcategory") @PathVariable("id") int id) {
		ProductSubCategory subCategory = subCategoryService.findSubCategoryById(id);
		if (subCategory == null) {
			return generateResponse("subcategory Not found", "", HttpStatus.NOT_FOUND);
		}
		subCategoryService.removeSubCategoryById(id);
		return generateResponse("subcategory deleted", "", HttpStatus.OK);
	}

	@PostMapping(value = "/getSubcategory", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSubCategoryById(@RequestBody RequestProduct req) {

		ProductSubCategory subCategory = subCategoryService.findSubCategoryById(req.getPsc_id());

		if (subCategory == null) {
			return generateResponse("Sub category by id ", "", HttpStatus.NOT_FOUND);
		}
		return generateResponse("Sub category by id ", subCategory, HttpStatus.OK);
	}

	@PostMapping(value = "/subcategories", headers = "Accept=application/json")
	public ResponseEntity<Object> geSubCategories(@RequestBody RequestProduct req) {
		return generateResponse("Find All Sub categories as a list",
				subCategoryService.getSubCategoryList(req.getPageNo(), req.getSize()), HttpStatus.OK);
	}

	@PostMapping(value = "/getSubcategories", headers = "Accept=application/json")
	public ResponseEntity<Object> getSubCategories(@RequestBody RequestProduct req) {
		List<ProductSubCategory> subcatList = new ArrayList<ProductSubCategory>();
		for (ProductSubCategory psc : subCategoryService.getSubCategoryList(req.getPc_id())) {
			ProductSubCategory newPSC = new ProductSubCategory();
			newPSC.setPsc_id(psc.getPsc_id());
			newPSC.setSubCategoryName(psc.getSubCategoryName());
			if (psc.getSubCategoryImg() == null) {
				newPSC.setSubCategoryImg("/img/subCat/palaceholder.jpg");
			} else {
				newPSC.setSubCategoryImg(psc.getSubCategoryImg());
			}
			subcatList.add(newPSC);
		}
		return generateResponse("Find All Sub categories as a list", subcatList, HttpStatus.OK);
	}

	@PostMapping(value = "/subAllCategories", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllSubCategories() {
		return generateResponse("Find All Sub categories as a list", subCategoryService.getSubCategoryList(0, 500),
				HttpStatus.OK);
	}

	public static ResponseEntity<Object> generateResponse(String message, Object responseObj, HttpStatus status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}

}
