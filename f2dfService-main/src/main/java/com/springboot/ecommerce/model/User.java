package com.springboot.ecommerce.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/*this is Training type*/
@Entity
@Table(name = "User")
public class User {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "img")
	private String img;

	@Column(name = "mobile")
	private long mobile;

	@Column(name = "email")
	private String email;

	@Column(name = "otp")
	private int otp;
	
	@Column(name = "userType")
	private String userType;

	@Column(name = "password")
	private String password;

	@Column(name = "status")
	private boolean status;

	@Column(name = "role")
	private String role;

	@Column(name = "gender")
	private String gender;

	@Column(name = "googleId")
	private String googleId;

	@Transient
	private Address address;

	@Transient
	private String type;
	
	@Transient
	private String dukanName;

	@Transient
	private boolean userExist;

	@Column(name = "reffralCode")
	private String reffralCode;

	@Column(name = "createDate")
	private Date createDate;

	@Column(name = "updateDate")
	private Date updateDate;
	
	@Column(name = "intro")
	private Date intro;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public long getMobile() {
		return mobile;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getOtp() {
		return otp;
	}

	public void setOtp(int otp) {
		this.otp = otp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isUserExist() {
		return userExist;
	}

	public void setUserExist(boolean userExist) {
		this.userExist = userExist;
	}

	public String getReffralCode() {
		return reffralCode;
	}

	public void setReffralCode(String reffralCode) {
		this.reffralCode = reffralCode;
	}

	public Date getIntro() {
		return intro;
	}

	public void setIntro(Date intro) {
		this.intro = intro;
	}

	public String getDukanName() {
		return dukanName;
	}

	public void setDukanName(String dukanName) {
		this.dukanName = dukanName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", img=" + img + ", mobile=" + mobile + ", email=" + email
				+ ", otp=" + otp + ", userType=" + userType + ", password=" + password + ", status=" + status
				+ ", role=" + role + ", gender=" + gender + ", googleId=" + googleId + ", address=" + address
				+ ", type=" + type + ", dukanName=" + dukanName + ", userExist=" + userExist + ", reffralCode="
				+ reffralCode + ", createDate=" + createDate + ", updateDate=" + updateDate + ", intro=" + intro + "]";
	}

	

}
