package com.springboot.ecommerce.controller;

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
import com.springboot.ecommerce.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@PostMapping("/add")
	public ResponseEntity<Object> addToCart(@RequestBody AddToCartRequest addToCartRequest) {

		try {
			cartService.addtoCart(addToCartRequest);
			// Return a custom response with a success message and status
			return generateResponse("Item added to the cart", null, HttpStatus.OK);
		} catch (ResponseStatusException e) {
			// Catch exceptions thrown by the service and return the corresponding HTTP
			// status code
			return generateResponse(e.getReason(), null, e.getStatus());
		}
	}
	
	@GetMapping("/list/{userId}")
    public ResponseEntity<Object> getAllCartItemsByUser(@PathVariable Integer userId) {
        try {
           
            CartResponse cartResponse = cartService.getAllCartItemsByUser(userId);
            return generateResponse("Cart List", cartResponse, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
	
	@DeleteMapping("delete/{cartItemUuid}")
    public ResponseEntity<Object> deleteCartItemById(@PathVariable String cartItemUuid) {
        try {
        	cartService.removeCart(cartItemUuid);
            return generateResponse("Cart Deleted Successfully", null, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            // Handle the case where the CartItem with the given ID is not found
            return generateResponse(e.getReason(), null, e.getStatus());
        }
    }

	public static ResponseEntity<Object> generateResponse(String message, Object responseObj, HttpStatus status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("status", status.value());
		map.put("data", responseObj);

		return new ResponseEntity<Object>(map, status);
	}
	
	@PostMapping("/increase")
	public ResponseEntity<Object> increaseQuantity(@RequestBody AddToCartRequest request) {
	    try {
	        cartService.addToCartOrUpdateQuantity(request.getProductId(), request.getUserId(), request.getQuantityToAdd());
	        return generateResponse("Quantity increased in the cart", null, HttpStatus.OK);
	    } catch (ResponseStatusException e) {
	        return generateResponse(e.getReason(), null, e.getStatus());
	    }
	}

	@PostMapping("/decrease")
	public ResponseEntity<Object> decreaseQuantity(@RequestBody AddToCartRequest request) {
	    try {
	        // Ensure that the quantity to decrease is not greater than the existing quantity
	        if (request.getQuantityToDecrease() > 0) {
	            cartService.addToCartOrUpdateQuantity(request.getProductId(), request.getUserId(), -request.getQuantityToDecrease());
	            return generateResponse("Quantity decreased in the cart", null, HttpStatus.OK);
	        } else {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid quantity to decrease");
	        }
	    } catch (ResponseStatusException e) {
	        return generateResponse(e.getReason(), null, e.getStatus());
	    }
	}

}
