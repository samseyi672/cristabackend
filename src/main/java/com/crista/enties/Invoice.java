package com.crista.enties;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="invoice")
@Data
public class Invoice {
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name="invoiceid")
	private String invoiceid ;
	@Column(name="shipping")
	private String shipping;
	@Column(name="dateofcreation")
	private Date dateofcreation;
	@Column(name="amount")
	private String amount;
	@Column(name="userid")
	private String userid;
	@Column(name="tax")
	private String tax;
	@Column(name="total")
	private String total;
	@Column(name="transactionid")
	private String transactionid;
	@Column(name="dateofpay")
	private String dateofpay;
}
