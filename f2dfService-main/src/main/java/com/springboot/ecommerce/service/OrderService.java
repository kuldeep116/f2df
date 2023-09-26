package com.springboot.ecommerce.service;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springboot.ecommerce.model.RzrOrder;
import com.springboot.ecommerce.util.Signature;

@Repository
@Transactional
public class OrderService {
	
	@Autowired
	SessionFactory sessionFactory;
 
 
    public RzrOrder saveOrder(final String razorpayOrderId, final int userId) {
    	Session session = sessionFactory.getCurrentSession();
        RzrOrder order = new RzrOrder();
        order.setRazorpayOrderId(razorpayOrderId);
        order.setUserId(userId);
        return (RzrOrder) session.save(order);
    }
 
    public String validateAndUpdateOrder(final String razorpayOrderId, final String razorpayPaymentId, final String razorpaySignature, final String secret) {
        String errorMsg = null;
        Session session = sessionFactory.getCurrentSession();
        try {
            RzrOrder order =findByRazorpayOrderId(razorpayOrderId);
            // Verify if the razorpay signature matches the generated one to
            // confirm the authenticity of the details returned
            String generatedSignature = Signature.calculateRFC2104HMAC(order.getRazorpayOrderId() + "|" + razorpayPaymentId, secret);
            if (generatedSignature.equals(razorpaySignature)) {
                order.setRazorpayOrderId(razorpayOrderId);
                order.setRazorpayPaymentId(razorpayPaymentId);
                order.setRazorpaySignature(razorpaySignature);
                session.update(order);
            } else {
                errorMsg = "Payment validation failed: Signature doesn't match";
            }
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }
        return errorMsg;
    }
    public RzrOrder findByRazorpayOrderId(String razorpayOrderId) {
		Session session = sessionFactory.getCurrentSession();
		RzrOrder obj = new RzrOrder();
		try {
			return obj = (RzrOrder) session
					.createQuery(
							"from com.springboot.ecommerce.model.RzrOrder obj where obj.razorpayOrderId =:razorpayOrderId")
					.setParameter("razorpayOrderId", razorpayOrderId).getSingleResult();
		} catch (NoResultException nre) {

			return obj;// Code for handling NoResultException
		} catch (NonUniqueResultException nure) {
			// Code for handling NonUniqueResultException
			return obj;
		}

	}
}