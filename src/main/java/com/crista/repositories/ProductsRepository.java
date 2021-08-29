package com.crista.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import com.crista.enties.Product;
import com.crista.utils.ProductDisplay;

public interface ProductsRepository extends PagingAndSortingRepository<Product,Long> {
  Product getByid(long id) ; 
  
  List<Product>  findByVendorname(String vendorname); 
  Product findById(long id) ;
  //@Query("select p.productname,p.id from product p where p.vendorname=:vendorname and p.imageurl=null")
  List<Product> findByVendornameAndImageurlIsNull(@Param("vendorname") String vendorname);

  Page<Product> findAll(Pageable pageable) ;
  
  List<Product> getByCategoryid(String categoryid) ;
  
  @Query("select p from Product p where p.productname=:prdname and p.productprice <=:prdprice+1000")
    List<Product> retrieve(@Param("prdname") String prd,@Param("prdprice") String prdpr) ;
 
    @Query(value ="SELECT * FROM product where productname like :prdname order by rand() limit 0,:lmt", nativeQuery = true)
	public List<Product> findTopN(@Param("prdname") String prd,@Param("lmt") int lmt) ;
    
  //List<Product> getByProductnameAndProductprice(String productname,String productprice) ;
  
   // write some native  query to query product randomly
  @Query(value = "{call GetProducts(:min,:max)}",nativeQuery=true)
  List<Product> findAll(@Param("min") int min,@Param("max") int max) ;
  
  // by category
  @Query(value = "{call GetProductsByCategoryid(:min,:max,:category)}",nativeQuery=true)
  List<Product> findAll(@Param("min") int min,@Param("max") int max,String category) ;
 
  @Query(value ="SELECT * FROM product where categoryid=:category order by rand() limit :min,:max", nativeQuery = true)
  Page<Product> findAll(@Param("min") int min,@Param("max") int max,@Param("category") String category,Pageable pageable) ;
  
  @Query(value ="SELECT * FROM product where categoryid=:category", nativeQuery = true)
  Page<Product> findAll(@Param("category") String category,Pageable pageable) ;
  
  @Query(value =":query", nativeQuery = true)
  Page<Product>  dynamicQuery(@Param("query") String query,Pageable pageable) ;
  
  //@Query(value ="SELECT * FROM product where categoryid=:category", nativeQuery = true)
  Page<Product> getByCategoryid(String categoryid , Pageable pageable) ;
  
  
   // by featured
  @Query(value = "{call GetProductsByFeatured(:min,:max)}",nativeQuery=true)
  List<Product> findByFeatured(@Param("min") int min,@Param("max") int max) ;
   
   // by special
  @Query(value = "{call GetProductsBySpecial(:min,:max)}",nativeQuery=true)
  List<Product> findBySpecial(@Param("min") int min,@Param("max") int max) ;
  
//  @Query("select p from Product p where p.special=:special")
//  Page<Product> getBySpecial(Pageable pageable,String special) ;
  
  // by new arrival
  @Query(value = "{call GetProductsByNewArrival(:min,:max)}",nativeQuery=true)
  List<Product> findByNewarrival(@Param("min") int min,@Param("max") int max) ;
  //call GetProductsByCategoryid(0,20,'elect')
  
  @Query(value = "{call GetProductsByCategoryid(:min,:max,:categoryid)}",nativeQuery=true)
  List<Product> findByCategoryidAndProductnameAndSubcategoryid(@Param("min") int min,@Param("max") int max,String categoryid) ;

    @Async
    @Query(value ="SELECT * FROM product where productname where id=:id", nativeQuery = true)
    CompletableFuture<Product> retrieveProductById(@Param("id") long id);
    
   // @Async
   // @Query(value ="SELECT * FROM product where productname where id=:id", nativeQuery = true)
    Product getById(long id);
    
     //@Async
    @Query(value ="SELECT * FROM product where productname like :prdname order by rand() limit 0,:lmt", nativeQuery = true)
 	public List<Product> searchTopN(@Param("prdname") String prd,@Param("lmt") int size) ;
}






















































































































































