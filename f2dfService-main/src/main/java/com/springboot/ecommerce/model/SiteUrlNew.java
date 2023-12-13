package com.springboot.ecommerce.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SiteUrlNew
{
	private String loc;
	
	private String lastmod;
	
	public SiteUrlNew() {
		
	
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
		lastmod= formatter.format(new Date()); 
		
	}
	public String getLoc() {
		return loc;
	}
	@XmlElement
	public void setLoc(String loc) {
		this.loc = loc;
	}
	
	public String getLastmod() {
		return lastmod;
	}
	@XmlElement
	public void setLastmod(String lastmod) {
		this.lastmod = lastmod;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((loc == null) ? 0 : loc.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SiteUrlNew other = (SiteUrlNew) obj;
		if (loc == null) {
			if (other.loc != null)
				return false;
		} else if (!loc.equals(other.loc))
			return false;
		return true;
	}


	

	
	
}