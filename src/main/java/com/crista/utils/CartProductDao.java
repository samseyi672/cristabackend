package com.crista.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class CartProductDao {

	public static final String HASH_KEY = "CartProduct";

	@Autowired
	private RedisTemplate template;

	public CartProduct save(CartProduct product) {
		template.opsForHash().put(HASH_KEY, product.getId().toString(), product);
		return product;
	}

	public java.util.List<CartProduct> findAll() {
		return template.opsForHash().values(HASH_KEY);
	}

	public CartProduct findCartProductById(Long id) {
		return (CartProduct) template.opsForHash().get(HASH_KEY, id.toString());
	}

	public String deleteProduct(Long id) {
		template.opsForHash().delete(HASH_KEY, id.toString());
		return "product removed !!";
	}

}

























































































































