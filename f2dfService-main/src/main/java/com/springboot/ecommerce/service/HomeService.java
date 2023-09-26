package com.springboot.ecommerce.service;

import java.util.List;

import com.springboot.ecommerce.model.AddSpace;
import com.springboot.ecommerce.model.Banner;
import com.springboot.ecommerce.model.ContactUs;
import com.springboot.ecommerce.model.Insurance;
import com.springboot.ecommerce.model.Loan;
import com.springboot.ecommerce.model.Training;
import com.springboot.ecommerce.pojo.CatExportQunatity;

public interface HomeService {
    public int createTraining(Training training);
    public int createInsurance(Insurance insurance);
    public int createLoan(Loan loan);
    public int createContactUs(ContactUs loan);
	public Object getNavigation();
	public Object getBanner(String type);
	public Object getTrainingType();
	public AddSpace getAddSpace();
	public Object getTestimonials();
	public Object getBannerByCatId(int pc_id);
	Object getBannerBySubCatId(int psc_id);
	public List<CatExportQunatity> exportSubCatQuantity();
	public int addBanner(Banner req);
	void deleteBanner(int id);
	int updateBanner(Banner req);
	public Object getBannerLocation();
	public List<Training> getTraining(int page, int size);
	public List<Insurance> getInsurance(int page, int size);
	public List<Loan> getLoan(int page, int size);
	public int countLoan();
	public int countInsurance();
	public int countTraining();
	public int countAdBanner(String string);
}
