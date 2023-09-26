package com.springboot.ecommerce.repository;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springboot.ecommerce.model.MereDukan;

@Repository
public class DukanDaoImpl implements DukanDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public int createDukan(MereDukan mereDukan) {
		Session session = sessionFactory.getCurrentSession();
		if(mereDukan.getId() > 0){
			session.update(mereDukan);
			return mereDukan.getId();
		}
		session.save(mereDukan);
		return mereDukan.getId();
	}

	@Override
	public int checkDukanExist(String mereDukanLink) {
		Session session = sessionFactory.getCurrentSession();
		try {
			MereDukan mereDukan = (MereDukan) session
					.createQuery("from com.springboot.ecommerce.model.MereDukan m where m.dukanLink=: dukanLink ")
					.setParameter("dukanLink", mereDukanLink).getSingleResult();
			return mereDukan.getId();
		} catch (NoResultException nre) {
			return 0;
			// Code for handling NoResultException
		} catch (NonUniqueResultException nure) {
			return 0;
			// Code for handling NonUniqueResultException
		}

	}

	@Override
	public List<MereDukan> getAllDukan(int pageNo, int size) {
		Session session = sessionFactory.getCurrentSession();
		// TODO Auto-generated method stub
		return session.createQuery("from com.springboot.ecommerce.model.MereDukan p ORDER BY p.id desc ")
				.setFirstResult(pageNo).setMaxResults(size).list();
	}

	@Override
	public MereDukan getDukan(int mereDukanId) {
		Session session = sessionFactory.getCurrentSession();
		MereDukan mereDukan = new MereDukan();
		try {
			 mereDukan = (MereDukan) session
					.createQuery("from com.springboot.ecommerce.model.MereDukan m where m.userId=: userId ")
					.setParameter("userId", mereDukanId).getSingleResult();
			return mereDukan;
		} catch (NoResultException nre) {
			// Code for handling NoResultException
		} catch (NonUniqueResultException nure) {
			// Code for handling NonUniqueResultException
		}
		return mereDukan;
	}

}
