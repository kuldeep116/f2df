package com.springboot.ecommerce.pojo;

public class AddToCartRequest {
	private int productId;
    private int quantity=1;
    private int userId;
    private int quantityToAdd;
    private int quantityToDecrease;
   
	public int getQuantityToDecrease() {
		return quantityToDecrease;
	}
	public void setQuantityToDecrease(int quantityToDecrease) {
		this.quantityToDecrease = quantityToDecrease;
	}
	public int getQuantityToAdd() {
		return quantityToAdd;
	}
	public void setQuantityToAdd(int quantityToAdd) {
		this.quantityToAdd = quantityToAdd;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
