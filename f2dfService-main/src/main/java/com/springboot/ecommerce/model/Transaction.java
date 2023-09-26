package com.springboot.ecommerce.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/*this is Training type*/
@Entity
@Table(name = "Transaction")
public class Transaction {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "userId")
	private int userId;

	@Column(name = "cartId")
	private int cartId;

	@Column(name = "paymentId")
	private String paymentId;

	@Column(name = "orderId")
	private String orderId;

	@Column(name = "signature")
	private String signature;

	@Column(name = "paidAmount")
	private double paidAmount;

	@Column(name = "code")
	private int code;
	
	@Column(name = "type")
	private int type;
	
	@Column(name = "message")
	private String message;

	@Column(name = "error")
	private String error;

	@Column(name = "status")
	private String status;

	@Column(name = "createDate")
	private String createDate;

	@Column(name = "updateDate")
	private String updateDate;
	
	@Transient
	private int p_Id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getP_Id() {
		return p_Id;
	}

	public void setP_Id(int p_Id) {
		this.p_Id = p_Id;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", userId=" + userId + ", cartId=" + cartId + ", paymentId=" + paymentId
				+ ", orderId=" + orderId + ", signature=" + signature + ", paidAmount=" + paidAmount + ", code=" + code
				+ ", type=" + type + ", message=" + message + ", error=" + error + ", status=" + status
				+ ", createDate=" + createDate + ", updateDate=" + updateDate + ", p_Id=" + p_Id + "]";
	}

	
}
