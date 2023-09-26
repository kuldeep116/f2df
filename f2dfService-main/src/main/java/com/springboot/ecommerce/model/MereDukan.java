package com.springboot.ecommerce.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*this is Training type*/
@Entity
@Table(name = "MereDukan")
public class MereDukan {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "userId")
	private int userId;

	@Column(name = "name")
	private String name;
	
	@Column(name = "logo")
	private String logo;

	@Column(name = "tagLine")
	private String tagLine;

	@Column(name = "yrsOfEst")
	private String yrsOfEst;

	@Column(name = "natureOfBuss")
	private String natureOfBuss;

	@Column(name = "dukanLink")
	private String dukanLink;

	@Column(name = "status")
	private boolean status;

	@Column(name = "fbLink")
	private String fbLink;

	@Column(name = "twitterLink")
	private String twitterLink;

	@Column(name = "instaLink")
	private String instaLink;

	@Column(name = "youTubeLink")
	private String youTubeLink;

	@Column(name = "linkdinLink")
	private String linkdinLink;

	@Column(name = "gitHubLink")
	private String gitHubLink;

	@Column(name = "address")
	private String address;

	@Column(name = "email")
	private String email;
	
	@Column(name = "mobile")
	private String mobile;

	@Column(name = "aboutUs")
	private String aboutUs;

	@Column(name = "createDate")
	private Date createDate;

	@Column(name = "updateDate")
	private Date updateDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTagLine() {
		return tagLine;
	}

	public void setTagLine(String tagLine) {
		this.tagLine = tagLine;
	}

	public String getYrsOfEst() {
		return yrsOfEst;
	}

	public void setYrsOfEst(String yrsOfEst) {
		this.yrsOfEst = yrsOfEst;
	}

	public String getNatureOfBuss() {
		return natureOfBuss;
	}

	public void setNatureOfBuss(String natureOfBuss) {
		this.natureOfBuss = natureOfBuss;
	}

	public String getDukanLink() {
		return dukanLink;
	}

	public void setDukanLink(String dukanLink) {
		this.dukanLink = dukanLink;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getFbLink() {
		return fbLink;
	}

	public void setFbLink(String fbLink) {
		this.fbLink = fbLink;
	}

	public String getTwitterLink() {
		return twitterLink;
	}

	public void setTwitterLink(String twitterLink) {
		this.twitterLink = twitterLink;
	}

	public String getInstaLink() {
		return instaLink;
	}

	public void setInstaLink(String instaLink) {
		this.instaLink = instaLink;
	}

	public String getYouTubeLink() {
		return youTubeLink;
	}

	public void setYouTubeLink(String youTubeLink) {
		this.youTubeLink = youTubeLink;
	}

	public String getLinkdinLink() {
		return linkdinLink;
	}

	public void setLinkdinLink(String linkdinLink) {
		this.linkdinLink = linkdinLink;
	}

	public String getGitHubLink() {
		return gitHubLink;
	}

	public void setGitHubLink(String gitHubLink) {
		this.gitHubLink = gitHubLink;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAboutUs() {
		return aboutUs;
	}

	public void setAboutUs(String aboutUs) {
		this.aboutUs = aboutUs;
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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}