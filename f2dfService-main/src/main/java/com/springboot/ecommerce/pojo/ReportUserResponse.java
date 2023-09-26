package com.springboot.ecommerce.pojo;

public class ReportUserResponse {

	private String UserName;
	private String UserContact;
	private String UserCreateDate;

	private String city;
	private String state;
	private String country;
	private String pincode;
	private String dukanName;
	private String dukanLink;

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

	public String getUserCreateDate() {
		return UserCreateDate;
	}

	public void setUserCreateDate(String userCreateDate) {
		UserCreateDate = userCreateDate;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getDukanName() {
		return dukanName;
	}

	public void setDukanName(String dukanName) {
		this.dukanName = dukanName;
	}

	public String getDukanLink() {
		return dukanLink;
	}

	public void setDukanLink(String dukanLink) {
		this.dukanLink = dukanLink;
	}

}
