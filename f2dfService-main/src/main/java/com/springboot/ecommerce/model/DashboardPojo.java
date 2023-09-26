package com.springboot.ecommerce.model;

import java.text.DecimalFormat;

public class DashboardPojo {

	private String name;

	private String value;

	private int intvalue;

	private String percentage;
	
	private String icon;
	
	private String reportLink;

	DecimalFormat format = new DecimalFormat("##,##,##0");

	public DashboardPojo(String name, int intvalue, String icon,String reportLink) {
		super();
		this.name = name;
		this.intvalue = intvalue;
		this.value = format.format(intvalue);
		this.icon = icon;
		this.reportLink = reportLink;
	}

	public DashboardPojo() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getIntvalue() {
		return intvalue;
	}

	public void setIntvalue(int d) {
		this.intvalue = d;
	}

	public DecimalFormat getFormat() {
		return format;
	}

	public void setFormat(DecimalFormat format) {
		this.format = format;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
		
	}

	public String getReportLink() {
		return reportLink;
	}

	public void setReportLink(String reportLink) {
		this.reportLink = reportLink;
	}

}
