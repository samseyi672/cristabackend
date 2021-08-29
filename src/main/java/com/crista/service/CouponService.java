package com.crista.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.crista.enties.Coupon;
import com.crista.enties.Product;
import com.crista.exceptions.CouponException;
import com.crista.repositories.CouponRepository;
import com.crista.utils.Pagination;

@Service
public class CouponService {

	 @Autowired
	 public CouponRepository couponRepo;
	 
	 public Coupon create(Coupon coupon) {
		 return couponRepo.save(coupon) ;
	 }
	 
	  Map hashMap = new HashMap();  // configure pagination properties
	    //paginated method 
	public String findByVendor(String vendorname,int pageSize, int pageNumber){
		String results= "";
		  int page2 = pageNumber/pageSize;
		  org.springframework.data.domain.Pageable pageable = PageRequest.of(page2, pageSize,Sort.by("id").ascending());
		  StringBuffer html  = new StringBuffer() ;	  
		 org.springframework.data.domain.Page<Coupon> datas= couponRepo.findByVendor(pageable,vendorname) ;
		 System.out.println(datas);
		 html.append("<table class=\"table table-striped table-borderless table-hover\">");
//		 <thead>
//		    <tr>								
//		      <th>Coupon Title</th>
//		      <th>Coupon code</th>
//		      <th>Status</th>
//		       <th>Discount Type</th>
//		        <th>Start Date</th>
//		         <th>End Date</th>
//		           <th>Action</th>
//		    </tr>
//		  </thead>
//		 <tbody id="couponbody">
//		  </tbody>
//		</table>
		 html.append("<thead><tr><th>Coupon Title</th><th>Coupon code</th><th>Status</th><th>Discount Type</th>") ;
		 html.append("<th>Start Date</th><th>End Date</th> <th>Action</th></thead><tbody>") ;
		 if(!datas.isEmpty()) {
			  // process  the  pagination
				for(Coupon c :datas) {
				html.append("<tr id="+c.getId()+">"+"<td>"+c.getCoupontitle()+"</td><td>"+c.getCouponcode()
				+"</td><td>"+c.getCouponstatus()+"</td><td>"+c.getDiscounttype()+"</td><td>"+c.getStartdate()
				+"</td><td>"+c.getEnddate()+"</td><td><span style=\"cursor:pointer;\" class=\"fa fa-eye\" onclick=\"edit("+c.getId()+")\"></span>&nbsp;&nbsp;<span style=\"cursor:pointer\" onclick=\"cancel("+c.getId()+")\" class=\"fa fa-remove\"></span></td></tr>");
				  }
				  results += "</tbody></table>";
			  	  hashMap.put("totalRows",datas.getTotalElements());
			      hashMap.put("perPage", pageSize);
			      hashMap.put("filter", "searchFilter2");       
			      hashMap.put("currentPage", pageNumber);  
			      Pagination pagination = new Pagination(hashMap);
			    //  System.out.println(html.toString());
			       results += html.toString() ;	
			      // results += "<div style=\"margin-bottom:120px;\"></div>" ;
				  results += pagination.paginate();
				}else {
					System.out.println("it is empty .....");
					results += "<center><h2> Coupon not available </h2></center>";
				} 
			return results;
	}
	
	public Coupon findCoupon(String id) {
		Coupon c  = couponRepo.getById(Long.parseLong(id)) ;
		if(c == null) {
			 throw new CouponException("Coupon is not available");
		}
		return c ;
	}

	public void deleteCoupon(Long id) {
		 couponRepo.deleteById(id);
	   } 
}
















































































