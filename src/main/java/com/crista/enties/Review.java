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
@Table(name="review")
@Data
public class Review {
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name="customername")
	private String customername  ;
	@Column(name="email")
	private String email  ;
	@Column(name="review")
	private String review  ;
	@Column(name="rate")
	private String rate  ;
	@Column(name="customertestimony")
	private String testimony  ;
	@Column(name="productid")
	private String productid  ;
	@Column(name="reviewdate")
	private Date reviewdate  ;
	@PrePersist
	protected void onCreate() {
		this.reviewdate = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.reviewdate = new Date();
	}
}




































































































