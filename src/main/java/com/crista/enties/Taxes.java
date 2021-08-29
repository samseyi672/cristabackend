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
@Table(name="taxes")
@Data
public class Taxes {
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name="taxdetail")
	private String taxdetail ;
	@Column(name="taxschedule")
	private String taxschedule;
	@Column(name="totaltaxamount")
	private String totaltaxamount;	
//	@Column(name="transactionid")
//	private String transactionid;	
//	@Column(name="transactionamount")
//	private String transactionamount;	
	@Column(name="taxrate")
	private String taxrate;
	
}


















































































































