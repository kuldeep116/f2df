package com.springboot.ecommerce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.springboot.ecommerce.model.ProductCategory;
import com.springboot.ecommerce.model.ProductSubCategory;
import com.springboot.ecommerce.model.SubCategoryFeature;
import com.springboot.ecommerce.pojo.DeleteFeature;
import com.springboot.ecommerce.service.ProductCategoryService;
import com.springboot.ecommerce.service.SubCategoryFeatureService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/subcategory-feature")
@Tag(name = "SubCategory Feature API")
public class SubcategoryFeatureController {

	@Autowired
	SubCategoryFeatureService featureService;

	@Autowired
	ProductCategoryService categoryService;
	private static Logger logger = Logger.getLogger(SubcategoryFeatureController.class.getName());
	@PostMapping(value = "/feature", headers = "Accept=application/json")
	public ResponseEntity<Object> createNewSubCategoryFeature(@RequestBody SubCategoryFeature feature,
			UriComponentsBuilder ucBuilder) {
		try {
			if (feature.getScf_id() != 0) {
				int id = featureService.createSubCategoryFeature(feature);

			} else {
				for (int sc : feature.getSubCategoryIdList()) {
					ProductSubCategory scb = new ProductSubCategory();
					SubCategoryFeature featureNew=new SubCategoryFeature();
					scb.setPsc_id(sc);
					logger.debug("sub categry ids -- "+sc);
					featureNew.setSubCategory(scb);
					featureNew.setFeatureType(feature.getFeatureType());
					featureNew.setProductFeatureDetail(feature.getProductFeatureDetail());
					featureNew.setSubCategoryFeatureName(feature.getSubCategoryFeatureName());
					featureService.createSubCategoryFeature(featureNew);
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("message", "Created succesfully");
			map.put("status", HttpStatus.OK);
			map.put("data", "");

			return generateResponse("feature save", "", HttpStatus.OK);
		} catch (Exception e) {
			return generateResponse("feature not save DUE TO " + e.getMessage(), "", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	public static ResponseEntity<Object> generateResponse(String message, Object responseObj, HttpStatus status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);
		return new ResponseEntity<Object>(map, status);
	}

	@PutMapping(value = "/feature/", headers = "Accept=application/json")
	public ResponseEntity<Object> updateSubCategoryFeature(@RequestBody SubCategoryFeature subCategoryFeature) {
		try {
			featureService.editSubCategoryFeature(subCategoryFeature);
			HomeController.homemap = new HashMap<String, Object>();
			HomeController.rentCategrory = new ArrayList<ProductCategory>();
			HomeController.AllCat = new ArrayList<ProductCategory>();
			return generateResponse("feature update successfuly", "", HttpStatus.OK);
		} catch (Exception e) {
			return generateResponse("feature not update ", "", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/deletefeature/{id}", headers = "Accept=application/json")
	public ResponseEntity<Object> deleteSubCategoryFeature(@PathVariable("id") int id) {
		SubCategoryFeature subCategoryFeature = featureService.findSubCategoryFeatureById(id);
		if (subCategoryFeature == null) {
			return generateResponse("feature not found", "", HttpStatus.NOT_FOUND);
		}
		featureService.removeSubCategoryFeatureById(id);
		return generateResponse("feature delete successfully", "", HttpStatus.OK);
	}

	@PostMapping(value = "/deletefeature", headers = "Accept=application/json")
	public ResponseEntity<Object> deletefeature(@RequestBody DeleteFeature req) {
		for (int id : req.getFeatureIds()) {
			featureService.removeSubCategoryFeatureById(id);
		}
		return generateResponse("feature delete successfully", "", HttpStatus.OK);
	}

	@GetMapping(value = "/feature/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSubCategoryFeatureId(@PathVariable("id") int id) {

		SubCategoryFeature feature = featureService.findSubCategoryFeatureById(id);

		if (feature == null) {
			return generateResponse("feature not found ", "", HttpStatus.NOT_FOUND);
		}
		return generateResponse("feature List get ", feature, HttpStatus.OK);
	}

	@GetMapping(value = "/features", headers = "Accept=application/json")
	public ResponseEntity<Object> getAllSubCategoryFeatures() {
		List<ProductCategory> categories = categoryService.getAllCategories();
		List<SubCategoryFeature> featuresList = new ArrayList<SubCategoryFeature>();
		for (ProductCategory cat : categories) {
			for (ProductSubCategory psc : cat.getSubCategories()) {
				for (SubCategoryFeature sb : psc.getSubCategoryFeatures()) {
					SubCategoryFeature scf = new SubCategoryFeature();
					scf.setScf_id(sb.getScf_id());
					scf.setPc_id(cat.getPc_id());
					scf.setCategoryName(cat.getCategoryName());
					scf.setPsc_id(psc.getPsc_id());
					scf.setSubCategoryName(psc.getSubCategoryName());
					scf.setSubCategoryFeatureName(sb.getSubCategoryFeatureName());
					scf.setFeatureType(sb.getFeatureType());
					scf.setProductFeatureDetail(sb.getProductFeatureDetail());
					featuresList.add(scf);
				}

			}
		}
		return generateResponse("feature list", featuresList, HttpStatus.OK);
	}
}
