package com.springboot.ecommerce.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.springboot.ecommerce.pojo.AddToCartRequest;
import com.springboot.ecommerce.pojo.CartResponse;
import com.springboot.ecommerce.pojo.CreateOrderDTO;
import com.springboot.ecommerce.pojo.OrderResponseDtoNew;
import com.springboot.ecommerce.pojo.ResultDTO;
import com.springboot.ecommerce.pojo.StatusCode;
import com.springboot.ecommerce.service.CartService;
import com.springboot.ecommerce.service.SiteMapService;

@RestController
@RequestMapping("/api/orders")
public class OrderControllerNew {

	@Autowired
	private CartService cartService;

	@Autowired
	private SiteMapService siteMapService;

	@PostMapping("/create")
	public ResponseEntity<ResultDTO> createOrder(@RequestBody CreateOrderDTO order) {
		ResultDTO result = cartService.createOrder(order);

		if (result.getStatusCode() == StatusCode.SUCCESS.getCode()) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
		}
	}

	@PostMapping("/getVenderOrder")
	public ResponseEntity<Object> getDukanOrder(@RequestBody CreateOrderDTO order) {

		return generateResponse("Vender Order List", cartService.getDukanOrder(order.getUserId()), HttpStatus.OK);

	}

	@PostMapping("/getCustomerOrder")
	public ResponseEntity<Object> getCustomerOrders(@RequestBody CreateOrderDTO order) {

		return generateResponse("Customer Order List", cartService.getCustomerOrderList(order.getUserId()),
				HttpStatus.OK);

	}
	
	@GetMapping("/getOrderDetails/{orderUUid}")
	public ResponseEntity<Object> getCustomerOrderDetails(@PathVariable("orderUUid") String orderUUid) {

		ArrayList<OrderResponseDtoNew> orders=cartService.getOrderDetails(orderUUid);
		if(orders.size()!=0) {
			return generateResponse("Customer Order Details", orders,
					HttpStatus.OK);
		}
		
		return generateResponse("Customer Order Not Found ", orders,
				HttpStatus.OK);

	}

	@GetMapping("/accept-dukan-order/{orderItemId}")
	public ResponseEntity<Object> acceptOrderFromDukan(@PathVariable("orderItemId") String orderItemId) {
		boolean status = cartService.acceptOrderItemFromDukan(orderItemId);
		if (status)
			return generateResponse("Order Status Updated Succesfully :Accept", status, HttpStatus.OK);
		else
			return generateResponse("Order No Found", status, HttpStatus.OK);
	}

	@GetMapping("/reject-dukan-order/{orderItemId}")
	public ResponseEntity<Object> rejectOrderFromDukan(@PathVariable("orderItemId") String orderItemId) {

		boolean status = cartService.rejectOrderItemFromDukan(orderItemId);
		if (status)
			return generateResponse("Order Status Updated Succesfully :Reject", status, HttpStatus.OK);
		else
			return generateResponse("Order No Found", status, HttpStatus.OK);

	}

	@GetMapping("/cancel-user-order/{orderUuid}")
	public ResponseEntity<Object> cancelOrderFromuser(@PathVariable("orderUuid") String orderUuid) {

		boolean status = cartService.cancelOrder(orderUuid);
		if (status)
			return generateResponse("Order Cancel Successfully ", status, HttpStatus.OK);
		else
			return generateResponse("Order No Found", status, HttpStatus.OK);

	}

	@GetMapping("/generate-sitemap")
	public ResponseEntity<ResultDTO> generateSiteMap() {
        siteMapService.generateSiteMap();
		return ResponseEntity.ok(null);
	}

	public static ResponseEntity<Object> generateResponse(String message, Object responseObj, HttpStatus status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}

}
