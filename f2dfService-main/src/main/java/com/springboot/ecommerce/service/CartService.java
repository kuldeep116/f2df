package com.springboot.ecommerce.service;

import java.util.ArrayList;

import com.springboot.ecommerce.pojo.AddToCartRequest;
import com.springboot.ecommerce.pojo.CartResponse;
import com.springboot.ecommerce.pojo.CreateOrderDTO;
import com.springboot.ecommerce.pojo.OrderResponseDtoNew;
import com.springboot.ecommerce.pojo.ResultDTO;

public interface CartService {

	void addtoCart(AddToCartRequest request);

	CartResponse getAllCartItemsByUser(int userId);

	void addToCartOrUpdateQuantity(int productId, int userId, int quantityToAdd);

	void removeCart(String cartItemUuid);

	ResultDTO createOrder(CreateOrderDTO order);

	ArrayList<OrderResponseDtoNew> getDukanOrder(Integer userId);

	ArrayList<OrderResponseDtoNew> getCustomerOrderList(Integer userId);

	boolean acceptOrderItemFromDukan(String orderItemUUid);

	boolean rejectOrderItemFromDukan(String orderItemUUid);

	boolean cancelOrder(String orderUUid);

	ArrayList<OrderResponseDtoNew> getOrderDetails(String orderUUid);

}
