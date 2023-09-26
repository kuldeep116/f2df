package com.springboot.ecommerce.model;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.springboot.ecommerce.controller.ProductController;

@Entity
@Table(name = "product")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonPOJOBuilder
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int p_id;

	@Column(name = "p_name")
	private String productName;

	@Column(name = "typeOfProduct")
	private String typeOfProduct;

	@Column(name = "p_fee")
	private int productFee;

	@Column(name = "old_fee")
	private int oldFee;


	@Column(name = "productDesc")
	private String productDesc;

	@Transient
	private String[] multiProductImages;

	@Column(name = "disFee")
	private int disFee;

	@Column(name = "img1")
	private String img1;

	@Column(name = "img2")
	private String img2;

	@Column(name = "img3")
	private String img3;

	@Column(name = "img4")
	private String img4;

	@Column(name = "userId")
	private int userId;

	@Column(name = "lognitude")
	private BigDecimal lognitude;

	@Column(name = "latitude")
	private BigDecimal latitude;

	@Column(name = "recommended")
	private boolean recommended;

	@Column(name = "bestSeller")
	private boolean bestSeller;

	@Column(name = "publish")
	private boolean publish;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "showType")
	private String showType = "N";

	@Column(name = "assured")
	private boolean assured = false;

	@Column(name = "organic")
	private boolean organic = false;
	
	@Transient
	private boolean aggrement;
	
	@Column(name = "digitalAggrement")
	private String digitalAggrement;

	@ManyToOne
	@JoinColumn(name = "pc_id")
	@JsonIgnoreProperties({ "subCategories" })
	private ProductCategory productCategory;

	@ManyToOne
	@JsonIgnoreProperties(value = { "subCategoryFeatures" })
	@JoinColumn(name = "psc_id")
	private ProductSubCategory subCategory;

	@OneToMany(fetch = FetchType.EAGER)
	@JsonBackReference
	@JsonIgnore
	@JoinColumn(name = "p_id")
	private List<ProductFeatureValue> featureDetailsValue;

	@Transient
	private int pc_id;

	@Column(name = "featureDetailsValueJson")
	private String featureDetailsValueJson;

	@Transient
	private int psc_id;

	@Transient
	private boolean isAddShow;

	@Column(name = "createDate")
	private Date createDate;

	@Column(name = "updateDate")
	private Date updateDate;

	public int getP_id() {
		return p_id;
	}

	public void setP_id(int p_id) {
		this.p_id = p_id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductFee() {
		return productFee;
	}

	public void setProductFee(int productFee) {
		this.productFee = productFee;
	}

	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public ProductSubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(ProductSubCategory subCategory) {
		this.subCategory = subCategory;
	}

	public List<ProductFeatureValue> getFeatureDetailsValue() {
		return featureDetailsValue;
	}

	public void setFeatureDetailsValue(List<ProductFeatureValue> featureDetailsValue) {
		this.featureDetailsValue = featureDetailsValue;
	}

	public int getOldFee() {
		return oldFee;
	}

	public void setOldFee(int oldFee) {
		this.oldFee = oldFee;
	}

	public String getImg1() {
		return img1;
	}

	public void setImg1(String img1) {
		if (img1 == null && img1.trim().isEmpty() && !imgExisting(ProductController.rootPath + img1)) {
			this.img1 = "/img/product/noImg.webp";
		} else {
			this.img1 = img1;
		}
	}

	public String getImg2() {
		return img2;
	}

	public boolean imgExisting(String path) {
		File f = new File(path);
		boolean istrue = false;
		if (f.exists()) {
			istrue = true;
		}
		return istrue;
	}

	public void setImg2(String img2) {
		this.img2 = img2;
	}

	public String getImg3() {
		return img3;
	}

	public void setImg3(String img3) {
		this.img3 = img3;
	}

	public String getImg4() {
		return img4;
	}

	public void setImg4(String img4) {
		this.img4 = img4;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public BigDecimal getLognitude() {
		return lognitude;
	}

	public void setLognitude(BigDecimal lognitude) {
		this.lognitude = lognitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public boolean isRecommended() {
		return recommended;
	}

	public void setRecommended(boolean recommended) {
		this.recommended = recommended;
	}

	public boolean isBestSeller() {
		return bestSeller;
	}

	public void setBestSeller(boolean bestSeller) {
		this.bestSeller = bestSeller;
	}

	public int getDisFee() {
		return disFee;
	}

	public void setDisFee(int disFee) {
		this.disFee = disFee;
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

	public String getTypeOfProduct() {
		return typeOfProduct;
	}

	public void setTypeOfProduct(String typeOfProduct) {
		this.typeOfProduct = typeOfProduct;
	}

	public int getPc_id() {
		return pc_id;
	}

	public void setPc_id(int pc_id) {
		this.pc_id = pc_id;
	}

	public int getPsc_id() {
		return psc_id;
	}

	public void setPsc_id(int psc_id) {
		this.psc_id = psc_id;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public boolean isPublish() {
		return publish;
	}

	public void setPublish(boolean publish) {
		this.publish = publish;
	}

	public String[] getMultiProductImages() {
		return multiProductImages;
	}

	public void setMultiProductImages(String[] multiProductImages) {
		this.multiProductImages = multiProductImages;
	}

	public String getFeatureDetailsValueJson() {
		return featureDetailsValueJson;
	}

	public void setFeatureDetailsValueJson(String featureDetailsValueJson) {
		this.featureDetailsValueJson = featureDetailsValueJson;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public boolean isAddShow() {
		return isAddShow;
	}

	public boolean isOrganic() {
		return organic;
	}

	public void setOrganic(boolean organic) {
		this.organic = organic;
	}

	public void setAddShow(boolean isAddShow) {
		this.isAddShow = isAddShow;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public boolean isAssured() {
		return assured;
	}

	public void setAssured(boolean assured) {
		this.assured = assured;
	}

	public boolean isAggrement() {
		return aggrement;
	}

	public void setAggrement(boolean aggrement) {
		this.aggrement = aggrement;
	}

	public String getDigitalAggrement() {
		return digitalAggrement;
	}

	public void setDigitalAggrement(String digitalAggrement) {
		this.digitalAggrement = digitalAggrement;
	}




}
