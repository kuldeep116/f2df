package com.springboot.ecommerce.service;

import java.util.List;

import com.springboot.ecommerce.model.MereDukan;

public interface DukanService {
   
	public int createDukan(MereDukan mereDukan);

	public int checkDukanExist(String mereDukanLink);

	public List<MereDukan> getAllDukan(int pageNo, int size);

	public MereDukan getDukan(int mereDukanId);
    
}
