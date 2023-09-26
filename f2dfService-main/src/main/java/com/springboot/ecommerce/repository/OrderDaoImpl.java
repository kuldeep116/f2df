package com.springboot.ecommerce.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.ecommerce.model.OrderDetail;

@Repository
@Transactional
public class OrderDaoImpl implements OrderDao {
	
	@Autowired
	SessionFactory sessionFactory;
	
	private static Logger logger = Logger.getLogger(OrderDaoImpl.class.getName());

	public void create(OrderDetail req) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.save(req);
		} catch (Exception e) {
			logger.debug("Error occured during add order " + e.getMessage());
		}
	}
	
	@Override
	public List<OrderDetail> getOrderByUser(long userId,String status) {
		Session session = sessionFactory.getCurrentSession();
		List<OrderDetail> list = new ArrayList<>();
		if(status != null && status != "") {
			list = session.createQuery("from OrderDetail t where t.userId =:userId and t.status =:status")
					.setParameter("userId", userId).setParameter("status", status).list();
		}else {
		list = session.createQuery("from OrderDetail t where t.userId =:userId ")
					.setParameter("userId", userId).list();
		}
		return list;
	}
	@Override
	public List<OrderDetail> getOrderByProductUser(long userId,String status) {
		Session session = sessionFactory.getCurrentSession();
		List<OrderDetail> list = new ArrayList<>();
		if(status != null && status != "") {
		list = session.createQuery("from OrderDetail t where t.productUserId =:userId and t.status =:status ")
					.setParameter("userId", userId).setParameter("status", status).list();
		}else {
		list = session.createQuery("from OrderDetail t where t.userId =:userId ")
					.setParameter("userId", userId).list();
		}
		return list;
	}
	
	@Override
	public void updateStatusOfOrderByUser(int userId,String status) {
		Session session = sessionFactory.getCurrentSession();
			session.createQuery(
					"update com.springboot.ecommerce.model.OrderDetail a  set a.status =:status where a.id =:id")
					.setParameter("status", status).setParameter("id", userId).executeUpdate();
	}
	
	@Override
	public void updateStatusOfOrderByProductUser(int userId,String status) {
		Session session = sessionFactory.getCurrentSession();
			session.createQuery(
					"update com.springboot.ecommerce.model.OrderDetail a  set a.status =:status where a.id =:id")
					.setParameter("status", status).setParameter("id", userId).executeUpdate();
	}
	@Override
	public void deleteOrder(int id) {
		Session session = sessionFactory.getCurrentSession();
		OrderDetail order=new OrderDetail();
		order.setId(id);
		session.delete(order);
	}
}
