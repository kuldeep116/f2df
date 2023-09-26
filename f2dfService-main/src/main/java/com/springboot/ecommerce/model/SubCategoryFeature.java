package com.springboot.ecommerce.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "SubCategoryFeature")
public class SubCategoryFeature {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@OrderBy
	private int scf_id;

	@Column
	private String subCategoryFeatureName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonBackReference
	@JoinColumn(name = "psc_id")
	private ProductSubCategory subCategory;

	@Transient
	private int[] subCategoryIdList;

	@Column
	private String featureType;

	@Transient
	private int psc_id;

	@Transient
	private int pc_id;

	@Transient
	private String categoryName;

	@Transient
	private String subCategoryName;

	@Column
	private String seq;

	@Column
	private String productFeatureDetail;

	@Transient
	private List<String> productFeatureDetailsList;

	public int getScf_id() {
		return scf_id;
	}

	public void setScf_id(int sf_id) {
		this.scf_id = sf_id;
	}

	public String getSubCategoryFeatureName() {
		return subCategoryFeatureName.toUpperCase();
	}

	public void setSubCategoryFeatureName(String subCategoryFeatureName) {
		this.subCategoryFeatureName = subCategoryFeatureName.toUpperCase();
	}

	public ProductSubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(ProductSubCategory subCategory) {
		this.subCategory = subCategory;
	}

	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getProductFeatureDetail() {
		return productFeatureDetail;
	}

	public void setProductFeatureDetail(String productFeatureDetail) {
		this.productFeatureDetail = productFeatureDetail;
	}

	public List<String> getProductFeatureDetailsList() {
		String str = this.getProductFeatureDetail();
		List<String> list = new ArrayList<>();
		if (str != null) {
			for (String featuredetails : str.split(",")) {
				list.add(featuredetails);
			}
			setProductFeatureDetailsList(list);
		}
		return productFeatureDetailsList;
	}

	public void setProductFeatureDetailsList(List<String> productFeatureDetailsList) {
		this.productFeatureDetailsList = productFeatureDetailsList;
	}

	public int[] getSubCategoryIdList() {
		return subCategoryIdList;
	}

	public void setSubCategoryIdList(int[] subCategoryIdList) {
		this.subCategoryIdList = subCategoryIdList;
	}

	public int getPsc_id() {
		return psc_id;
	}

	public void setPsc_id(int psc_id) {
		this.psc_id = psc_id;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public int getPc_id() {
		return pc_id;
	}

	public void setPc_id(int pc_id) {
		this.pc_id = pc_id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

}
