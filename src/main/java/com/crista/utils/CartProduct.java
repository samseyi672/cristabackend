package com.crista.utils;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("CartProduct")
public class CartProduct  implements Serializable {
	private static final long serialVersionUID = -1502835138464358818L;
		// this class  is the  class 
//for storing  cart products in memory in redis database
	    @Id
	    private Long id;
	    private String productname;
	    private String qty;
	    private String price;
	    private String total ;
	    private String sessionid ;
	    
}
























































































































