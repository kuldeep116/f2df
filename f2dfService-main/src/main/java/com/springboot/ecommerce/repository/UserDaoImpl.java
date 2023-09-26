package com.springboot.ecommerce.repository;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springboot.ecommerce.model.Address;
import com.springboot.ecommerce.model.User;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;
	private static Logger logger = Logger.getLogger(UserDaoImpl.class.getName());

	@Override
	public User userExistByEmail(String email) {
		Session session = sessionFactory.getCurrentSession();
		User user = new User();
		try {
			user = (User) session
					.createQuery("select u from com.springboot.ecommerce.model.User u where u.email =:email")
					.setParameter("email", email).getSingleResult();
			user.setAddress(getAddress(user.getId()));
		} catch (NoResultException nre) {
			// Code for handling NoResultException
		} catch (NonUniqueResultException nure) {
			// Code for handling NonUniqueResultException
		}
		return user;
	}

	@Override
	public User userExistByGoogle(String googleId) {
		Session session = sessionFactory.getCurrentSession();

		User user = new User();
		try {
			user = (User) session
					.createQuery("select u from com.springboot.ecommerce.model.User u where u.googleId =:googleId")
					.setParameter("googleId", googleId).getSingleResult();
			user.setAddress(getAddress(user.getId()));
		} catch (NoResultException nre) {
			// Code for handling NoResultException
		} catch (NonUniqueResultException nure) {
			// Code for handling NonUniqueResultException
		}
		return user;
	}

	@Override
	public User getUserByMobile(long mobile) {
		Session session = sessionFactory.getCurrentSession();

		User user = new User();
		try {
			user = (User) session.createQuery("from com.springboot.ecommerce.model.User  where mobile =:mobile")
					.setParameter("mobile", mobile).getSingleResult();
			user.setAddress(getAddress(user.getId()));
		} catch (NoResultException nre) {
			logger.debug("ERROR OCCURED DURING GET USER BY MOBILE " + nre);
		} catch (NonUniqueResultException nure) {
			logger.debug("ERROR OCCURED DURING GET USER BY MOBILE " + nure);
		}
		return user;
	}

	@Override
	public User createUser(User user) {
		try {
			Session session = sessionFactory.getCurrentSession();
			if (user.getId() != 0) {
				session.update(user);
			} else {
				session.save(user);
			}
		} catch (Exception e) {
			logger.debug("Error during save user in hibernate " + e.toString());
		}
		return user;
	}

	@Override
	public void addressUpdate(User user) {
		Session session = sessionFactory.getCurrentSession();
		Address address = user.getAddress();
		logger.debug("Goinng to update address " + address.toString());
		session.createQuery(
				"update com.springboot.ecommerce.model.Address a set a.address1 =:address1,a.address2 =:address2,a.city=:city,a.state =:state,a.country=:country,a.pincode=:pincode where a.id=:userId")
				.setParameter("address1", address.getAddress1()).setParameter("address2", address.getAddress2())
				.setParameter("city", address.getCity()).setParameter("state", address.getState())
				.setParameter("country", address.getCountry()).setParameter("pincode", address.getPincode())
				.setParameter("userId", address.getId()).executeUpdate();

	}

	@Override
	public int addressSave(Address address) {
		//logger.debug("Goinng to save address " + address.toString());
		Session session = sessionFactory.getCurrentSession();
		session.save(address);
		return address.getId();

	}

	@Override
	public User login(String email, String password) {
		Session session = sessionFactory.getCurrentSession();
		User user = (User) session
				.createQuery("from com.springboot.ecommerce.model.User u where u.email :email and u.password :password")
				.setParameter("email", email).setParameter("password", password).getSingleResult();
		user.setAddress(getAddress(user.getId()));
		return user;
	}

	@Override
	public void updateProfile(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.update(user);
	}

	@Override
	public User getUser(int id) {
		Session session = sessionFactory.getCurrentSession();
		User user = new User();
		try {
			user = (User) session.createQuery(" select u from com.springboot.ecommerce.model.User u where u.id =:id")
					.setParameter("id", id).getSingleResult();
			user.setAddress(getAddress(user.getId()));
		} catch (NoResultException nre) {
			// Code for handling NoResultException
		} catch (NonUniqueResultException nure) {
			// Code for handling NonUniqueResultException
		}
		return user;
	}

	@Override
	public Address getAddress(int userId) {
		Session session = sessionFactory.getCurrentSession();
		Address address = new Address();
		try {
			address = (Address) session
					.createQuery("select u from com.springboot.ecommerce.model.Address u where u.userId =:userId")
					.setParameter("userId", userId).getSingleResult();
		} catch (NoResultException nre) {
			address.setId(0);
			// Code for handling NoResultException
		} catch (NonUniqueResultException nure) {
			// Code for handling NonUniqueResultException
			address.setId(0);
		}
		return address;
	}

	@Override
	public int countOfTotalUser() {
		Session session = sessionFactory.getCurrentSession();
		// TODO Auto-generated method stub
		return session.createQuery(" select u from com.springboot.ecommerce.model.User u ").list().size();
	}

	@Override
	public int countOfTotalDukan() {
		Session session = sessionFactory.getCurrentSession();
		// TODO Auto-generated method stub
		return session.createQuery(" select u from com.springboot.ecommerce.model.MereDukan u ").list().size();
	}

	@Override
	public int countOfBussEntityUser() {
		Session session = sessionFactory.getCurrentSession();
		// TODO Auto-generated method stub
		return session
				.createQuery(" select u from com.springboot.ecommerce.model.User u where u.userType='BUSINESSENTITY'")
				.list().size();
	}

}
