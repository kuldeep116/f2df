package com.springboot.ecommerce.pojo;

import java.util.ArrayList;

import com.springboot.ecommerce.model.Address;
import com.springboot.ecommerce.model.DeliveryAddress;

public class OrderResponseDtoNew {
	private String orderUUid;

	private double totalPrice;

	private DeliveryAddress deliveryAddress;

	private ArrayList<OrderItemDTO> itemList;

	private String oderDate;

	public String getOrderUUid() {
		return orderUUid;
	}

	public void setOrderUUid(String orderUUid) {
		this.orderUUid = orderUUid;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public DeliveryAddress getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public ArrayList<OrderItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<OrderItemDTO> itemList) {
		this.itemList = itemList;
	}

	public String getOderDate() {
		return oderDate;
	}

	public void setOderDate(String oderDate) {
		this.oderDate = oderDate;
	}

}
