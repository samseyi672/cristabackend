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
@Table(name="coupon")
@Data
public class Coupon {
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name="coupontitle")
	private String coupontitle ;
	@Column(name="couponcode")
	private String couponcode;
	@Column(name="startdate")
	private Date startdate;
	@Column(name="enddate")
	private Date enddate;
	@Column(name="freeshipping")
	private String freeshipping;
	@Column(name="couponqty")
	private String couponqty ;
	@Column(name="discounttype")
	private String discounttype;
	@Column(name="couponstatus")
	private String couponstatus;
	@Column(name="couponproductid")
	private String couponproductid ;
	@Column(name="couponcategory")
	private String couponcategory;
	@Column(name="minspend")
	private String minspend;
	@Column(name="maxspend")
	private String maxspend ;
	@Column(name="perlimit")
	private String perlimit;
	@Column(name="percustomer")
	private String percustomer  ;
	@Column(name="couponsubcategory")
	private String couponsubcategory  ;
	@Column(name="vendor")
	private String vendor ;
}







