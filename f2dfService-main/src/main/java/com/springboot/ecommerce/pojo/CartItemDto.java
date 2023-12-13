package com.springboot.ecommerce.pojo;

public class CartItemDto {
private String cartUUid;

private String productName;

private String productImg;

private int price;

private int productId;

public int getProductId() {
	return productId;
}

public void setProductId(int productId) {
	this.productId = productId;
}

public String getCartUUid() {
	return cartUUid;
}

public void setCartUUid(String cartUUid) {
	this.cartUUid = cartUUid;
}

public String getProductName() {
	return productName;
}

public void setProductName(String productName) {
	this.productName = productName;
}

public String getProductImg() {
	return productImg;
}

public void setProductImg(String productImg) {
	this.productImg = productImg;
}

public int getPrice() {
	return price;
}

public void setPrice(int price) {
	this.price = price;
}

public int getActualPrice() {
	return actualPrice;
}

public void setActualPrice(int actualPrice) {
	this.actualPrice = actualPrice;
}

public int getQunatity() {
	return qunatity;
}

public void setQunatity(int qunatity) {
	this.qunatity = qunatity;
}

private int actualPrice;

private int qunatity;


}
