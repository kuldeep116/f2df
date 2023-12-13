package com.springboot.ecommerce.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.standard.expression.OrExpression;

import com.springboot.ecommerce.model.CartItem;
import com.springboot.ecommerce.model.DeliveryAddress;
import com.springboot.ecommerce.model.OrderItems;
import com.springboot.ecommerce.model.Orders;
import com.springboot.ecommerce.model.Product;
import com.springboot.ecommerce.model.User;
import com.springboot.ecommerce.pojo.AddToCartRequest;
import com.springboot.ecommerce.pojo.CartItemDto;
import com.springboot.ecommerce.pojo.CartResponse;
import com.springboot.ecommerce.pojo.CreateOrderDTO;
import com.springboot.ecommerce.pojo.OrderResponseDtoNew;
import com.springboot.ecommerce.pojo.OrderItemDTO;
import com.springboot.ecommerce.pojo.OrderResponseDto;
import com.springboot.ecommerce.pojo.ResultDTO;
import com.springboot.ecommerce.pojo.StatusCode;
import com.springboot.ecommerce.repository.CommonDao;
import com.springboot.ecommerce.repository.OrderManageDao;

@Component
public class CartServiceImpl implements CartService {
	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderManageDao orderManageDao;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private CommonDao commonDao;

	@Override
	public void addtoCart(AddToCartRequest request) {
		addToCartOrUpdateQuantity(request.getProductId(), request.getUserId(), request.getQuantity());
	}

	public void addToCartOrUpdateQuantity(int productId, int userId, int quantityToAdd) {
		Session session = sessionFactory.openSession();
		try {
			CartItem existingCartItem = orderManageDao.getCartItemByProductAndUser(productId, userId);

			if (existingCartItem != null) {
				orderManageDao.updateCartQuantity(existingCartItem, quantityToAdd);
			} else {
				// Entry doesn't exist, create a new one
				User user = userService.getUser(userId);
				Product product = productService.findProductById(productId);

				if (user == null || product == null) {
					throw new EntityNotFoundException("User or product not found");
				}

				CartItem newCartItem = new CartItem();
				newCartItem.setProduct(product);
				newCartItem.setUser(user);
				newCartItem.setQuantity(quantityToAdd);
				UUID uuid = UUID.randomUUID();
				newCartItem.setCartUuid(uuid.toString());
				session.save(newCartItem);
			}
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or product not found");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
		}
	}

	@Override
	public CartResponse getAllCartItemsByUser(int userId) {
		try (Session session = sessionFactory.openSession()) {

			User user = userService.getUser(userId);
			System.out.println("user ::" + user.getId());
			String hql = "FROM CartItem WHERE user = :user";
			Query<CartItem> query = session.createQuery(hql, CartItem.class);
			query.setParameter("user", user);

			List<CartItem> cartItems = query.getResultList();
			double totalPrice = calculateTotalPrice(cartItems);
			List<CartItemDto> cartItemDtoList = new ArrayList<CartItemDto>();
			cartItemDtoList = bindCartItemData(cartItems);
			return new CartResponse(cartItemDtoList, totalPrice);
		} catch (Exception e) {
			e.printStackTrace();
			// Handle exceptions appropriately
			return new CartResponse(Collections.emptyList(), 0.0);
		}
	}

	private double calculateTotalPrice(List<CartItem> cartItems) {
		return cartItems.stream().mapToDouble(item -> item.getProduct().getProductFee() * item.getQuantity()).sum();
	}

	private ArrayList<CartItemDto> bindCartItemData(List<CartItem> cartItem) {
		ArrayList<CartItemDto> cartItemList = new ArrayList<CartItemDto>();
		if (cartItem.size() != 0) {
			cartItem.forEach(action -> {
				CartItemDto cartItemDto = new CartItemDto();
				cartItemDto.setActualPrice(action.getProduct().getProductFee());
				cartItemDto.setCartUUid(action.getCartUuid());
				cartItemDto.setPrice(action.getProduct().getProductFee() * action.getQuantity());
				cartItemDto.setProductImg(action.getProduct().getImg1());
				cartItemDto.setProductName(action.getProduct().getProductName());
				cartItemDto.setQunatity(action.getQuantity());
				cartItemDto.setProductId(action.getProduct().getP_id());
				cartItemList.add(cartItemDto);
			});
		}
		return cartItemList;
	}

	@Override
	public void removeCart(String cartId) {
		orderManageDao.deleteCartItemById(cartId);
	}

	@Override
	public ResultDTO createOrder(CreateOrderDTO order) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			Orders orderEntity = new Orders();

			DeliveryAddress address = commonDao.getAddressBasedOnId(order.getShippingAddressId());

			if (address == null) {
				return new ResultDTO(StatusCode.NOT_FOUND.getCode(), "Address not found", null);
			}

			orderEntity.setAddress(address);
			orderEntity.setOrderDate(new Date());
			orderEntity.setOrderStatus("Pending");
			UUID uuid = UUID.randomUUID();
			orderEntity.setOrderUuid(uuid.toString());
			User user = userService.getUser(order.getUserId());

			if (user == null) {
				return new ResultDTO(StatusCode.NOT_FOUND.getCode(), "User not found", null);
			}

			orderEntity.setUser(user);

			session.save(orderEntity);
			transaction.commit();

			return bindOrderItems(order, orderEntity);
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
			return new ResultDTO(StatusCode.INTERNAL_SERVER_ERROR.getCode(), "An error occurred", null);
		} finally {
			session.close();
		}
	}

	private ResultDTO bindOrderItems(CreateOrderDTO order, Orders orderEntity) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();
			List<OrderItems> orderItemsList = new ArrayList<>();

			for (OrderItemDTO action : order.getOrderItems()) {
				Product product = productService.findProductById(action.getProductId().intValue());

				if (product == null) {
					return new ResultDTO(StatusCode.NOT_FOUND.getCode(), "Product not found", null);
				}

				OrderItems orderItem = new OrderItems();
				orderItem.setProductId(action.getProductId());
				if (product.getUserId() != 0) {
					orderItem.setProductUserId(product.getUserId());
				}
				orderItem.setQuantity(action.getQuantity());
				orderItem.setSubtotal(product.getProductFee() * action.getQuantity());
				orderItem.setOrderItemStatus("Pending");
				orderItem.setOrder(orderEntity);
				UUID uuid = UUID.randomUUID();

				orderItem.setOrderItemUuid(uuid.toString());
				if (product != null)
					session.save(orderItem);
				orderItemsList.add(orderItem);
			}
			// orderEntity.setOrderItems(orderItemsList);
			transaction.commit();
			return new ResultDTO(StatusCode.SUCCESS.getCode(), "Order created successfully",
					bindOrderResponse(orderEntity));
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
			return new ResultDTO(StatusCode.INTERNAL_SERVER_ERROR.getCode(), "An error occurred", null);
		} finally {
			session.close();
		}
	}

	private OrderResponseDto bindOrderResponse(Orders order) {
		OrderResponseDto orderResponse = new OrderResponseDto();
		orderResponse.setOrderUuid(order.getOrderUuid());
		orderResponse.setTotalPrice(orderManageDao.getOrderTotalPrice(order.getOrderId()));
		return orderResponse;

	}

	@Override
	public ArrayList<OrderResponseDtoNew> getDukanOrder(Integer userId) {
		ArrayList<OrderResponseDtoNew> dukanOrderList = new ArrayList<OrderResponseDtoNew>();
		try {
			Map<String, ArrayList<OrderItems>> orderItemMap = new LinkedHashMap<>();
			List<OrderItems> orderItemList = orderManageDao.getOrderItemsByUserIdAndOrderDate(userId);
			for (OrderItems orderItem : orderItemList) {
				String orderUuid = orderItem.getOrder().getOrderUuid();

				// Check if the order UUID already exists in the map
				if (orderItemMap.containsKey(orderUuid)) {
					// If it exists, add the order item to the existing list
					orderItemMap.get(orderUuid).add(orderItem);
				} else {
					// If it doesn't exist, create a new list and add the order item to it
					ArrayList<OrderItems> itemList = new ArrayList<>();
					itemList.add(orderItem);
					orderItemMap.put(orderUuid, itemList);
				}
			}

			for (Map.Entry<String, ArrayList<OrderItems>> entry : orderItemMap.entrySet()) {
				String orderUuid = entry.getKey();
				OrderResponseDtoNew dukanOrder = new OrderResponseDtoNew();
				dukanOrder.setOrderUUid(orderUuid);
				Orders orders = orderManageDao.getOrderDetailsByOrderUuid(orderUuid);
				// System.out.println("Delivery Addres Is ::"+orders.getAddress());
				if (orders != null && orders.getAddress() != null)
					dukanOrder.setDeliveryAddress(orders.getAddress());
				ArrayList<OrderItems> orderItemsList = entry.getValue();

				// Now you can use orderUuid and orderItemsList as needed
				double totalPrice = 0.0;
				System.out.println("Order UUID: " + orderUuid);
				ArrayList<OrderItemDTO> itemList = new ArrayList<OrderItemDTO>();
				for (OrderItems orderItem : orderItemsList) {
					// Process each order item in the list
					totalPrice += orderItem.getSubtotal();
					OrderItemDTO orderItemDto = new OrderItemDTO();
					orderItemDto.setProductId(orderItem.getProductId());
					Product product = productService.findProductById((int) orderItem.getProductId());
					orderItemDto.setOrderUUid(orderItem.getOrderItemUuid());
					orderItemDto.setStatus(orderItem.getOrderItemStatus());
					if (product != null) {
						orderItemDto.setProductName(product.getProductName());
						orderItemDto.setPrice(orderItem.getSubtotal());
					}
					orderItemDto.setQuantity(orderItem.getQuantity());
					orderItemDto.setStatus(orderItem.getOrderItemStatus());
					itemList.add(orderItemDto);
				}
				dukanOrder.setItemList(itemList);
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				dukanOrder.setOderDate(dateFormat.format(orders.getOrderDate()));
				dukanOrder.setTotalPrice(totalPrice);
				dukanOrderList.add(dukanOrder);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dukanOrderList;
	}

	
	
	@Override
	public ArrayList<OrderResponseDtoNew> getCustomerOrderList(Integer userId) {
		ArrayList<OrderResponseDtoNew> dukanOrderList = new ArrayList<OrderResponseDtoNew>();
		try {
			List<Orders> ordersList = orderManageDao.getOrderByUserIdAndOrderDate(userId);
			System.out.println("List is ::"+ordersList.size());
			ordersList.forEach(action -> {
				OrderResponseDtoNew orderData = new OrderResponseDtoNew();
				orderData.setOrderUUid(action.getOrderUuid());
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				orderData.setOderDate(dateFormat.format(action.getOrderDate()));
				if (action.getAddress() != null)
					orderData.setDeliveryAddress(action.getAddress());
				double totalPrice = 0.0;
				ArrayList<OrderItemDTO> orderItemsResponseList=new ArrayList<OrderItemDTO>();
				for (OrderItems orderItems : orderManageDao.getOrderItemsByOrderId(action.getOrderId())) {

					totalPrice += orderItems.getSubtotal();
					OrderItemDTO orderItemDto = new OrderItemDTO();
					orderItemDto.setProductId(orderItems.getProductId());
					orderItemDto.setOrderUUid(orderItems.getOrderItemUuid());
					orderItemDto.setQuantity(orderItems.getQuantity());
					orderItemDto.setStatus(orderItems.getOrderItemStatus());
					Product product = productService.findProductById((int) orderItems.getProductId());
					System.out.println(":::::::::::");
					if (product != null) {
						orderItemDto.setProductName(product.getProductName());
						orderItemDto.setPrice(orderItems.getSubtotal());
					}
					orderItemsResponseList.add(orderItemDto);

				}
				orderData.setTotalPrice(totalPrice);
				orderData.setItemList(orderItemsResponseList);
				dukanOrderList.add(orderData);
			});
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dukanOrderList;
	}
	
	@Override
	public ArrayList<OrderResponseDtoNew> getOrderDetails(String orderUUid) {
		ArrayList<OrderResponseDtoNew> dukanOrderList = new ArrayList<OrderResponseDtoNew>();
		try {
			Orders action = orderManageDao.getOrderDetailsByOrderUuid(orderUUid);
		
			if(action!=null) {
				OrderResponseDtoNew orderData = new OrderResponseDtoNew();
				orderData.setOrderUUid(action.getOrderUuid());
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				orderData.setOderDate(dateFormat.format(action.getOrderDate()));
				if (action.getAddress() != null)
					orderData.setDeliveryAddress(action.getAddress());
				double totalPrice = 0.0;
				ArrayList<OrderItemDTO> orderItemsResponseList=new ArrayList<OrderItemDTO>();
				for (OrderItems orderItems : orderManageDao.getOrderItemsByOrderId(action.getOrderId())) {

					totalPrice += orderItems.getSubtotal();
					OrderItemDTO orderItemDto = new OrderItemDTO();
					orderItemDto.setProductId(orderItems.getProductId());
					orderItemDto.setOrderUUid(orderItems.getOrderItemUuid());
					orderItemDto.setQuantity(orderItems.getQuantity());
					orderItemDto.setStatus(orderItems.getOrderItemStatus());
					Product product = productService.findProductById((int) orderItems.getProductId());
					System.out.println(":::::::::::");
					if (product != null) {
						orderItemDto.setProductName(product.getProductName());
						orderItemDto.setPrice(orderItems.getSubtotal());
					}
					orderItemsResponseList.add(orderItemDto);

				}
				orderData.setTotalPrice(totalPrice);
				orderData.setItemList(orderItemsResponseList);
				dukanOrderList.add(orderData);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return dukanOrderList;
	}
	
	@Override
	public boolean acceptOrderItemFromDukan(String orderItemUUid)
	{
		try {
			List<OrderItems> orderItemList=orderManageDao.getOrderItemsbasedOnOrderUUid(orderItemUUid);
			if(orderItemList.size()!=0) {
				OrderItems orderItem=orderItemList.get(0);
				orderManageDao.updateOrderStatus(orderItemUUid, "Accept");
				return true;
			}
			return false;
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	@Override
	public boolean rejectOrderItemFromDukan(String orderItemUUid)
	{
		try {
			List<OrderItems> orderItemList=orderManageDao.getOrderItemsbasedOnOrderUUid(orderItemUUid);
			if(orderItemList.size()!=0) {
			List<OrderItems>	orderItemListDb=orderManageDao.getOrderItemsByOrderId(orderItemList.get(0).getOrder().getOrderId());

			orderItemListDb.forEach(action->{
				orderManageDao.updateOrderStatus(action.getOrderItemUuid(), "Reject");
			});
				
				return true;
			}
			return false;
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	@Override
	public boolean cancelOrder(String orderUUid)
	{
		try {
			
			Orders order=orderManageDao.getOrderDetailsByOrderUuid(orderUUid);
			
            if(order!=null) {
            	
            	order.getOrderItems().forEach(action->{
            		orderManageDao.updateOrderStatus(action.getOrderItemUuid(), "Cancel");
            	});
            	return true;
            }
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

}
