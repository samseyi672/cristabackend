package com.crista.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crista.enties.Coupon;
import com.crista.enties.Product;
import com.crista.service.CouponService;

import net.bytebuddy.utility.RandomString;

@RestController
@RequestMapping("coupon")
public class CouponController {
	
	@Autowired
	CouponService couponService ;
	
	@PostMapping("/coupon")
	public String save(@Valid @RequestBody Coupon coupon,BindingResult result) {
		System.out.println("coupon "+ coupon);
		if(coupon.getCouponcode() == null) {
		 coupon.setCouponcode(RandomString.make(18));	
		     }
		couponService.create(coupon) ;
		 return "save" ;
	}

    @GetMapping("/coupon")
   public String findAllCoupon(@RequestParam("vendorname") String  vendorname,@RequestParam("pageSize") int pageSize, @RequestParam("pageNumber") int pageNumber){
	  return  couponService.findByVendor(vendorname,pageSize,pageNumber); 
  }
   @GetMapping("findcoupon/{id}")
   public Coupon findCoupon(@PathVariable("id") String id) {
	  return couponService.findCoupon(id) ;  
   }
   @DeleteMapping("/deletecoupon/{id}")
   public String deletecoupon(@PathVariable("id") String id) {
	   couponService.deleteCoupon(Long.parseLong(id)) ;  
	   return "deleted successfully" ;
   }
   
}

























































