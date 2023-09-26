package com.springboot.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.ecommerce.model.MereDukan;
import com.springboot.ecommerce.repository.DukanDao;

@Service
@Transactional
public class DukanServiceImpl implements DukanService{

    @Autowired
    DukanDao dukanDao;

	@Override
	public int createDukan(MereDukan mereDukan) {
		// TODO Auto-generated method stub
		return dukanDao.createDukan(mereDukan);
	}

	@Override
	public int checkDukanExist(String mereDukanLink) {
		// TODO Auto-generated method stub
		return dukanDao.checkDukanExist(mereDukanLink);
	}

	@Override
	public List<MereDukan> getAllDukan(int pageNo, int size) {
		// TODO Auto-generated method stub
		return dukanDao.getAllDukan(pageNo,size);
	}

	@Override
	public MereDukan getDukan(int mereDukanId) {
		// TODO Auto-generated method stub
		return dukanDao.getDukan(mereDukanId);
	}

	


}
