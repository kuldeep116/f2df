package com.springboot.ecommerce.pojo;

public class RequestProduct {

	private int productId;
	private int pc_id;
	private int psc_id;
	private int minCost=0;
	private int maxCost=10000000;
	private java.util.List<String> brandName;
	private String productName;
	private int pageNo = 0;
	private int size = 10;
	private String productType;
	private String searchKeyword;
	
	private String home;

	private int userId;
	private int page;
	private int id;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
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

	public int getMinCost() {
		return minCost;
	}

	public void setMinCost(int minCost) {
		this.minCost = minCost;
	}

	public int getMaxCost() {
		return maxCost;
	}

	public void setMaxCost(int maxCost) {
		this.maxCost = maxCost;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public void setBrandName(java.util.List<String> brandName) {
		this.brandName = brandName;
	}

	public java.util.List<String> getBrandName() {
		return brandName;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	@Override
	public String toString() {
		return "RequestProduct [productId=" + productId + ", pc_id=" + pc_id + ", psc_id=" + psc_id + ", minCost="
				+ minCost + ", maxCost=" + maxCost + ", brandName=" + brandName + ", productName=" + productName
				+ ", pageNo=" + pageNo + ", size=" + size + ", productType=" + productType + ", searchKeyword="
				+ searchKeyword + ", home=" + home + ", userId=" + userId + ", page=" + page + ", id=" + id + "]";
	}

}
