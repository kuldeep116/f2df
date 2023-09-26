package com.springboot.ecommerce.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.springboot.ecommerce.pojo.RazorPayClientConfig;
import com.springboot.ecommerce.pojo.RzrOrderRequest;
import com.springboot.ecommerce.pojo.RzrOrderResponse;
import com.springboot.ecommerce.pojo.RzrPaymentResponse;
import com.springboot.ecommerce.service.OrderService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/rzr")
@Tag(name = "Payment gateway API")
public class PaymentController {
 
    private RazorpayClient client;
 
    private RazorPayClientConfig razorPayClientConfig;
 
    @Autowired
    private OrderService orderService;
 
    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestBody RzrOrderRequest orderRequest) throws RazorpayException {
        RzrOrderResponse razorPay = null;
        this.client = new RazorpayClient("rzp_live_MmKm1tIl8HM05R", "NwjedeYNrjiMUZVRFXwyxDvX");
        try {
            // The transaction amount is expressed in the currency subunit, such
            // as paise (in case of INR)
            String amountInPaise = convertRupeeToPaise(orderRequest.getAmount());
            // Create an order in RazorPay and get the order id
            Order order = createRazorPayOrder(amountInPaise);
            razorPay = getOrderResponse((String) order.get("id"), amountInPaise);
            // Save order in the database
            orderService.saveOrder(razorPay.getRazorpayOrderId(), orderRequest.getUserId());
        } catch (RazorpayException e) {
        	return generateResponse("Error while create payment order: ", "", HttpStatus.BAD_REQUEST);
        }
        return generateResponse("succesffully create payment order: ", razorPay, HttpStatus.OK);
    }
 
    @PutMapping("/order")
    public ResponseEntity<Object> updateOrder(@RequestBody RzrPaymentResponse paymentResponse) {
        String errorMsg = orderService.validateAndUpdateOrder(paymentResponse.getRazorpayOrderId(), paymentResponse.getRazorpayPaymentId(), paymentResponse.getRazorpaySignature(),
                razorPayClientConfig.getSecret());
        if (errorMsg != null) {
            return generateResponse("Error while updateOrder payment order: ", errorMsg, HttpStatus.BAD_REQUEST);
        }
        return generateResponse("succesffully create payment order: ", paymentResponse.getRazorpayPaymentId(), HttpStatus.OK);
    }
 
    private RzrOrderResponse getOrderResponse(String orderId, String amountInPaise) {
        RzrOrderResponse razorPay = new RzrOrderResponse();
        razorPay.setApplicationFee(amountInPaise);
        razorPay.setRazorpayOrderId(orderId);
        razorPay.setSecretKey(razorPayClientConfig.getKey());
        return razorPay;
    }
 
    private Order createRazorPayOrder(String amount) throws RazorpayException {
        org.json.JSONObject options = new org.json.JSONObject();
        options.put("amount", amount);
        options.put("currency", "INR");
        options.put("receipt", "f2df_"+new Date().getTime());
        return client.Orders.create(options);
    }
 
    private String convertRupeeToPaise(String paise) {
        BigDecimal b = new BigDecimal(paise);
        BigDecimal value = b.multiply(new BigDecimal("100"));
        return value.setScale(0, RoundingMode.UP).toString();
    }
    
    public static ResponseEntity<Object> generateResponse(String message, Object responseObj, HttpStatus status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}
}