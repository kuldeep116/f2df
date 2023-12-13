package com.springboot.ecommerce.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name ="urlset")
public class SiteUrlSet {
	
	
	
	ArrayList<SiteUrl> SiteUrlList = new ArrayList<>();

	public ArrayList<SiteUrl> getSiteUrlList() {
		return SiteUrlList;
	}
	 @XmlElement(name ="url")
	public void setSiteUrlList(ArrayList<SiteUrl> siteUrlList) {
		SiteUrlList = siteUrlList;
	}
	
	
	
}
