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
@Table(name="vendor")
@Data
public class Vendor {
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name="vendorname")
	private String vendorname ;
	@Column(name="vendorlogo")
	private String vendorlogo;
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
