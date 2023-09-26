package com.springboot.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "ProductCategory")
public class ProductCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int pc_id;

	@Column
	@OrderBy("categoryName")
	private String categoryName;

	@Column
	private String categoryImg;

	@Column
	private String productType;

	@OneToMany(fetch = FetchType.EAGER)
	@JsonBackReference
	@JsonIgnore
	@OrderBy("subCategoryName")
	@JoinColumn(name = "pc_id")
	private Set<ProductSubCategory> subCategories = new HashSet<>();

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

	public Set<ProductSubCategory> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(Set<ProductSubCategory> subCategories) {
		this.subCategories = subCategories;
	}

	public String getCategoryImg() {
		return categoryImg;
	}

	public void setCategoryImg(String categoryImg) {
		this.categoryImg = categoryImg;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

}
