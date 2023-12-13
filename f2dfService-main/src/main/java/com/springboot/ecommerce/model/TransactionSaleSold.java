package com.springboot.ecommerce.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/*this is Training type*/
@Entity
@Table(name = "TransactionSaleSold")
public class TransactionSaleSold {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "paymentId")
	private String paymentId;

	@Column(name = "userName")
	private String userName;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "invoice")
	private String invoice;

	@Column(name = "transactionId")
	private String transactionId;

	@Column(name = "qty")
	private int qty;

	@Column(name = "paidAmount")
	private double paidAmount;

	@Column(name = "status")
	private String status;
	
	@Column(name = "price")
	private String price;

	@Column(name = "productName")
	private String productName;

	@Column(name = "createDate")
	private String createDate;

	@Column(name = "updateDate")
	private String updateDate;
	
	@Column(name = "billing_date")
	private String billingDate;

	public String getBillingDate() {
		return billingDate;
	}

	public void setBillingDate(String billingDate) {
		this.billingDate = billingDate;
	}

	@Transient
	private List<Integer> paymentList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public List<Integer> getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(List<Integer> paymentList) {
		this.paymentList = paymentList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public static void main(String[] args) {
		System.out.println(new Date());
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "TransactionSale [id=" + id + ", paymentId=" + paymentId + ", transactionId=" + transactionId + ", qty="
				+ qty + ", paidAmount=" + paidAmount + ", status=" + status + ", createDate=" + createDate
				+ ", updateDate=" + updateDate + "]";
	}

}
