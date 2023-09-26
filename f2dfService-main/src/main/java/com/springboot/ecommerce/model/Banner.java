package com.springboot.ecommerce.model;

import java.util.Arrays;
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
@Table(name = "Banner")
public class Banner {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "img")
	private String img;

	@Transient
	private String[] multiImages;

	@Column(name = "type")
	private String type;

	@Column(name = "catId")
	private int catId;
	
	@Column(name = "amount")
	private int amount;
	
	@Column(name = "startDate")
	private Date startDate;
	
	@Column(name = "endDate")
	private Date endDate;

	@Column(name = "subCatId")
	private int subCatId;

	@Column(name = "userId")
	private int userId;

	@Column(name = "link")
	private String urlLink;
	
	@Transient
	private String link;

	@Column(name = "active")
	private boolean active;

	@Transient
	private String catgeoryName;

	@Transient
	private int categoryId;

	@Transient
	private int[] subCategoryId;

	@Transient
	private String subCatgeoryName;

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

	public String getUrlLink() {
		return urlLink;
	}

	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public int getSubCatId() {
		return subCatId;
	}

	public void setSubCatId(int subCatId) {
		this.subCatId = subCatId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String[] getMultiImages() {
		return multiImages;
	}

	public void setMultiImages(String[] multiImages) {
		this.multiImages = multiImages;
	}

	public String getCatgeoryName() {
		return catgeoryName;
	}

	public void setCatgeoryName(String catgeoryName) {
		this.catgeoryName = catgeoryName;
	}

	public String getSubCatgeoryName() {
		return subCatgeoryName;
	}

	public void setSubCatgeoryName(String subCatgeoryName) {
		this.subCatgeoryName = subCatgeoryName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int[] getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(int[] subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "Banner [id=" + id + ", name=" + name + ", img=" + img + ", multiImages=" + Arrays.toString(multiImages)
				+ ", type=" + type + ", catId=" + catId + ", subCatId=" + subCatId + ", userId=" + userId + ", urlLink="
				+ urlLink + ", active=" + active + ", catgeoryName=" + catgeoryName + ", categoryId=" + categoryId
				+ ", subCategoryId=" + Arrays.toString(subCategoryId) + ", subCatgeoryName=" + subCatgeoryName + "]";
	}

}
