package com.crista.enties;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "customer")
@Data
public class Customer {
  @Id	
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  @Column(name="firstname")
 private String firstname ;
  @Column(name="lastname")
 private String lastname ;
  @Column(name="phone") 
 private String phone ;
  @Column(name="email") 
 private String email ;
//  @Column(name="country")  
// private String country ;
  @Column(name="address")  
 private String address ;
  @Column(name="town")  
 private String town ;
  @Column(name="state") 
  private String state ;
 @Column(name="password")
 private String password ;
  @Column(name="postalcode") 
 private String postalcode ;
}






































































