package com.springboot.ecommerce.pojo;

import java.util.List;

public class CreateOrderDTO {

	private Integer userId;
	
	
	private List<OrderItemDTO> orderItems;

	private Integer shippingAddressId;

	public CreateOrderDTO() {
		// Default constructor
	}

	public Integer getUserId() {
		return userId;
	}

	public Integer getShippingAddressId() {
		return shippingAddressId;
	}

	public void setShippingAddressId(Integer shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<OrderItemDTO> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemDTO> orderItems) {
		this.orderItems = orderItems;
	}
}
