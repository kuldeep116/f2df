package com.springboot.ecommerce.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "ProductFeatureValue")
public class ProductFeatureValue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column
	private String productFeatureKey;

	@Column
	private String productFeatureValue;
	
	@ManyToOne(fetch = FetchType.EAGER)     //multiple subcategories can have one category
    @JsonBackReference
    @JoinColumn(name = "p_id")
    private Product product;
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductFeatureKey() {
		return productFeatureKey;
	}

	public void setProductFeatureKey(String productFeatureKey) {
		this.productFeatureKey = productFeatureKey;
	}

	public String getProductFeatureValue() {
		return productFeatureValue;
	}

	public void setProductFeatureValue(String productFeatureValue) {
		this.productFeatureValue = productFeatureValue;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	

}
