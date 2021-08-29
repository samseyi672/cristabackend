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
@Table(name="message")
@Data
public class ServerMessages {
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name="vendorname")
	private String vendorname  ;
	@Column(name="message")
	private String message  ;
	@Column(name="userid")
	private String userid  ;
	@Column(name="view")
	private String view ;
}















































