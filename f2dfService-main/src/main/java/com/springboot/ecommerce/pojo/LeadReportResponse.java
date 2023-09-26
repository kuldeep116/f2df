package com.springboot.ecommerce.pojo;

public class LeadReportResponse {

	private String user;
	private String business_name;
	private String permalink;
	private String contact;
	private String State_name;
	private String district_name;
	private String pincode;
	private String process_type;
	private String category;
	private String sub_category;
	private String productcategory;
	private String date;
	private String time;
	private String quantity;
	private String unit;
	private String price;
	private String product_value;
	private String BusinessStatus;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		user = user;
	}

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}

	public String getPermalink() {
		return permalink;
	}

	public void setPermalink(String permalink) {
		this.permalink = permalink;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getState_name() {
		return State_name;
	}

	public void setState_name(String state_name) {
		State_name = state_name;
	}

	public String getDistrict_name() {
		return district_name;
	}

	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getProcess_type() {
		return process_type;
	}

	public void setProcess_type(String process_type) {
		this.process_type = process_type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSub_category() {
		return sub_category;
	}

	public void setSub_category(String sub_category) {
		this.sub_category = sub_category;
	}

	public String getProductcategory() {
		return productcategory;
	}

	public void setProductcategory(String productcategory) {
		this.productcategory = productcategory;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getProduct_value() {
		return product_value;
	}

	public void setProduct_value(String product_value) {
		this.product_value = product_value;
	}

	public String getBusinessStatus() {
		return BusinessStatus;
	}

	public void setBusinessStatus(String businessStatus) {
		BusinessStatus = businessStatus;
	}

	@Override
	public String toString() {
		return "LeadReportResponse [User=" + user + ", business_name=" + business_name + ", permalink=" + permalink
				+ ", contact=" + contact + ", State_name=" + State_name + ", district_name=" + district_name
				+ ", pincode=" + pincode + ", process_type=" + process_type + ", category=" + category
				+ ", sub_category=" + sub_category + ", productcategory=" + productcategory + ", date=" + date
				+ ", time=" + time + ", quantity=" + quantity + ", unit=" + unit + ", price=" + price
				+ ", product_value=" + product_value + ", BusinessStatus=" + BusinessStatus + "]";
	}

}
