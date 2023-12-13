package com.springboot.ecommerce.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(value = XmlAccessType.NONE) 
@XmlRootElement(name = "sitemapindex")
public class SiteMapIndex {
	
	
	
	ArrayList<SiteUrlNew> SiteUrlList = new ArrayList<>();

	public ArrayList<SiteUrlNew> getSiteUrlList() {
		return SiteUrlList;
	}
	 @XmlElement(name ="sitemap")
	public void setSiteUrlList(ArrayList<SiteUrlNew> siteUrlList) {
		SiteUrlList = siteUrlList;
	}
	
	
	
}
