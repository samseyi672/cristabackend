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

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table(name="product")
@Data
public class Product {
	
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name="imageurl")
	private String imageurl ;
	@Column(name="productname")
	private String productname;
	@Column(name="shortsummary")
	private String shortsummary;
	@Column(name="newarrival")
	private String newarrival;
	@Column(name="priceofproduct")
	private String priceofproduct;
	@Column(name="special")
	private String special;
	@Column(name="featured")
	private String featured;
	@Column(name="metatitle")
	private String metatitle;
	@Column(name="prodtype")
	private String prodtype;
	@Column(name="productreview")
	private String productreview;
	@Column(name="productoldprice")
	private String productoldprice;
	@Column(name="frontpage")
	private boolean frontpage ;
	// remember to add old price, discount/promo price.
	@Column(name="title")
	private String title;
	@Column(name="tax")
	private String tax;
	@Column(name="valnumber")
	private String valnumber ;
	@Column(name="categoryid")
	private String categoryid;
	@Column(name="colour")
	private String colour[];
	@Column(name="proddescription")
	private String proddescription;
	@Column(name="productprice")
	private String productprice;
	@Column(name="subcategoryid")
	private String subcategoryid;
	@Column(name="productstatus")
	private String productstatus;
	@Column(name="productcode")
	private String productcode ;
	@Column(name="productquantity")
	private String productquantity;
	@Column(name="state")
	private String state;
	@Column(name="size")
	private String size ;
	@Column(name="manysizes")
	private String manysizes[] ;
	@Column(name="expirydate")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date expirydate;
	@Column(name="vendorname")
	private String vendorname;
	@Column(name="model")
	private String model;
	@Column(name="metadescription")
	private String metadescription;
	@Column(name="dateofcreation")
	private Date dateofcreation;
	@PrePersist
	protected void onCreate() {
		this.dateofcreation = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.dateofcreation = new Date();
	}
}


























































