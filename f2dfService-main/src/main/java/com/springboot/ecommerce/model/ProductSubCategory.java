package com.springboot.ecommerce.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ProductSubCategory")
public class ProductSubCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int psc_id;

	@Column
	private String subCategoryName;
	
	@Transient
	private int categoryId;

	@Column
	private String subCategoryImg;

	@OneToMany(fetch = FetchType.EAGER) // one subcategory can have multiple
										// subcategory features
	@JsonBackReference
	@JsonIgnore
	@OrderBy("scf_id")
	@JoinColumn(name = "psc_id")
	private Set<SubCategoryFeature> subCategoryFeatures = new HashSet<>();

	@ManyToOne(fetch = FetchType.EAGER) // multiple subcategories can have one
										// category
	@JsonBackReference
	@JoinColumn(name = "pc_id")
	private ProductCategory productCategory;

	public int getPsc_id() {
		return psc_id;
	}

	public void setPsc_id(int psc_id) {
		this.psc_id = psc_id;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public Set<SubCategoryFeature> getSubCategoryFeatures() {
		return subCategoryFeatures;
	}

	public void setSubCategoryFeatures(Set<SubCategoryFeature> subCategoryFeatures) {
		this.subCategoryFeatures = subCategoryFeatures;
	}

	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public String getSubCategoryImg() {
		return subCategoryImg;
	}

	public void setSubCategoryImg(String subCategoryImg) {
		this.subCategoryImg = subCategoryImg;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
}
