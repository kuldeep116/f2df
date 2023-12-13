package com.springboot.ecommerce.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*this is Training type*/
@Entity
@Table(name = "TransactionSale")
public class TransactionSale {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "productId")
	private int productId;

	@Column(name = "productName")
	private String productName;

	
	@Column(name = "price")
	private String price;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "paymentId")
	private int paymentId;

	@Column(name = "transactionId")
	private String transactionId;

	@Column(name = "qty")
	private int qty;
	
	@Column(name = "invoice")
	private String invoice;

	@Column(name = "paidAmount")
	private double paidAmount;

	@Column(name = "mobile")
	private String mobile;
	
	@Column(name = "finalBuyerName")
	private String finalBuyerName;
	
	@Column(name = "po_date")
	private String poDate;
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFinalBuyerName() {
		return finalBuyerName;
	}

	public String getPoDate() {
		return poDate;
	}

	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}

	public void setFinalBuyerName(String finalBuyerName) {
		this.finalBuyerName = finalBuyerName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "status")
	private String status;
	
	@Column(name = "comment")
	private String comment;

	@Column(name = "createDate")
	private String createDate;

	@Column(name = "updateDate")
	private String updateDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
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

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "TransactionSale [id=" + id + ", productId=" + productId + ", productName=" + productName
				+ ", paymentId=" + paymentId + ", transactionId=" + transactionId + ", qty=" + qty + ", paidAmount="
				+ paidAmount + ", status=" + status + ", createDate=" + createDate + ", updateDate=" + updateDate + "]";
	}
	
	
}
