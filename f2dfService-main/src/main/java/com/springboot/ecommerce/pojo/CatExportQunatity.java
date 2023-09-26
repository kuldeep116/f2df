package com.springboot.ecommerce.pojo;

import java.util.ArrayList;
import java.util.List;

public class CatExportQunatity {

	private String categoryName;
	private String catId;
	private String categoryImg;
	private String quantity;
	private String type;
	private List<SubCatQunatity> subCat = new ArrayList<SubCatQunatity>();

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryImg() {
		return categoryImg;
	}

	public void setCategoryImg(String categoryImg) {
		this.categoryImg = categoryImg;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<SubCatQunatity> getSubCat() {
		return subCat;
	}

	public void setSubCat(List<SubCatQunatity> subCat) {
		this.subCat = subCat;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

}
