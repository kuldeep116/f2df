package com.springboot.ecommerce.repository;

import java.util.List;

import com.springboot.ecommerce.model.CartItem;
import com.springboot.ecommerce.model.OrderItems;
import com.springboot.ecommerce.model.Orders;

public interface OrderManageDao {

	CartItem getCartItemByProductAndUser(int productId, int userId);
	
	void updateCartQuantity(CartItem cartitem, int quantity);

	void deleteCartItemById(String cartUuid);

	double getOrderTotalPrice(long orderId);

	List<OrderItems> getOrderItemsByUserIdAndOrderDate(Integer userId);

	Orders getOrderDetailsByOrderUuid(String orderUuid);

	List<Orders> getOrderByUserIdAndOrderDate(Integer userId);

	List<OrderItems> getOrderItemsByOrderId(long orderId);

	List<OrderItems> getOrderItemsbasedOnOrderItem(String orderItemUuid);

	void updateOrderStatus(String orderItemUUid, String status);

	List<OrderItems> getOrderItemsbasedOnOrderUUid(String orderItemUuid);

}
