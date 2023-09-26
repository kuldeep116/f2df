package com.springboot.ecommerce.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.ecommerce.model.AddSpace;
import com.springboot.ecommerce.model.Banner;
import com.springboot.ecommerce.model.ContactUs;
import com.springboot.ecommerce.model.Insurance;
import com.springboot.ecommerce.model.Loan;
import com.springboot.ecommerce.model.Training;
import com.springboot.ecommerce.pojo.CatExportQunatity;
import com.springboot.ecommerce.repository.HomeDao;

@Service
@Transactional
public class HomeServiceImpl implements HomeService {

	@Autowired
	HomeDao homeDao;

	@Override
	public int createTraining(Training training) {
		return homeDao.addTraining(training);
	}

	@Override
	public int createLoan(Loan loan) {
		return homeDao.createLoan(loan);
	}

	@Override
	public Object getNavigation() {
		// TODO Auto-generated method stub
		return homeDao.getNavigation();
	}

	@Override
	public Object getBanner(String type) {
		Object obj = homeDao.getBanner(type);
		if (!ObjectUtils.isEmpty(obj)) {
			return obj;
		} else {
			return homeDao.getBanner("Home");
		}
	}

	@Override
	public Object getBannerByCatId(int pc_id) {
		// TODO Auto-generated method stub
		Object obj = homeDao.getBannerByCatId(pc_id);
		if (!ObjectUtils.isEmpty(obj)) {
			return obj;
		} else {
			return homeDao.getBanner("Home");
		}
	}

	@Override
	public Object getBannerBySubCatId(int psc_id) {
		// TODO Auto-generated method stub
		Object obj = homeDao.getBannerBySubCatId(psc_id);
		if (!ObjectUtils.isEmpty(obj)) {
			return obj;
		} else {
			return homeDao.getBannerByCatId(homeDao.getCategoryBySubCat(psc_id));
		}

	}

	@Override
	public Object getTrainingType() {
		// TODO Auto-generated method stub
		return homeDao.getTrainingType();
	}

	@Override
	public AddSpace getAddSpace() {
		// TODO Auto-generated method stub
		return homeDao.getAddSpace();
	}

	@Override
	public int createInsurance(Insurance insurance) {
		return homeDao.addInsuranceLead(insurance);
	}

	@Override
	public Object getTestimonials() {
		// TODO Auto-generated method stub
		return homeDao.getTestimonials();
	}

	@Override
	public List<CatExportQunatity> exportSubCatQuantity() {
		// TODO Auto-generated method stub
		return homeDao.exportSubCatQuantity();
	}

	@Override
	public int addBanner(Banner req) {
		return homeDao.addBanner(req);
	}

	@Override
	public void deleteBanner(int id) {
		homeDao.deleteBanner(id);
	}

	@Override
	public int updateBanner(Banner req) {
		// TODO Auto-generated method stub
		return updateBanner(req);
	}

	@Override
	public Object getBannerLocation() {
		return homeDao.getBannerLocation();
	}

	@Override
	public int createContactUs(ContactUs loan) {
		// TODO Auto-generated method stub
		return homeDao.createContactUs(loan);
	}

	@Override
	public List<Training> getTraining(int page, int size) {
		// TODO Auto-generated method stub
		return homeDao.getTraining(page, size);
	}

	@Override
	public List<Insurance> getInsurance(int page, int size) {
		// TODO Auto-generated method stub
		return homeDao.getInsurance(page, size);
	}

	@Override
	public List<Loan> getLoan(int page, int size) {
		// TODO Auto-generated method stub
		return homeDao.getLoan(page, size);
	}

	@Override
	public int countLoan() {
		// TODO Auto-generated method stub
		return homeDao.countLoan();
	}

	@Override
	public int countInsurance() {
		// TODO Auto-generated method stub
		return homeDao.countInsurance();
	}

	@Override
	public int countTraining() {
		// TODO Auto-generated method stub
		return homeDao.countTraining();
	}

	@Override
	public int countAdBanner(String type) {
		// TODO Auto-generated method stub
		return homeDao.countAdBanner(type);
	}

}
