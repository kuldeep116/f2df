package com.springboot.ecommerce.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.ecommerce.model.Events;
import com.springboot.ecommerce.model.Images;
import com.springboot.ecommerce.model.OrderDetail;
import com.springboot.ecommerce.pojo.RequestProduct;
import com.springboot.ecommerce.repository.OrderDao;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/order")
@Tag(name = "order API")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {

	@Autowired
	OrderDao dao;

	private static Logger logger = Logger.getLogger(PaymentController.class.getName());
	@PostMapping(value = "/create", headers = "Accept=application/json")
	public ResponseEntity<Object> createOrder(@RequestBody OrderDetail req) {
		try { 
		    req.setCreateDate(new Date());
		    req.setUpdateDate(new Date());
			dao.create(req);
			return generateResponse("Order save succesfully", "OK", HttpStatus.OK);
		} catch (Exception e) {
			logger.debug("Error occured i  add Order" +e.toString());
			return generateResponse("Order fail to save ", 0, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/getOrderByUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getOrderByUser(@RequestBody OrderDetail req) {
		List<OrderDetail> obj = dao.getOrderByUser(req.getUserId(),req.getStatus());
		if (obj == null) {
			return generateResponse(" Order by id ", "", HttpStatus.NOT_FOUND);
		}
		return generateResponse("Order by userid ", obj, HttpStatus.OK);
	}
	@PostMapping(value = "/getOrderByProductUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getOrderByProductUser(@RequestBody OrderDetail req) {
		List<OrderDetail> obj = dao.getOrderByProductUser(req.getProductUserId(),req.getStatus());
		if (obj == null) {
			return generateResponse(" Order by product user id ", "", HttpStatus.NOT_FOUND);
		}
		return generateResponse("Order by product userid ", obj, HttpStatus.OK);
	}
	
	@PostMapping(value = "/updateOrderByProductUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateOrderByProductUser(@RequestBody OrderDetail req) {
		dao.updateStatusOfOrderByProductUser(req.getId(),req.getStatus());
		return generateResponse("Order updated by product userid ", req, HttpStatus.OK);
	}
	
	@PostMapping(value = "/updateOrderByUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateOrderByUser(@RequestBody OrderDetail req) {
		dao.updateStatusOfOrderByUser(req.getId(),req.getStatus());
		return generateResponse("Order updated by user ", req, HttpStatus.OK);
	}
	@PostMapping(value = "/getOrderStatus", headers = "Accept=application/json")
	public ResponseEntity<Object> getOrderStatus() {
		Map<String, String> map = new HashMap<>();
		map.put("Pending", "Pending");
		map.put("Accept", "Accept");
		map.put("Packed", "Packed");
		map.put("Delivered", "Delivered");
		map.put("Recieved", "Recieved");
		return generateResponse("Order Status succesfully", map, HttpStatus.OK);
	}
	
	public static ResponseEntity<Object> generateResponse(String message, Object responseObj, HttpStatus status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}

}
