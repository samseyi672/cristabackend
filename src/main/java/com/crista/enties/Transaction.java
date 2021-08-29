package com.crista.enties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="transaction")
@Data
public class Transaction {	
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name="productname")
	private String productname ;
	@Column(name="orderid")
	private String orderid;
	@Column(name="price")
	private String price;
	@Column(name="userid")
	private String userid;
	@Column(name="productid")
	private String productid;
	@Column(name="producttype")
	private String producttype;
}


























































































































