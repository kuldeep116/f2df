package com.springboot.ecommerce.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.ecommerce.model.Address;
import com.springboot.ecommerce.model.User;
import com.springboot.ecommerce.repository.ProductDaoImpl;
import com.springboot.ecommerce.repository.UserDao;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;
	

	private static Logger logger = Logger.getLogger(UserServiceImpl.class.getName());


	@Override
	public User userExistByEmail(String email) {
		return userDao.userExistByEmail(email);
	}

	@Override
	public User userExistByGoogle(String googleId) {
		return userDao.userExistByGoogle(googleId);
	}

	@Override
	public User getUserByMobile(long mobile) {
		return userDao.getUserByMobile(mobile);
	}

	@Override
	public User createUser(User user) {
		if (user.getId() != 0) {
			user = userDao.createUser(user);
			userDao.addressUpdate(user);
		} else {
			user = userDao.createUser(user);
			//Address add = user.getAddress();
			Address add = getAddress(user.getId());
			add.setUserId(user.getId());
			userDao.addressSave(add);
			//userDao.addressUpdate(user);
		}
		return user;
	}

	@Override
	public User login(String email, String password) {
		return userDao.login(email, password);
	}

	@Override
	public void updateProfile(User user) {
		userDao.createUser(user);
	}

	@Override
	public User getUser(int id) {
		// TODO Auto-generated method stub
		return userDao.getUser(id);
	}

	@Override
	public int addressSave(Address address) {
		// TODO Auto-generated method stub
		return userDao.addressSave(address);
	}

	@Override
	public Address getAddress(int userId) {
		// TODO Auto-generated method stub
		return userDao.getAddress(userId);
	}

	@Override
	public int countOfTotalUser() {
		// TODO Auto-generated method stub
		return userDao.countOfTotalUser();
	}

	@Override
	public int countOfTotalDukan() {
		return userDao.countOfTotalDukan();
	}

	@Override
	public int countOfBussEntityUser() {
		// TODO Auto-generated method stub
		return  userDao.countOfBussEntityUser();
	}

}
