package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.model.Address;
import com.springboot.ecommerce.model.User;

public interface UserDao {
    public User userExistByEmail(String email);
    public User userExistByGoogle(String googleId);
    public User getUserByMobile(long mobile);
    public User createUser(User user);
	public User login(String email,String password);
	//public void signUp(User user);
	public void updateProfile(User user);
	public User getUser(int id);
	int addressSave(Address address);
	Address getAddress(int userId);
	public int countOfTotalUser();
	public int countOfTotalDukan();
	public void addressUpdate(User user);
	public int countOfBussEntityUser();
    
}
