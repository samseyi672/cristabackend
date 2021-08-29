package com.crista.enties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="subcategories")
@Data
public class SubCategories {
	    @Id	
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@Column(name = "id")
		private long id;
		@Column(name="imageurl")
		private String imageurl ;
		@Column(name="subcategoryname")
		private String subcategoryname;
		@Column(name="pricerange")
		private String pricerange;
		@Column(name="categoryid")
		private String categoryid;
}








































