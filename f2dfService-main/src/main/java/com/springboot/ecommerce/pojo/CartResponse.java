package com.springboot.ecommerce.pojo;

import java.util.List;

import com.springboot.ecommerce.model.CartItem;

public class CartResponse {
	private List<CartItemDto> cartItems;
    private double totalPrice;

    
    public List<CartItemDto> getCartItems() {
		return cartItems;
	}


	public void setCartItems(List<CartItemDto> cartItems) {
		this.cartItems = cartItems;
	}


	public double getTotalPrice() {
		return totalPrice;
	}


	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}


	public CartResponse(List<CartItemDto> cartItems, double totalPrice) {
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
    }
}
