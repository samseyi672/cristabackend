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
@Table(name="brands")
@Data
public class Brands {
	
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name = "productid")
	private long productid;
	@Column(name = "brandname")
	private String brandname;
}
















































































































































