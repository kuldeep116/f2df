package com.springboot.ecommerce.repository;

import com.springboot.ecommerce.model.SubCategoryFeature;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubCategoryFeatureDaoImpl implements SubCategoryFeatureDao {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public int addSubCategoryFeature(SubCategoryFeature subCategoryFeature) {
        Session session = sessionFactory.getCurrentSession();
        session.save(subCategoryFeature);
        return subCategoryFeature.getScf_id();
    }

    @Override
    public void deleteSubCategoryFeature(int id) {
    	String sql=" delete from SubCategoryFeature p where p.scf_id ="+id;
		Session session = sessionFactory.getCurrentSession();
		session.createQuery(sql).executeUpdate();
    }

    @Override
    public int updateSubCategoryFeature(SubCategoryFeature subCategoryFeature) {
        Session session = sessionFactory.getCurrentSession();
        session.createQuery(
				" update com.springboot.ecommerce.model.SubCategoryFeature scf  set scf.subCategoryFeatureName =:subCategoryFeatureName,scf.featureType =:featureType,scf.productFeatureDetail =:productFeatureDetail where scf.scf_id =:scf_id")
				.setParameter("subCategoryFeatureName", subCategoryFeature.getSubCategoryFeatureName())
				.setParameter("featureType",  subCategoryFeature.getFeatureType()).setParameter("productFeatureDetail",  subCategoryFeature.getProductFeatureDetail())
				.setParameter("scf_id",  subCategoryFeature.getScf_id()).executeUpdate();
        return subCategoryFeature.getScf_id();
    }

    @Override
    public SubCategoryFeature findSubCategoryFeatureById(int id) {
        Session session = sessionFactory.getCurrentSession();
        SubCategoryFeature subCategoryFeature = (SubCategoryFeature) session.get(SubCategoryFeature.class, id);
        return subCategoryFeature;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SubCategoryFeature> getSubCategoryFeatures() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from com.springboot.ecommerce.model.SubCategoryFeature").list();
    }
}
