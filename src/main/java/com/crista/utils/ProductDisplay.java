package com.crista.utils;

import lombok.Data;

@Data
public class ProductDisplay { // this class is to get some required  data from productordered class
	// to display as cart in fromnt page
   private String productname ;
   private long id ;
   private String imageurl ;
   private String productprice ;
   private String  productoldprice ;
   private String  productdescription ;
}
