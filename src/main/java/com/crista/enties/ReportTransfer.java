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
@Table(name="reporttransfer")
@Data
public class ReportTransfer {
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name="transfername")
	private String transfername ;
	@Column(name="transferid")
	private String transferid;
	@Column(name="transactionid")
	private String transactionid;
	@Column(name="total")
	private String total;
	@Column(name="userid")
	private String userid;
	@Column(name="datofcreation")
	private Date datofcreation;
}













































































































