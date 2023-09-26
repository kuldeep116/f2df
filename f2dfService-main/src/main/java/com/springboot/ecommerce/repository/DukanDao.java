package com.springboot.ecommerce.repository;

import java.util.List;

import com.springboot.ecommerce.model.MereDukan;

public interface DukanDao {

	public int createDukan(MereDukan mereDukan);

	public int checkDukanExist(String mereDukanLink);

	public List<MereDukan> getAllDukan(int pageNo, int size);

	public MereDukan getDukan(int mereDukanId);
   
}
