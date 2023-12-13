package com.springboot.ecommerce.pojo;

public class OrderItemDTO {
	private Long productId;
	private int quantity;
	private String productName;
	private double price;
	private String status;
	private String orderUUid;
	
	public String getOrderUUid() {
		return orderUUid;
	}
	public void setOrderUUid(String orderUUid) {
		this.orderUUid = orderUUid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
