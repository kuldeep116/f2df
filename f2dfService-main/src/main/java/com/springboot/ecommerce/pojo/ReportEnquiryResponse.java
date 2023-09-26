package com.springboot.ecommerce.pojo;

public class ReportEnquiryResponse {

	private String UserName;
	private String UserContact;
	

	private String Quantity;
	private String ProductName;
	private String CreateDate;
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getUserContact() {
		return UserContact;
	}
	public void setUserContact(String userContact) {
		UserContact = userContact;
	}
	public String getQuantity() {
		return Quantity;
	}
	public void setQuantity(String quantity) {
		Quantity = quantity;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public String getCreateDate() {
		return CreateDate;
	}
	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}


}
