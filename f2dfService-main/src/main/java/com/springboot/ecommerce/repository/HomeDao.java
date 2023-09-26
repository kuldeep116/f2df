package com.springboot.ecommerce.repository;

import java.util.List;

import com.springboot.ecommerce.model.AddSpace;
import com.springboot.ecommerce.model.Banner;
import com.springboot.ecommerce.model.ContactUs;
import com.springboot.ecommerce.model.Insurance;
import com.springboot.ecommerce.model.Loan;
import com.springboot.ecommerce.model.Training;
import com.springboot.ecommerce.pojo.CatExportQunatity;
import com.springboot.ecommerce.pojo.SubCatQunatity;

public interface HomeDao {
    public int addTraining(Training training);

	public int createLoan(Loan loan);

	public Object getNavigation();

	public List<Banner> getBanner(String type);

	public AddSpace getAddSpace();

	public Object getTrainingType();

	public int addInsuranceLead(Insurance insurance);

	public Object getTestimonials();

	public Object getBannerByCatId(int pc_id);

	public Object getBannerBySubCatId(int psc_id);

	int getCategoryBySubCat(int pscId);

	public List<CatExportQunatity> exportSubCatQuantity();

	public int addBanner(Banner req);

	void deleteBanner(int id);

	int updateBanner(Banner req);

	public Object getBannerLocation();

	public int createContactUs(ContactUs loan);

	public List<Training> getTraining(int page, int size);

	public List<Insurance> getInsurance(int page, int size);

	public List<Loan> getLoan(int page, int size);

	public int countLoan();

	public int countInsurance();

	public int countTraining();

	public int countAdBanner(String type);
   
}
