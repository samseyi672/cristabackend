package com.crista.enties;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="usertype")
@Data
public class UserType {
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name="usertype")
	private String usertype ;
	@Column(name="userid")
	private String userid;
	@Column(name="overtime")
	private String overtime;
	@Column(name="dateofcreation")
	private Date dateofcreation;
	@Column(name="leavestaken")
	private String leavestaken;
	      @PrePersist
	    protected void onCreate(){
	        this.dateofcreation = new Date();
	    }
	     @PreUpdate
	    protected void onUpdate(){
	        this.dateofcreation = new Date();
	    }
}
