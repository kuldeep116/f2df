package com.springboot.ecommerce.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.standard.expression.OrExpression;

import com.springboot.ecommerce.model.CartItem;
import com.springboot.ecommerce.model.OrderItems;
import com.springboot.ecommerce.model.Orders;
import com.springboot.ecommerce.pojo.OrderResponseDtoNew;

@Repository
@Transactional
public class OrderManageDaoImpl implements OrderManageDao {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public void updateCartQuantity(CartItem cartitem, int quantity) {
		String hql = "update com.springboot.ecommerce.model.CartItem ci set ci.quantity = :newQuantity where ci.user.id = :userId and ci.product.id = :productId";
		Session session = sessionFactory.getCurrentSession();
		try {
			session.createQuery(hql).setParameter("newQuantity", cartitem.getQuantity() + quantity)
					.setParameter("userId", cartitem.getUser().getId())
					.setParameter("productId", cartitem.getProduct().getP_id()).executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public void deleteCartItemById(String cartUuid) {
		try {
			Session session = sessionFactory.getCurrentSession();

			CartItem cartItemToDelete = getCartBasedOnUuid(cartUuid);

			if (cartItemToDelete != null) {
				session.delete(cartItemToDelete);
			} else {
				// Handle the case where the CartItem with the given ID is not found
				// For example, you can throw an exception or log a message.
				// Here, we are printing a message to the console.
				System.out.println("CartItem with ID " + cartUuid + " not found.");
			}
		} catch (Exception e) {
			// Handle exceptions
			e.printStackTrace();
		}
	}

	public CartItem getCartBasedOnUuid(String uuid) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "FROM CartItem ci WHERE ci.cartUuid = :uuid ";
		return session.createQuery(hql, CartItem.class).setParameter("uuid", uuid).uniqueResult();
	}

	@Override
	public CartItem getCartItemByProductAndUser(int productId, int userId) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "FROM CartItem ci WHERE ci.product.id = :productId AND ci.user.id = :userId";
		return session.createQuery(hql, CartItem.class).setParameter("productId", productId)
				.setParameter("userId", userId).uniqueResult();
	}

	@Override
	public double getOrderTotalPrice(long orderId) {
		Session session = sessionFactory.getCurrentSession();
		String jpql = "SELECT SUM(oi.subtotal) FROM OrderItems oi WHERE oi.order.orderId = :orderId";
		try {
			return (double) session.createQuery(jpql).setParameter("orderId", orderId).getSingleResult();
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public List<OrderItems> getOrderItemsByUserIdAndOrderDate(Integer userId) {
		Session session = sessionFactory.getCurrentSession();

		// Begin a transaction if necessary
		// session.beginTransaction();

		try {
			List<OrderItems> orderItems = session
					.createQuery("FROM OrderItems oi WHERE oi.productUserId = :userId ORDER BY oi.orderDate desc")
					.setParameter("userId", (long) userId).list();

			return orderItems;
		} catch (Exception e) {
			// Rollback the transaction if an exception occurs
			// session.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public List<OrderItems> getOrderItemsByOrderId(long orderId) {
		Session session = sessionFactory.getCurrentSession();

		// Begin a transaction if necessary
		// session.beginTransaction();

		try {
			List<OrderItems> orderItems = session
					.createQuery("FROM OrderItems oi WHERE oi.order.orderId = :orderId ORDER BY oi.orderDate desc")
					.setParameter("orderId", orderId).list();

			return orderItems;
		} catch (Exception e) {
			// Rollback the transaction if an exception occurs
			// session.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public List<OrderItems> getOrderItemsbasedOnOrderUUid(String orderItemUuid) {
		Session session = sessionFactory.getCurrentSession();

		// Begin a transaction if necessary
		// session.beginTransaction();

		try {
			List<OrderItems> orderItems = session
					.createQuery("FROM OrderItems oi WHERE oi.orderItemUuid = :orderItemUuid ORDER BY oi.orderDate desc")
					.setParameter("orderItemUuid", orderItemUuid).list();

			return orderItems;
		} catch (Exception e) {
			// Rollback the transaction if an exception occurs
			// session.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public List<OrderItems> getOrderItemsbasedOnOrderItem(String orderItemUuid) {
		Session session = sessionFactory.getCurrentSession();

		// Begin a transaction if necessary
		// session.beginTransaction();

		try {
			List<OrderItems> orderItems = session
					.createQuery("FROM OrderItems oi WHERE oi.orderItemUuid = :orderItemUuid")
					.setParameter("orderItemUuid", orderItemUuid).list();

			return orderItems;
		} catch (Exception e) {
			// Rollback the transaction if an exception occurs
			// session.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public void updateOrderStatus(String orderItemUUid,String status) {
		String hql = "update com.springboot.ecommerce.model.OrderItems oi set oi.orderItemStatus = :orderStatus where oi.orderItemUuid = :orderItemUuid";
		Session session = sessionFactory.getCurrentSession();
		try {
			session.createQuery(hql).setParameter("orderItemUuid", orderItemUUid)
					.setParameter("orderStatus", status)
					.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public List<Orders> getOrderByUserIdAndOrderDate(Integer userId) {
		Session session = sessionFactory.getCurrentSession();

		// Begin a transaction if necessary
		// session.beginTransaction();

			return session
					.createQuery("FROM Orders oi WHERE oi.user.id= :userId ORDER BY oi.orderDate desc")
					.setParameter("userId",  userId).list();

		
		
	}

	@Override
	public Orders getOrderDetailsByOrderUuid(String orderUuid) {
		try {
			Session session = sessionFactory.getCurrentSession();
			String hql = "FROM Orders o WHERE o.orderUuid = :orderUuid";
			Query<Orders> query = session.createQuery(hql, Orders.class);
			query.setParameter("orderUuid", orderUuid);
			return query.uniqueResult();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

}
