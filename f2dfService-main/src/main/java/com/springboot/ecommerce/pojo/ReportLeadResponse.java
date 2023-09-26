package com.springboot.ecommerce.pojo;

public class ReportLeadResponse {

	private String ProductName;
	private String ProductPrice;
	private String ProductType;
	private String CategoryName;

	private String SubCategoryName;
	private String Quantity;
	private String ProductStatus;
	private String DukanName;
	private String DukanLink;

	private String UserName;
	private String UserContactNo;
	private String City;
	private String State;
	private String Pincode;
	private String PostedDate;

	private String Type;
	private String ProductValue;

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public String getProductPrice() {
		return ProductPrice;
	}

	public void setProductPrice(String productPrice) {
		ProductPrice = productPrice;
	}

	public String getCategoryName() {
		return CategoryName;
	}

	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}

	public String getSubCategoryName() {
		return SubCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		SubCategoryName = subCategoryName;
	}

	public String getQuantity() {
		return Quantity;
	}

	public void setQuantity(String quantity) {
		Quantity = quantity;
	}

	public String getDukanName() {
		return DukanName;
	}

	public void setDukanName(String dukanName) {
		DukanName = dukanName;
	}

	public String getProductType() {
		return ProductType;
	}

	public void setProductType(String productType) {
		ProductType = productType;
	}

	public String getDukanLink() {
		return DukanLink;
	}

	public void setDukanLink(String dukanLink) {
		DukanLink = dukanLink;
	}

	public String getProductStatus() {
		return ProductStatus;
	}

	public void setProductStatus(String productStatus) {
		ProductStatus = productStatus;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getUserContactNo() {
		return UserContactNo;
	}

	public void setUserContactNo(String userContactNo) {
		UserContactNo = userContactNo;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getPincode() {
		return Pincode;
	}

	public void setPincode(String pincode) {
		Pincode = pincode;
	}

	public String getPostedDate() {
		return PostedDate;
	}

	public void setPostedDate(String postedDate) {
		PostedDate = postedDate;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getProductValue() {
		return ProductValue;
	}

	public void setProductValue(String productValue) {
		ProductValue = productValue;
	}

}
