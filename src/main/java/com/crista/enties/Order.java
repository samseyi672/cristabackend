package com.crista.enties;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="productordered")
@Data
public class Order {
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name="productname")
	private String productname ;
	@Column(name="paymentstatus")
	private String paymentstatus ;
	@Column(name="ordstatus")
	private String ordstatus ;
	@Column(name="orderdate")
	private Date orderdate ;
	@Column(name="quantity")
	private String quantity ;
	@Column(name="town")
	private String town ;
	@Column(name="tax")
	private String tax ;
	@Column(name="phone")
	private String phone ;
	@Column(name="state")
	private String state ;
	@Column(name="email")
	private String email ;	
	@Column(name="country")
	private String country ;
	@Column(name="shipping")
	private boolean shipping ;
	@Column(name="localpickup")
	private boolean localpickup ;
	@Column(name="price")
	private String price;
	@Column(name="productid")
	private String productid ; 
	@Column(name="ordid")
	private String ordid ;
	@Column(name="address")
	private String address ;
	@Column(name="firstname")
	private String firstname ;
	@Column(name="lastname")
	private String lastname ;
	@Column(name="paymentgroup")
	private String paymentgroup;
	@Column(name="postalcode")
	private String postalcode;
//	@Column(name="paymentmethod")
//	private String paymentmethod;
	@Column(name="overalltotal")
	private String overalltotal;
	@Column(name="userid")
	private String userid;
	@Column(name="total")
	private String total;
	
	@PrePersist
	protected void onCreate() {
		this.orderdate = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.orderdate = new Date();
	}
}









































































































































































































































































































































































