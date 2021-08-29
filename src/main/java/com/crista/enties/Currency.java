package com.crista.enties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="currency")
@Data
public class Currency {
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name="currencyttle")
	private String currencyttle ;
	@Column(name="exchangerate")
	private String exchangerate;
	@Column(name="currencydate")
	private String currencydate;
}
