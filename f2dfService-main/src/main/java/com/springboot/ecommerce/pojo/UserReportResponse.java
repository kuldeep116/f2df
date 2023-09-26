package com.springboot.ecommerce.pojo;

public class UserReportResponse {

	private String name;
	private String username;
	private String contact;
	private String date;
	private String time;
	private String business_name;
	private String url;
	private String pincode;
	private String district_name;
	private String state_name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getDistrict_name() {
		return district_name;
	}

	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}

	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	@Override
	public String toString() {
		return "UserReportResponse [name=" + name + ", username=" + username + ", contact=" + contact + ", date=" + date
				+ ", time=" + time + ", business_name=" + business_name + ", url=" + url + ", pincode=" + pincode
				+ ", district_name=" + district_name + ", state_name=" + state_name + "]";
	}

}
