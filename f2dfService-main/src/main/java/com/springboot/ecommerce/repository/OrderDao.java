package com.springboot.ecommerce.repository;

import java.util.List;

import com.springboot.ecommerce.model.OrderDetail;

public interface OrderDao {

	public void create(OrderDetail order);

	List<OrderDetail> getOrderByUser(long userId,String status);

	List<OrderDetail> getOrderByProductUser(long userId,String status);

	void updateStatusOfOrderByUser(int userId, String status);

	void updateStatusOfOrderByProductUser(int userId, String status);

	void deleteOrder(int id);
	
}
